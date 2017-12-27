package com.gitlab.devmix.warehouse.core.api.services.filestorage;

/**
 * @author Sergey Grachev
 */
public interface FileStorageStream extends AutoCloseable {

    void close();

    FileStreamMeta getMeta();
}
