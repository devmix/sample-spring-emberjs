package com.gitlab.devmix.warehouse.core.api.services.entity.importexport;

import com.gitlab.devmix.warehouse.core.api.entity.importexport.ExportProcess;

/**
 * @author Sergey Grachev
 */
public interface EntityExportService {

    ExportProcess create(ExportOptions options);

    void active(boolean active);
}
