package com.gitlab.devmix.warehouse.core.api.services.filestorage;

/**
 * @author Sergey Grachev
 */
public class FileStreamOpenException extends Exception {
    private static final long serialVersionUID = 960963987503363229L;

    public FileStreamOpenException() {
    }

    public FileStreamOpenException(final String message) {
        super(message);
    }

    public FileStreamOpenException(final Throwable cause) {
        super(cause);
    }
}
