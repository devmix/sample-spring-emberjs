package com.gitlab.devmix.warehouse.core.api.web.entity;

import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import javax.annotation.Nullable;

/**
 * @author Sergey Grachev
 */
@Data
public class RestQuery {

    private int page = 0;

    private int size = 10;

    @Nullable
    private String search;

    public Pageable asPageable() {
        return new PageRequest(page, size);
    }
}
