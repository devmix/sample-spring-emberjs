package com.gitlab.devmix.warehouse.core.api.services.entity.importexport;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Data;

import java.io.IOException;
import java.util.Set;

/**
 * @author Sergey Grachev
 */
@Data
public class ExportOptions {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private ContainerType container;
    private FormatType format;
    private Set<Entity> entities;
    private int offset;
    private int limit;
    private Set<String> projection;

    public String serialize() {
        try {
            return OBJECT_MAPPER.writeValueAsString(this);
        } catch (final JsonProcessingException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public static ExportOptions deserialize(final String options) {
        try {
            return OBJECT_MAPPER.readValue(options, ExportOptions.class);
        } catch (final IOException e) {
            throw new IllegalArgumentException(e);
        }
    }

    public enum ContainerType {
        ZIP
    }

}
