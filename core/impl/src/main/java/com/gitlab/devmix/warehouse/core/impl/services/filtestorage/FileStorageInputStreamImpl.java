package com.gitlab.devmix.warehouse.core.impl.services.filtestorage;

import com.gitlab.devmix.warehouse.core.api.services.filestorage.FileStorageInputStream;
import com.gitlab.devmix.warehouse.core.api.services.filestorage.FileStorageOutputStream;
import com.gitlab.devmix.warehouse.core.api.services.filestorage.FileStreamMeta;
import lombok.Value;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @author Sergey Grachev
 */
@Value
public class FileStorageInputStreamImpl implements FileStorageInputStream {

    private final InputStream inputStream;
    private final FileStreamMeta meta;

    @Override
    public void close() {
        try {
            inputStream.close();
        } catch (final IOException e) {
            e.printStackTrace();
        }
    }
}
