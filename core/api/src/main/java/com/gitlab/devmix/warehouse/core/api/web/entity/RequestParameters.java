package com.gitlab.devmix.warehouse.core.api.web.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import javax.annotation.Nullable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.apache.commons.collections4.MapUtils.isNotEmpty;

/**
 * @author Sergey Grachev
 */
@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class RequestParameters {

    private int page;

    private int pageSize = 10;

    @Nullable
    private String search;

    private Map<String, String> sort;

    public Pageable asPageable() {
        final Sort pagetSort;
        if (isNotEmpty(sort)) {
            final List<Sort.Order> orders = new ArrayList<>(sort.size());
            for (final Map.Entry<String, String> e : sort.entrySet()) {
                orders.add(new Sort.Order(Sort.Direction.fromString(e.getValue()), e.getKey()));
            }
            pagetSort = new Sort(orders);
        } else {
            pagetSort = null;
        }
        return new PageRequest(page, pageSize <= 0 ? 20 : pageSize, pagetSort);
    }

}
