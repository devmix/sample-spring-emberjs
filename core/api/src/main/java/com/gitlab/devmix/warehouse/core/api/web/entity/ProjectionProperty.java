package com.gitlab.devmix.warehouse.core.api.web.entity;

/**
 * @author Sergey Grachev
 */
public interface ProjectionProperty {

    boolean isAny();

    ProjectionProperty find(String property);
}
