package com.gitlab.devmix.warehouse.core.impl.services;

import com.gitlab.devmix.warehouse.core.api.services.EntityApiService;
import com.gitlab.devmix.warehouse.core.api.web.entity.Request;
import com.gitlab.devmix.warehouse.core.api.web.entity.ResponseData;
import com.gitlab.devmix.warehouse.core.api.web.entity.operations.CreateOperation;
import com.gitlab.devmix.warehouse.core.api.web.entity.operations.DeleteOperation;
import com.gitlab.devmix.warehouse.core.api.web.entity.operations.ListOperation;
import com.gitlab.devmix.warehouse.core.api.web.entity.operations.ReadOperation;
import com.gitlab.devmix.warehouse.core.api.web.entity.operations.UpdateOperation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;

import static com.gitlab.devmix.warehouse.core.api.web.entity.ResponseData.of;

/**
 * @author Sergey Grachev
 */
@Service
public class EntityApiServiceImpl implements EntityApiService {

    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    @Override
    public ResponseData execute(final ListOperation operation, final Request request) {
        final Page page = operation.getHandler() != null
                ? operation.getHandler().handle(operation, request)
                : new PageImpl(Collections.emptyList(), request.asPageable(), 0);
        return of(operation.getEntityClass())
                .include(operation.getIncludes())
                .projection(operation.getProjection())
                .add(page)
                .list();
    }

    @Transactional(readOnly = true)
    @SuppressWarnings("unchecked")
    @Override
    public ResponseData execute(final ReadOperation operation, final String id) {
        final Object handle = operation.getHandler().handle(operation, id);
        return of(operation.getEntityClass())
                .addIfNotNull(handle)
                .include(operation.getIncludes())
                .single();
    }

    @Transactional
    @SuppressWarnings("unchecked")
    @Override
    public ResponseData execute(final CreateOperation operation, final Object entity) {
        final Object newEntity = operation.getHandler().handle(entity);
        return of(operation.getEntityClass()).add(newEntity).single();
    }

    @Transactional
    @SuppressWarnings("unchecked")
    @Override
    public ResponseData execute(final UpdateOperation operation, final String id, final Object entity) {
        final Object newEntity = operation.getHandler().handle(id, entity);
        return of(operation.getEntityClass()).add(newEntity).single();
    }

    @Transactional
    @Override
    public void execute(final DeleteOperation operation, final String id) {
        operation.getHandler().handle(id);
    }
}
