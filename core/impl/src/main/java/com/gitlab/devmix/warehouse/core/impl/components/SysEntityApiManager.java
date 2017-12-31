package com.gitlab.devmix.warehouse.core.impl.components;

import com.gitlab.devmix.warehouse.core.api.entity.export.ExportProcess;
import com.gitlab.devmix.warehouse.core.api.services.EntityRestRegistry;
import com.gitlab.devmix.warehouse.core.api.web.entity.Endpoint;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.inject.Inject;

import static com.gitlab.devmix.warehouse.core.api.web.entity.Endpoint.list;
import static com.gitlab.devmix.warehouse.core.api.web.entity.handlers.OperationHandlers.jpaReadList;

/**
 * @author Sergey Grachev
 */
@Component
public class SysEntityApiManager {

    @Inject
    private EntityRestRegistry registry;

    @PostConstruct
    private void onCreate() {
        registry.add(exportProcess());
    }

    private Endpoint exportProcess() {
        final Class<ExportProcess> e = ExportProcess.class;
        return Endpoint.builder("/sys/export-process")
                .add(list(e)
                        .projection("id", "state", "startDate", "finishDate", "percent", "entities")
                        .handler(jpaReadList(e).build()).build())
                .build();
    }
}
