package com.gitlab.devmix.warehouse.core.impl.services.filtestorage;

import com.gitlab.devmix.warehouse.core.api.services.filestorage.FileStorageOutputStream;
import com.gitlab.devmix.warehouse.core.api.services.filestorage.FileStreamMeta;
import lombok.Value;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Path;

/**
 * @author Sergey Grachev
 */
@Value
public class FileStorageOutputStreamImpl implements FileStorageOutputStream {

    private final FileOutputStream outputStream;
    private final Path file;
    private final FileStreamMeta meta;

    @Override
    public void close() {
        try {
            outputStream.flush();
        } catch (final IOException e) {
            e.printStackTrace();
        }

        try {
            outputStream.close();
        } catch (final IOException e) {
            e.printStackTrace();
        }

        FileStreamMetaInternal.serialize(meta, file.getParent(), meta.getFile());
    }
}
