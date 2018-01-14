package com.gitlab.devmix.warehouse.core.api.services.entity.importexport;

import com.gitlab.devmix.warehouse.core.api.entity.importexport.ImportProcess;

/**
 * @author Sergey Grachev
 */
public interface EntityImportService {

    ImportProcess create(ImportOptions options);

    void active(boolean active);
}
