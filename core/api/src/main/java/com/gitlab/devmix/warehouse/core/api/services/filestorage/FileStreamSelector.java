package com.gitlab.devmix.warehouse.core.api.services.filestorage;

import lombok.Builder;
import lombok.Value;

/**
 * @author Sergey Grachev
 */
@Value
@Builder
public class FileStreamSelector {

    private String folder;
    private String file;
    private String name;
}
