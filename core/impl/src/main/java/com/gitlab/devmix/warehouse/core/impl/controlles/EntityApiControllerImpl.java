package com.gitlab.devmix.warehouse.core.impl.controlles;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gitlab.devmix.warehouse.core.api.controllers.EntityApiController;
import com.gitlab.devmix.warehouse.core.api.services.EntityRestRegistry;
import com.gitlab.devmix.warehouse.core.api.web.entity.Endpoint;
import com.gitlab.devmix.warehouse.core.api.web.entity.Payload;
import com.gitlab.devmix.warehouse.core.api.web.entity.Request;
import com.gitlab.devmix.warehouse.core.api.web.entity.RequestParameters;
import com.gitlab.devmix.warehouse.core.api.web.entity.Response;
import com.gitlab.devmix.warehouse.core.api.web.entity.operations.CreateOperation;
import com.gitlab.devmix.warehouse.core.api.web.entity.operations.DeleteOperation;
import com.gitlab.devmix.warehouse.core.api.web.entity.operations.ListOperation;
import com.gitlab.devmix.warehouse.core.api.web.entity.operations.ReadOperation;
import com.gitlab.devmix.warehouse.core.api.web.entity.operations.UpdateOperation;
import com.gitlab.devmix.warehouse.core.api.web.entity.utils.RequestParametersUtils;
import org.apache.commons.lang3.time.StopWatch;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.gitlab.devmix.warehouse.core.api.web.entity.Operation.Type.CREATE;
import static com.gitlab.devmix.warehouse.core.api.web.entity.Operation.Type.DELETE;
import static com.gitlab.devmix.warehouse.core.api.web.entity.Operation.Type.LIST;
import static com.gitlab.devmix.warehouse.core.api.web.entity.Operation.Type.READ;
import static com.gitlab.devmix.warehouse.core.api.web.entity.Operation.Type.UPDATE;

/**
 * @author Sergey Grachev
 */
@RestController
public class EntityApiControllerImpl implements EntityApiController {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();
    private static final Logger LOGGER = LoggerFactory.getLogger(EntityApiControllerImpl.class);
    private static final Pattern PATTERN_ENTITY_ID = Pattern.compile("/(.*)(?<!/)$");

    @Inject
    private EntityRestRegistry apiRegistry;

    @SuppressWarnings("unchecked")
    @Override
    public ResponseEntity<?> get(@RequestParam final Map<String, Object> query, final HttpServletRequest request) {
        LOGGER.trace("get: request");

        final StopWatch stopWatch = StopWatch.createStarted();

        final String requestUri = parseRequestUri(request);
        final Endpoint endpoint = apiRegistry.findEndpoint(requestUri);
        if (endpoint == null) {
            return ResponseEntity.notFound().build();
        }

        final String operationUri = requestUri.substring(endpoint.getRootUri().length());
        final Matcher matcher = PATTERN_ENTITY_ID.matcher(operationUri);
        if (matcher.matches()) {
            // READ
            final String id = matcher.group(1);
            final ReadOperation operation = (ReadOperation) endpoint.findOperation(READ);
            if (operation != null) {
                stopWatch.split();
                LOGGER.trace("get: operation found {}ms", stopWatch.getSplitTime());

                final Object handle = operation.getRun().handle(id);

                stopWatch.split();
                LOGGER.trace("get: handler finished {}ms", stopWatch.getSplitTime());

                final Response single = Response.of(operation.getEntityClass())
                        .addIfNotNull(handle)
                        .include(operation.getRelationships())
                        .single();

                stopWatch.split();
                LOGGER.trace("get: single created {}ms", stopWatch.getSplitTime());

                final ResponseEntity<Response> response = ResponseEntity.ok(single);

                stopWatch.split();
                LOGGER.trace("get: response created {}ms", stopWatch.getSplitTime());

                LOGGER.trace("get: total time {}ms", stopWatch.getTime());

                return response;
            }
        } else {
            // LIST
            final ListOperation operation = (ListOperation) endpoint.findOperation(LIST);
            if (operation != null) {
                stopWatch.split();
                LOGGER.trace("get: operation found {}ms", stopWatch.getSplitTime());

                final Class parametersClass = operation.getParametersClass() == null
                        ? RequestParameters.class : operation.getParametersClass();
                final RequestParameters requestParameters = (RequestParameters) OBJECT_MAPPER
                        .convertValue(RequestParametersUtils.queryToMap(query), parametersClass);
                final Page page = operation.getRun().handle(operation, requestParameters);

                stopWatch.split();
                LOGGER.trace("get: handler finished {}ms", stopWatch.getSplitTime());

                final Response list = Response.of(operation.getEntityClass())
                        .include(operation.getRelationships())
                        .projection(operation.getProjection())
                        .add(page)
                        .list();

                stopWatch.split();
                LOGGER.trace("get: list created {}ms", stopWatch.getSplitTime());

                final ResponseEntity<?> response = ResponseEntity.ok(list);

                stopWatch.split();
                LOGGER.trace("get: response created {}ms", stopWatch.getSplitTime());
                LOGGER.trace("get: total time {}ms", stopWatch.getTime());

                return response;
            }
        }

        return ResponseEntity.notFound().build();
    }

