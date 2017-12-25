package com.gitlab.devmix.warehouse.core.api.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Sergey Grachev
 */
@RequestMapping("/api/core/storage/file")
public interface FileStorageController {

    @GetMapping("/**")
    ResponseEntity<StreamingResponseBody> get(HttpServletRequest request);

    @PostMapping("/**")
    ResponseEntity<?> post(MultipartFile file, HttpServletRequest request);
}
