package com.gitlab.devmix.warehouse.core.impl.services.entity.imprt;

import com.gitlab.devmix.warehouse.core.api.entity.importexport.ImportProcess;
import com.gitlab.devmix.warehouse.core.api.entity.security.User;
import com.gitlab.devmix.warehouse.core.api.repositories.ImportProcessRepository;
import com.gitlab.devmix.warehouse.core.api.services.entity.importexport.EntityImportService;
import com.gitlab.devmix.warehouse.core.api.services.entity.importexport.ImportOptions;
import com.gitlab.devmix.warehouse.core.impl.utils.SecurityUtils;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManager;

import static com.gitlab.devmix.warehouse.core.api.entity.importexport.AbstractProcess.State.PROCESSING;
import static com.gitlab.devmix.warehouse.core.api.entity.importexport.AbstractProcess.State.WAITING;
import static com.gitlab.devmix.warehouse.core.impl.config.AsyncConfiguration.SYS_IMPORT_ENTITY_TASK_EXECUTOR;
import static org.springframework.transaction.annotation.Propagation.NOT_SUPPORTED;

/**
 * @author Sergey Grachev
 */
@Service
@Transactional(propagation = NOT_SUPPORTED)
public class EntityImportServiceImpl implements EntityImportService {

    @Inject
    @Named(SYS_IMPORT_ENTITY_TASK_EXECUTOR)
    private ThreadPoolTaskExecutor executor;

    @Inject
    private ImportProcessRepository repository;

    @Inject
    private ImportTask task;

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
    public ImportProcess create(final ImportOptions options) {
        final ImportProcess entity = new ImportProcess();
        entity.setUser(em.getReference(User.class, SecurityUtils.userID()));

        task.trigger();

        return entity;
    }

    @Override
    public void active(final boolean active) {
        if (!task.isActive() && active) {
            repository.switchState(PROCESSING, WAITING);
        }
        task.active(active);
    }
}
