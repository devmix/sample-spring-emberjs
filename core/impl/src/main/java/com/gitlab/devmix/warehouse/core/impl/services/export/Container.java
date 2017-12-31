package com.gitlab.devmix.warehouse.core.impl.services.export;

import com.gitlab.devmix.warehouse.core.api.web.entity.export.ExportOptions;

import java.io.IOException;
import java.util.Map;

/**
 * @author Sergey Grachev
 */
public interface Container extends AutoCloseable {

    void store(String name, Object id, Map<String, Object> data) throws IOException;

    void store(ExportOptions options) throws IOException;
}
