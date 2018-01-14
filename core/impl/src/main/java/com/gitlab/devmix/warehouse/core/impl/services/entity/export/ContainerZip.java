package com.gitlab.devmix.warehouse.core.impl.services.entity.export;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gitlab.devmix.warehouse.core.api.services.entity.importexport.ExportOptions;
import com.gitlab.devmix.warehouse.core.api.services.entity.importexport.FormatType;
import lombok.Builder;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Map;
import java.util.zip.Deflater;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author Sergey Grachev
 */
@Builder
public class ContainerZip implements Container {

    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    private final FormatType format;
    private final OutputStream stream;

    public ContainerZip(final FormatType format, final OutputStream stream) {
        this.format = format;
        this.stream = new ZipOutputStream(stream);
        initOptions();
    }

    private void initOptions() {
        final ZipOutputStream zstream = (ZipOutputStream) stream;
        zstream.setLevel(Deflater.BEST_COMPRESSION);
        zstream.setComment(format.name());
    }

    @Override
    public void store(final String name, final Object id, final Map<String, Object> data) throws IOException {
        final ZipOutputStream zstream = (ZipOutputStream) this.stream;
        final ZipEntry entry = new ZipEntry(name + '/' + id);
        zstream.putNextEntry(entry);
        try {
            switch (format) {
                case JSON:
                    zstream.write(OBJECT_MAPPER.writeValueAsString(data).getBytes());
                    break;
                default:
                    throw new UnsupportedOperationException("Format type '" + format + "' is not supported");
            }
        } finally {
            zstream.closeEntry();
        }
    }

    @Override
    public void store(final ExportOptions options) throws IOException {
        final ZipOutputStream zstream = (ZipOutputStream) this.stream;
        final ZipEntry entry = new ZipEntry(".meta/options.json");
        zstream.putNextEntry(entry);
        try {
            zstream.write(OBJECT_MAPPER.writeValueAsString(options).getBytes());
        } finally {
            zstream.closeEntry();
        }
    }

    @Override
    public void close() throws IOException {
        stream.close();
    }
}
