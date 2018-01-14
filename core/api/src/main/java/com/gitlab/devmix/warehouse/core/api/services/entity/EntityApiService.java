package com.gitlab.devmix.warehouse.core.api.services.entity;

import com.gitlab.devmix.warehouse.core.api.web.entity.Request;
import com.gitlab.devmix.warehouse.core.api.web.entity.ResponseData;
import com.gitlab.devmix.warehouse.core.api.web.entity.operations.CreateOperation;
import com.gitlab.devmix.warehouse.core.api.web.entity.operations.DeleteOperation;
import com.gitlab.devmix.warehouse.core.api.web.entity.operations.ListOperation;
import com.gitlab.devmix.warehouse.core.api.web.entity.operations.ReadOperation;
import com.gitlab.devmix.warehouse.core.api.web.entity.operations.UpdateOperation;

/**
 * @author Sergey Grachev
 */
public interface EntityApiService {

    ResponseData execute(ListOperation operation, Request request);

    ResponseData execute(ReadOperation operation, String id);

    ResponseData execute(CreateOperation operation, Object entity);

    ResponseData execute(UpdateOperation operation, String id, Object entity);

    void execute(DeleteOperation operation, String id);
}
