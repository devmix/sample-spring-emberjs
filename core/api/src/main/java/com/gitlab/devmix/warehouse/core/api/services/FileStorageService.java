package com.gitlab.devmix.warehouse.core.api.services;

import com.gitlab.devmix.warehouse.core.api.services.filestorage.FileStorageInputStream;
import com.gitlab.devmix.warehouse.core.api.services.filestorage.FileStorageOutputStream;
import com.gitlab.devmix.warehouse.core.api.services.filestorage.FileStreamOpenException;
import com.gitlab.devmix.warehouse.core.api.services.filestorage.FileStreamSelector;

/**
 * @author Sergey Grachev
 */
public interface FileStorageService {

    void remove(FileStreamSelector selector);

    void removeAll(FileStreamSelector selector);

    FileStorageInputStream openInputStream(FileStreamSelector selector) throws FileStreamOpenException;

    FileStorageOutputStream openOutputStream(FileStreamSelector selector) throws FileStreamOpenException;
}
