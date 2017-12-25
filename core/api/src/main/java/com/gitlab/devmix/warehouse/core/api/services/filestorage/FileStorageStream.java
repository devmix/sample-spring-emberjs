package com.gitlab.devmix.warehouse.core.api.services.filestorage;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Sergey Grachev
 */
public interface FileStorageStream extends AutoCloseable {

    void close();

    FileStreamMeta getMeta();
}
