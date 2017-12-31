package com.gitlab.devmix.warehouse.core.impl.components;

import com.gitlab.devmix.warehouse.core.api.App;
import com.gitlab.devmix.warehouse.core.api.web.entity.metadata.EntityMetadataProvider;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.Collections.singletonList;

/**
 * @author Sergey Grachev
 */
@Component
public class SysEntityMetadataProvider implements EntityMetadataProvider {

    @Override
    public List<Class<?>> listClassBaseForScan() {
        return singletonList(App.class);
    }
}
