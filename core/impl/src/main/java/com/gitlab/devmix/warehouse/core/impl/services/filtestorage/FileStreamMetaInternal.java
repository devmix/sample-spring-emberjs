package com.gitlab.devmix.warehouse.core.impl.services.filtestorage;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gitlab.devmix.warehouse.core.api.services.filestorage.FileStreamMeta;
import lombok.Data;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author Sergey Grachev
 */
@Data
public class FileStreamMetaInternal {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private String name;

    public void serialize(final Path folder, final String file) {
        try {
            final Path metaFile = folder.resolve(file + ".meta");
            final byte[] bytes = OBJECT_MAPPER.writeValueAsBytes(this);
            Files.write(metaFile, bytes);
        } catch (final IOException e) {
            throw new RuntimeException(e); // TODO SG correct exception
        }
    }

    public static FileStreamMetaInternal deserialize(final Path folder, final String file) {
        try {
            final Path metaFile = folder.resolve(file + ".meta");
            final byte[] bytes = Files.readAllBytes(metaFile);
            return OBJECT_MAPPER.readValue(bytes, FileStreamMetaInternal.class);
        } catch (final IOException e) {
            throw new RuntimeException(e); // TODO SG correct exception
        }
    }

    public static void serialize(final FileStreamMeta meta, final Path parent, final String file) {
        final FileStreamMetaInternal metaInternal = new FileStreamMetaInternal();
        metaInternal.setName(meta.getName());
        metaInternal.serialize(parent, file);
    }
}
