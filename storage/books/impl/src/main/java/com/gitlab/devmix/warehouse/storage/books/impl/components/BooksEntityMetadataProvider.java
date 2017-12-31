package com.gitlab.devmix.warehouse.storage.books.impl.components;

import com.gitlab.devmix.warehouse.core.api.web.entity.metadata.EntityMetadataProvider;
import com.gitlab.devmix.warehouse.storage.books.api.entity.Author;
import org.springframework.stereotype.Component;

import java.util.List;

import static java.util.Collections.singletonList;

/**
 * @author Sergey Grachev
 */
@Component
public class BooksEntityMetadataProvider implements EntityMetadataProvider {

    @Override
    public List<Class<?>> listClassBaseForScan() {
        return singletonList(Author.class);
    }
}
