package com.gitlab.devmix.warehouse.core.impl.services.entity.export;

import com.gitlab.devmix.warehouse.core.api.entity.importexport.ExportProcess;
import com.gitlab.devmix.warehouse.core.api.repositories.ExportProcessRepository;
import com.gitlab.devmix.warehouse.core.api.services.FileStorageService;
import com.gitlab.devmix.warehouse.core.api.services.entity.importexport.Entity;
import com.gitlab.devmix.warehouse.core.api.services.entity.importexport.ExportOptions;
import com.gitlab.devmix.warehouse.core.api.services.entity.importexport.FormatType;
import com.gitlab.devmix.warehouse.core.api.services.filestorage.FileStorageOutputStream;
import com.gitlab.devmix.warehouse.core.api.services.filestorage.FileStreamSelector;
import com.gitlab.devmix.warehouse.core.api.web.entity.Projection;
import com.gitlab.devmix.warehouse.core.api.web.entity.ProjectionProperty;
import com.gitlab.devmix.warehouse.core.api.web.entity.metadata.EntityMetadata;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import javax.annotation.Nullable;
import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import static com.gitlab.devmix.warehouse.core.api.entity.importexport.AbstractProcess.State.FAILED;
import static com.gitlab.devmix.warehouse.core.api.entity.importexport.AbstractProcess.State.FINISHED;
import static com.gitlab.devmix.warehouse.core.api.entity.importexport.AbstractProcess.State.PROCESSING;
import static com.gitlab.devmix.warehouse.core.api.entity.importexport.AbstractProcess.State.WAITING;
import static java.util.Collections.emptySet;
import static java.util.Collections.singletonList;
import static org.apache.commons.collections4.CollectionUtils.isEmpty;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

/**
 * @author Sergey Grachev
 */
@Component
@Slf4j
class ExportTask implements Runnable {

    private static final String FOLDER_EXPORT = "sys/export";
    private static final int BATCH_SIZE = 100;
    // TODO SG not thread safe
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat("yyyyMMddHHmmssSSS");

    @Inject
    private ExportProcessRepository repository;

    @Inject
    private FileStorageService fileStorageService;

    @Inject
    private EntityManager em;

    @Inject
    private ExportCache cache;

    private final Object monitor = new Object();

    private volatile boolean active;

    public void trigger() {
        synchronized (monitor) {
            monitor.notify();
        }
    }

    @Override
    public void run() {
        final Thread thread = Thread.currentThread();
        while (!thread.isInterrupted() && thread.isAlive()) {
            if (!tryExport() && !thread.isInterrupted()) {
                synchronized (monitor) {
                    try {
                        monitor.wait(TimeUnit.MINUTES.toMillis(1));
                    } catch (final InterruptedException e) {
                        break;
                    }
                }
            }
        }
    }

    @SuppressWarnings("checkstyle:illegalcatch")
    private boolean tryExport() {
        if (!active) {
            return false;
        }

        try {
            final ExportProcess process = repository.findFirstByState(WAITING);
            if (process != null) {
                try {
                    updateState(process, PROCESSING);
                    export(process);
                    updateState(process, FINISHED);
                } catch (final Exception e) {
                    log.error(e.getMessage(), e);
                    try {
                        updateState(process, FAILED);
                    } catch (final Exception e2) {
                        log.error(e2.getMessage(), e2);
                    }
                } finally {
                    cache.clear();
                }
                return true;
            }
        } catch (final Exception e) {
            log.error(e.getMessage(), e);
        }

        return false;
    }

    private void export(final ExportProcess process) throws Exception {
        final ExportOptions options = ExportOptions.deserialize(process.getOptions());
        final FileStreamSelector file = FileStreamSelector.builder()
                .name("export-" + DATE_FORMAT.format(new Date()) + containerExtension(options))
                .folder(FOLDER_EXPORT).file(process.getId().toString())
                .build();

        boolean success = false;
        try (FileStorageOutputStream stream = fileStorageService.openOutputStream(file)) {
            final Container container = createContainer(options, stream);
            final Projection projection = isEmpty(options.getProjection())
                    ? Projection.full() : Projection.of(options.getProjection());

            for (final Entity entity : options.getEntities()) {
                final EntityMetadata.Descriptor meta = EntityMetadata.of(entity.getName());
                if (meta == null) {
                    throw new IllegalArgumentException("Not found metadata for the entity '" + entity.getName() + "'");
                }

                final int max = options.getOffset() + (options.getLimit() <= 0 ? Integer.MAX_VALUE : options.getLimit());
                for (int offset = options.getOffset(); offset < max; offset += BATCH_SIZE) {
                    cache.clearEntities();

                    final List<Object> entities = loadEntities(meta.getEntityClass(), offset, BATCH_SIZE);
                    if (entities.isEmpty() || !exportEntities(container, meta, projection, entities)) {
                        break;
                    }
                }
            }

            container.store(options);
            container.close();
            success = true;
        } finally {
            if (!success) {
                fileStorageService.remove(file);
            }
        }
    }

