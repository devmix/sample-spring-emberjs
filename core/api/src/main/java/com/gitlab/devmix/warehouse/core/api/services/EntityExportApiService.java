package com.gitlab.devmix.warehouse.core.api.services;

import com.gitlab.devmix.warehouse.core.api.entity.export.ExportProcess;
import com.gitlab.devmix.warehouse.core.api.web.entity.export.ExportOptions;

/**
 * @author Sergey Grachev
 */
public interface EntityExportApiService {

    ExportProcess create(ExportOptions options);

    void active(boolean active);
}