    @SuppressWarnings("unchecked")
    @Override
    public ResponseEntity<?> post(@RequestBody final String json, final HttpServletRequest request) {
        final String requestUri = parseRequestUri(request);
        final Endpoint endpoint = apiRegistry.findEndpoint(requestUri);
        if (endpoint == null) {
            return ResponseEntity.notFound().build();
        }

        final CreateOperation operation = (CreateOperation) endpoint.findOperation(CREATE);
        if (operation == null) {
            return ResponseEntity.notFound().build();
        }

        final Payload payload;
        try {
            payload = OBJECT_MAPPER.readValue(json, Payload.class);
        } catch (final IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

        final Object entity = Request.of(operation.getEntityClass()).data(payload).build().getEntity();
        final Object newEntity = operation.getRun().handle(entity);

        return ResponseEntity.ok(Response.of(operation.getEntityClass()).add(newEntity).single());
    }

    @SuppressWarnings("unchecked")
    @Override
    public ResponseEntity<?> put(@RequestBody final String json, final HttpServletRequest request) {
        final String requestUri = parseRequestUri(request);
        final Endpoint endpoint = apiRegistry.findEndpoint(requestUri);
        if (endpoint == null) {
            return ResponseEntity.notFound().build();
        }

        final String operationUri = requestUri.substring(endpoint.getRootUri().length());
        final Matcher matcher = PATTERN_ENTITY_ID.matcher(operationUri);
        if (!matcher.matches()) {
            return ResponseEntity.notFound().build();
        }

        final String id = matcher.group(1);

        final UpdateOperation operation = (UpdateOperation) endpoint.findOperation(UPDATE);
        if (operation == null) {
            return ResponseEntity.notFound().build();
        }

        final Payload payload;
        try {
            payload = OBJECT_MAPPER.readValue(json, Payload.class);
        } catch (final IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }

        final Object entity = Request.of(operation.getEntityClass()).id(id).data(payload).build().getEntity();
        final Object newEntity = operation.getRun().handle(id, entity);

        return ResponseEntity.ok(Response.of(operation.getEntityClass()).add(newEntity).single());
    }

    @SuppressWarnings("unchecked")
    @Override
    public ResponseEntity<?> delete(final HttpServletRequest request) {
        final String requestUri = parseRequestUri(request);
        final Endpoint endpoint = apiRegistry.findEndpoint(requestUri);
        if (endpoint == null) {
            return ResponseEntity.notFound().build();
        }

        final String operationUri = requestUri.substring(endpoint.getRootUri().length());
        final Matcher matcher = PATTERN_ENTITY_ID.matcher(operationUri);
        if (!matcher.matches()) {
            return ResponseEntity.notFound().build();
        }

        final String id = matcher.group(1);

        final DeleteOperation operation = (DeleteOperation) endpoint.findOperation(DELETE);
        if (operation == null) {
            return ResponseEntity.notFound().build();
        }

        operation.getRun().handle(id);

        return ResponseEntity.ok(Response.delete(operation.getEntityClass()));
    }

    private String parseRequestUri(final HttpServletRequest request) {
        return request.getRequestURI().substring(EntityApiController.API_CORE_ENTITY.length());
    }
}