    private boolean exportEntities(final Container container, final EntityMetadata.Descriptor meta,
                                   final ProjectionProperty projection, final Collection<Object> entities) throws IOException {

        final Set<Object> ids = meta.isWithAssociations() ? meta.readId(entities) : emptySet();

        for (final Object entity : entities) {
            final Object id = meta.readId(entity);
            if (cache.isStored(meta.getEntityClass(), id)) {
                continue;
            }

            final Map<String, Object> data = new HashMap<>();
            for (final Map.Entry<String, EntityMetadata.Descriptor.Attribute> entry : meta.getAttributes().entrySet()) {
                final String name = entry.getKey();
                final EntityMetadata.Descriptor.Attribute attribute = entry.getValue();
                if (!attribute.hasGetter()) {
                    continue;
                }

                final ProjectionProperty projectionProperty = projection.find(name);
                if (projectionProperty == null) {
                    continue;
                }

                final Object value = attribute.readValue(entity);
                if (value == null) {
                    continue;
                }

                if (attribute.isAssociationMany()) {
                    final Collection<Object> associations = loadAssociations(id, meta, name, attribute, ids);
                    if (isNotEmpty(associations)) {
                        final EntityMetadata.Descriptor associationMeta = EntityMetadata.of(attribute.getEntityType());
                        exportEntities(container, associationMeta, projection.find(name), associations);
                        data.put(name, associationMeta.readId(associations));
                    }
                } else if (attribute.isAssociationOne()) {
                    final Object association = loadAssociation(id, meta, name, attribute, ids);
                    if (association != null) {
                        final EntityMetadata.Descriptor associationMeta = EntityMetadata.of(attribute.getEntityType());
                        exportEntities(container, associationMeta, projection.find(name), singletonList(association));
                        data.put(name, associationMeta.readId(association));
                    }
                } else {
                    data.put(name, value);
                }
            }
            container.store(meta.getEntityName(), id, data);
            cache.addToStored(meta.getEntityClass(), id);
        }

        return true;
    }

    private <T> List<T> loadEntities(final Class<T> entityClass, final int offset, final int limit) {
        final CriteriaBuilder cb = em.getCriteriaBuilder();
        final CriteriaQuery<T> q = cb.createQuery(entityClass);
        return em.createQuery(q.select(q.from(entityClass)))
                .setFirstResult(offset).setMaxResults(limit).getResultList();
    }

    private Collection<Object> loadAssociations(final Object entityId, final EntityMetadata.Descriptor entityMeta, final String associationName,
                                                final EntityMetadata.Descriptor.Attribute association,
                                                final Set<Object> prefetchIds) {

        final EntityMetadata.Descriptor associationMeta = EntityMetadata.of(association.getEntityType());
        return cache.loadAssociations(
                entityMeta, associationMeta, associationName, entityId, prefetchIds, association::newCollection);
    }

    @Nullable
    private Object loadAssociation(final Object entityId, final EntityMetadata.Descriptor entityMeta, final String associationName,
                                   final EntityMetadata.Descriptor.Attribute association, final Set<Object> prefetchIds) {

        final EntityMetadata.Descriptor associationMeta = EntityMetadata.of(association.getEntityType());
        final Collection<Object> objects = cache.loadAssociations(
                entityMeta, associationMeta, associationName, entityId, prefetchIds, ArrayList::new);

        return isEmpty(objects) ? null : objects.iterator().next();
    }

    private void updateState(final ExportProcess process, final ExportProcess.State state) {
        process.setState(state);
        if (state == PROCESSING) {
            process.setStartDate(new Date());
        } else if (state == FINISHED || state == FAILED) {
            process.setFinishDate(new Date());
        }
        repository.save(process);
    }

    private Container createContainer(final ExportOptions options, final FileStorageOutputStream stream) {
        switch (options.getContainer()) {
            case ZIP:
                return ContainerZip.builder()
                        .format(FormatType.JSON)
                        .stream(stream.getOutputStream())
                        .build();
            default:
                throw new IllegalArgumentException("Container type '" + options.getContainer() + "' is not supported");
        }
    }

    private String containerExtension(final ExportOptions options) {
        if (options.getContainer() == ExportOptions.ContainerType.ZIP) {
            return ".zip";
        }
        return "";
    }

    public void active(final boolean active) {
        this.active = active;
        if (active) {
            trigger();
        }
    }

    public boolean isActive() {
        return active;
    }
}
