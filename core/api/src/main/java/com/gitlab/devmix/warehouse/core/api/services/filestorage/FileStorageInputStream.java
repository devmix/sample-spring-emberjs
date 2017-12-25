package com.gitlab.devmix.warehouse.core.api.services.filestorage;

import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Sergey Grachev
 */
public interface FileStorageInputStream extends FileStorageStream{

    InputStream getInputStream();

}
