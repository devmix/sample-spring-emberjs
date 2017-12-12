package com.gitlab.devmix.warehouse.core.impl.services;

import com.gitlab.devmix.warehouse.core.api.entity.FileEntity;
import com.gitlab.devmix.warehouse.core.api.services.FileStorageService;
import com.gitlab.devmix.warehouse.core.impl.repositories.FileEntityRepository;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nullable;
import javax.inject.Inject;
import java.io.IOException;
import java.io.InputStream;

/**
 * @author Sergey Grachev
 */
@Service
public class FileStorageServiceImpl implements FileStorageService {

    @Inject
    private FileEntityRepository repository;

    @Transactional
    @Override
    public void saveStream(final FileEntity file, final InputStream stream) {
        final byte[] content;
        try {
            content = IOUtils.toByteArray(stream);
        } catch (final IOException e) {
            throw new IllegalArgumentException(e);
        }
        file.setContent(content);
        repository.save(file);
    }

    @Nullable
    @Override
    public FileEntity loadFile(final String path) {
        return repository.findOne(path);
    }

    @Transactional
    @Override
    public void removeAll(final String pathPrefix) {
        repository.deleteAllByIdStartingWith(pathPrefix);
    }
}
