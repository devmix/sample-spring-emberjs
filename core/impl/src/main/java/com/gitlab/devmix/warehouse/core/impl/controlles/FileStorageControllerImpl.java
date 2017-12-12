package com.gitlab.devmix.warehouse.core.impl.controlles;

import com.gitlab.devmix.warehouse.core.api.controllers.FileStorageController;
import com.gitlab.devmix.warehouse.core.api.entity.FileEntity;
import com.gitlab.devmix.warehouse.core.api.services.FileStorageService;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Sergey Grachev
 */
@RestController
public class FileStorageControllerImpl implements FileStorageController {

    @Inject
    private FileStorageService storage;

    @Override
    public ResponseEntity get(final HttpServletRequest request) {
        final String path = request.getRequestURI().substring("/api/core/storage/file/get/".length());
        final FileEntity file = storage.loadFile(path);
        if (file == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok()
                .contentLength(file.getContent().length)
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header("Content-Disposition", "attachment; filename=" + file.getFileName())
                .body(file.getContent());
    }
}
