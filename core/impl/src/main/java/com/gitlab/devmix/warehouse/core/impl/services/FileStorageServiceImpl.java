package com.gitlab.devmix.warehouse.core.impl.services;

import com.gitlab.devmix.warehouse.core.api.services.FileStorageService;
import com.gitlab.devmix.warehouse.core.api.services.filestorage.FileStorageInputStream;
import com.gitlab.devmix.warehouse.core.api.services.filestorage.FileStorageOutputStream;
import com.gitlab.devmix.warehouse.core.api.services.filestorage.FileStreamMeta;
import com.gitlab.devmix.warehouse.core.api.services.filestorage.FileStreamOpenException;
import com.gitlab.devmix.warehouse.core.api.services.filestorage.FileStreamSelector;
import com.gitlab.devmix.warehouse.core.impl.services.filtestorage.FileStorageInputStreamImpl;
import com.gitlab.devmix.warehouse.core.impl.services.filtestorage.FileStorageOutputStreamImpl;
import com.gitlab.devmix.warehouse.core.impl.services.filtestorage.FileStreamMetaInternal;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

/**
 * @author Sergey Grachev
 */
@Service
public class FileStorageServiceImpl implements FileStorageService {

    @Override
    public void remove(final FileStreamSelector selector) {
        final Path actualPath = Paths.get(actualPathOf(selector.getFolder()), selector.getFile());
        try {
            Files.deleteIfExists(actualPath);
            Files.deleteIfExists(Paths.get(actualPath.toString() + ".meta"));
        } catch (final IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    @Override
    public void removeAll(final FileStreamSelector selector) {
        final Path actualPath = Paths.get(actualPathOf(selector.getFolder()));
        if (Files.exists(actualPath)) {
            try {
                FileUtils.deleteDirectory(actualPath.toFile());
            } catch (final IOException e) {
                throw new IllegalArgumentException(e);
            }
        }
    }

    @Override
    public FileStorageInputStream openInputStream(final FileStreamSelector selector) throws FileStreamOpenException {
        final Path actualPath = Paths.get(actualPathOf(selector.getFolder())).resolve(selector.getFile());
        if (!Files.exists(actualPath)) {
            throw new FileStreamOpenException(actualPath.toString());
        }

        final long size;
        try {
            size = Files.size(actualPath);
        } catch (final IOException e) {
            throw new FileStreamOpenException(e);
        }

        final FileInputStream stream;
        try {
            stream = new FileInputStream(actualPath.toFile());
        } catch (final FileNotFoundException e) {
            throw new FileStreamOpenException(e);
        }

        final FileStreamMetaInternal metaInternal = FileStreamMetaInternal
                .deserialize(actualPath.getParent(), selector.getFile());

        return new FileStorageInputStreamImpl(stream, FileStreamMeta.builder()
                .folder(selector.getFolder())
                .file(selector.getFile())
                .name(metaInternal.getName())
                .size(size)
                .build());
    }

    @Override
    public FileStorageOutputStream openOutputStream(final FileStreamSelector selector) throws FileStreamOpenException {
        final Path folder = Paths.get(actualPathOf(selector.getFolder()));
        if (!Files.exists(folder)) {
            try {
                Files.createDirectories(folder);
            } catch (final IOException e) {
                throw new FileStreamOpenException(e);
            }
        }

        final Path file = folder.resolve(selector.getFile());

        final FileOutputStream stream;
        try {
            stream = new FileOutputStream(file.toFile());
        } catch (final FileNotFoundException e) {
            throw new FileStreamOpenException(e);
        }

        final FileStreamMeta meta = FileStreamMeta.builder()
                .folder(selector.getFolder())
                .file(selector.getFile())
                .name(selector.getName())
                .build();

        return new FileStorageOutputStreamImpl(stream, file, meta);
    }

    private String actualPathOf(final String path) {
        final String actualPath = (path.startsWith("/") ? path.substring(1) : path).replaceAll("/", File.separator);
        return "files" + File.separator + actualPath;
    }
}
