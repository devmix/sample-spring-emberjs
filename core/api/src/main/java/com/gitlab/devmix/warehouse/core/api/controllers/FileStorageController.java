package com.gitlab.devmix.warehouse.core.api.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Sergey Grachev
 */
@RequestMapping("/api/core/storage/file")
public interface FileStorageController {

    @GetMapping("/get/**")
    ResponseEntity get(HttpServletRequest request);
}
