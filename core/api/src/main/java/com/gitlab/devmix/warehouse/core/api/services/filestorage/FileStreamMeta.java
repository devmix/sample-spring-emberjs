package com.gitlab.devmix.warehouse.core.api.services.filestorage;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Value;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * @author Sergey Grachev
 */
@Value
@Builder
public class FileStreamMeta {

    private String folder;
    private String file;
    private String name;
    private long size;

}
