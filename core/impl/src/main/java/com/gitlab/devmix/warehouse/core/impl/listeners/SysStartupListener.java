package com.gitlab.devmix.warehouse.core.impl.listeners;

import com.gitlab.devmix.warehouse.core.api.services.EntityExportApiService;
import com.gitlab.devmix.warehouse.core.api.web.entity.metadata.EntityMetadata;
import com.gitlab.devmix.warehouse.core.api.web.entity.metadata.EntityMetadataProvider;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.List;

import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;

/**
 * @author Sergey Grachev
 */
@Component
public class SysStartupListener implements ApplicationListener<ApplicationReadyEvent> {

    @Inject
    private EntityExportApiService entityExportApiService;

    @Inject
    private List<EntityMetadataProvider> metadataProviders;

    @Override
    public void onApplicationEvent(final ApplicationReadyEvent event) {
        readMetadata();
        entityExportApiService.active(true);
    }

    private void readMetadata() {
        if (metadataProviders == null) {
            return;
        }

        for (final EntityMetadataProvider provider : metadataProviders) {
            final List<Class<?>> classes = provider.listClassBaseForScan();
            if (isNotEmpty(classes)) {
                EntityMetadata.scanClassBase(classes);
            }
        }
    }
}
