package com.gitlab.devmix.warehouse.core.api.services.filestorage;

import java.io.OutputStream;

/**
 * @author Sergey Grachev
 */
public interface FileStorageOutputStream extends FileStorageStream {

    OutputStream getOutputStream();

}
