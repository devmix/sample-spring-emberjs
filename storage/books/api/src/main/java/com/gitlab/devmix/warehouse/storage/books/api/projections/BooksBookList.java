package com.gitlab.devmix.warehouse.storage.books.api.projections;

import com.gitlab.devmix.warehouse.core.api.web.entity.Metadata;
import com.gitlab.devmix.warehouse.storage.books.api.entitity.Book;

import java.util.List;
import java.util.UUID;

/**
 * @author Sergey Grachev
 */
@Metadata.Name(Book.ENTITY)
public interface BooksBookList {

    @Metadata.Id
    UUID getId();

    String getTitle();

    @Metadata.RelationshipMany
    List<Author> getAuthors();

    @Metadata.Name(com.gitlab.devmix.warehouse.storage.books.api.entitity.Author.ENTITY)
    interface Author {

        @Metadata.Id
        UUID getId();

        String getFirstName();

        String getMiddleName();

        String getLastName();
    }
}
