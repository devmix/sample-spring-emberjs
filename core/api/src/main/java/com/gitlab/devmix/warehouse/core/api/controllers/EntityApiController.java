package com.gitlab.devmix.warehouse.core.api.controllers;

import com.gitlab.devmix.warehouse.core.api.web.entity.RestQuery;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * @author Sergey Grachev
 */
@RequestMapping(EntityApiController.API_CORE_ENTITY)
public interface EntityApiController {

    String API_CORE_ENTITY = "/api/core/entity";

    @GetMapping("/**")
    ResponseEntity<?> get(Map<String, Object> query, HttpServletRequest request);

    @PostMapping("/**")
    ResponseEntity<?> post(String json, HttpServletRequest request);

    @PutMapping("/**")
    ResponseEntity<?> put(String json, HttpServletRequest request);

    @DeleteMapping("/**")
    ResponseEntity<?> delete(HttpServletRequest request);
}
