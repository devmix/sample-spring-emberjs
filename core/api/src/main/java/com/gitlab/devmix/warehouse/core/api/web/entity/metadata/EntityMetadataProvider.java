package com.gitlab.devmix.warehouse.core.api.web.entity.metadata;

import java.util.List;

/**
 * @author Sergey Grachev
 */
public interface EntityMetadataProvider {

    List<Class<?>> listClassBaseForScan();
}
