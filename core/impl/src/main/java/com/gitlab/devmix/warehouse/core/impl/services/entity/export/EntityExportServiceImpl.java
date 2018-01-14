package com.gitlab.devmix.warehouse.core.impl.services.entity.export;

import com.gitlab.devmix.warehouse.core.api.entity.importexport.ExportProcess;
import com.gitlab.devmix.warehouse.core.api.entity.security.User;
import com.gitlab.devmix.warehouse.core.api.repositories.ExportProcessRepository;
import com.gitlab.devmix.warehouse.core.api.services.entity.importexport.Entity;
import com.gitlab.devmix.warehouse.core.api.services.entity.importexport.EntityExportService;
import com.gitlab.devmix.warehouse.core.api.services.entity.importexport.ExportOptions;
import com.gitlab.devmix.warehouse.core.impl.utils.SecurityUtils;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;
import java.util.stream.Collectors;

import static com.gitlab.devmix.warehouse.core.api.entity.importexport.AbstractProcess.State.PROCESSING;
import static com.gitlab.devmix.warehouse.core.api.entity.importexport.AbstractProcess.State.WAITING;
import static com.gitlab.devmix.warehouse.core.impl.config.AsyncConfiguration.SYS_EXPORT_ENTITY_TASK_EXECUTOR;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.springframework.transaction.annotation.Propagation.NOT_SUPPORTED;

/**
 * @author Sergey Grachev
 */
@Service
@Transactional(propagation = NOT_SUPPORTED)
public class EntityExportServiceImpl implements EntityExportService {

    @Inject
    @Named(SYS_EXPORT_ENTITY_TASK_EXECUTOR)
    private ThreadPoolTaskExecutor executor;

    @Inject
    private ExportProcessRepository repository;

    @Inject
    private ExportTask task;

    @Inject
    private EntityManager em;

    @PostConstruct
    private void onCreate() {
        executor.execute(task);
    }

    @PreDestroy
    private void onDestroy() {
        executor.shutdown();
    }

    @Override
    public ExportProcess create(final ExportOptions options) {
        final ExportProcess entity = new ExportProcess();

        entity.setUser(em.getReference(User.class, SecurityUtils.userID()));
        entity.setOptions(options.serialize());

        if (isNotEmpty(options.getEntities())) {
            entity.setEntities(options.getEntities().stream()
                    .map(Entity::getName).collect(Collectors.joining(", ")));
        }

        final ExportProcess merge = repository.save(entity);

        task.trigger();

        return merge;
    }

    @Override
    public void active(final boolean active) {
        if (!task.isActive() && active) {
            repository.switchState(PROCESSING, WAITING);
        }
        task.active(active);
    }
}
