package com.gitlab.devmix.warehouse.core.impl.services.export;

import com.gitlab.devmix.warehouse.core.api.entity.export.ExportProcess;
import com.gitlab.devmix.warehouse.core.api.entity.security.User;
import com.gitlab.devmix.warehouse.core.api.repositories.ExportProcessRepository;
import com.gitlab.devmix.warehouse.core.api.services.EntityExportApiService;
import com.gitlab.devmix.warehouse.core.api.web.entity.export.Entity;
import com.gitlab.devmix.warehouse.core.api.web.entity.export.ExportOptions;
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

import static com.gitlab.devmix.warehouse.core.api.entity.export.ExportProcess.State.PROCESSING;
import static com.gitlab.devmix.warehouse.core.api.entity.export.ExportProcess.State.WAITING;
import static com.gitlab.devmix.warehouse.core.impl.config.AsyncConfiguration.SYS_EXPORT_ENTITY_TASK_EXECUTOR;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
import static org.springframework.transaction.annotation.Propagation.NOT_SUPPORTED;

/**
 * @author Sergey Grachev
 */
@Service
public class EntityExportApiServiceImpl implements EntityExportApiService {

    @Inject
    @Named(SYS_EXPORT_ENTITY_TASK_EXECUTOR)
    private ThreadPoolTaskExecutor taskExecutor;

    @Inject
    private ExportProcessRepository repository;

    @Inject
    private EntityExportTask exportTask;

    @Inject
    private EntityManager em;

    @PostConstruct
    private void onCreate() {
        taskExecutor.execute(exportTask);
    }

    @PreDestroy
    private void onDestroy() {
        taskExecutor.shutdown();
    }

    @Transactional(propagation = NOT_SUPPORTED)
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

        exportTask.trigger();

        return merge;
    }

    @Transactional(propagation = NOT_SUPPORTED)
    @Override
    public void active(final boolean active) {
        if (!exportTask.isActive() && active) {
            repository.switchState(PROCESSING, WAITING);
        }
        exportTask.active(active);
    }
}
