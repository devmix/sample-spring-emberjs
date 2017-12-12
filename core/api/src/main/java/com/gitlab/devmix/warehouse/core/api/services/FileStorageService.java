package com.gitlab.devmix.warehouse.core.api.services;

import com.gitlab.devmix.warehouse.core.api.entity.FileEntity;

import javax.annotation.Nullable;
import java.io.InputStream;

/**
 * @author Sergey Grachev
 */
public interface FileStorageService {

    void saveStream(FileEntity file, InputStream stream);

    @Nullable
    FileEntity loadFile(String path);

    void removeAll(String pathPrefix);
}
