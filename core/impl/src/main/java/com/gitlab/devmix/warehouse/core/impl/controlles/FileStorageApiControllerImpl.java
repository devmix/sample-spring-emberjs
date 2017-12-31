package com.gitlab.devmix.warehouse.core.impl.controlles;

import com.gitlab.devmix.warehouse.core.api.controllers.FileStorageApiController;
import com.gitlab.devmix.warehouse.core.api.services.FileStorageService;
import com.gitlab.devmix.warehouse.core.api.services.filestorage.FileStorageInputStream;
import com.gitlab.devmix.warehouse.core.api.services.filestorage.FileStorageOutputStream;
import com.gitlab.devmix.warehouse.core.api.services.filestorage.FileStreamOpenException;
import com.gitlab.devmix.warehouse.core.api.services.filestorage.FileStreamSelector;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

import static org.apache.commons.io.IOUtils.copy;

/**
 * @author Sergey Grachev
 */
@RestController
public class FileStorageApiControllerImpl implements FileStorageApiController {

    @Inject
    private FileStorageService storage;

    @Override
    public ResponseEntity<StreamingResponseBody> get(final HttpServletRequest request) {
        final FileStreamSelector selector = fileStreamSelectorOf(request).build();

        final FileStorageInputStream stream;
        try {
            stream = storage.openInputStream(selector);
        } catch (final FileStreamOpenException e) {
            return ResponseEntity.notFound().build();
        }

        return ResponseEntity.ok()
                .contentLength(stream.getMeta().getSize())
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .header("Content-Disposition", "attachment; filename=" + stream.getMeta().getName())
                .body(output -> {
                    try {
                        copy(stream.getInputStream(), output);
                    } finally {
                        stream.close();
                        output.close();
                    }
                });
    }

    @Override
    public ResponseEntity<?> post(@RequestParam("file") final MultipartFile file, final HttpServletRequest request) {
        final FileStreamSelector selector = fileStreamSelectorOf(request)
                .name(file.getName()).build();

        try (FileStorageOutputStream stream = storage.openOutputStream(selector)) {
            copy(file.getInputStream(), stream.getOutputStream());
        } catch (final FileStreamOpenException e) {
            return ResponseEntity.notFound().build();
        } catch (final IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }

        return ResponseEntity.ok().build();
    }

    private FileStreamSelector.FileStreamSelectorBuilder fileStreamSelectorOf(final HttpServletRequest request) {
        final String fullName = request.getRequestURI().substring("/api/core/storage/file/".length());
        final int fileIndex = fullName.lastIndexOf("/");
        return FileStreamSelector.builder()
                .folder(fullName.substring(0, fileIndex))
                .file(fullName.substring(fileIndex + 1));
    }
}
