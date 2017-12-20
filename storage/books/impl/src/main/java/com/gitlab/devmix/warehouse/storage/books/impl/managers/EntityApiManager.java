package com.gitlab.devmix.warehouse.storage.books.impl.managers;

import com.gitlab.devmix.warehouse.core.api.services.EntityRestRegistry;
import com.gitlab.devmix.warehouse.core.api.web.entity.Endpoint;
import com.gitlab.devmix.warehouse.storage.books.api.entitity.Author;
import com.gitlab.devmix.warehouse.storage.books.api.entitity.Book;
import com.gitlab.devmix.warehouse.storage.books.api.projections.BooksBookList;
import com.gitlab.devmix.warehouse.storage.books.api.repository.AuthorRepository;
import com.gitlab.devmix.warehouse.storage.books.api.repository.BookRepository;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.UUID;

import static com.gitlab.devmix.warehouse.core.api.web.entity.Endpoint.builder;
import static com.gitlab.devmix.warehouse.core.api.web.entity.Endpoint.create;
import static com.gitlab.devmix.warehouse.core.api.web.entity.Endpoint.delete;
import static com.gitlab.devmix.warehouse.core.api.web.entity.Endpoint.list;
import static com.gitlab.devmix.warehouse.core.api.web.entity.Endpoint.read;
import static com.gitlab.devmix.warehouse.core.api.web.entity.Endpoint.update;
import static com.gitlab.devmix.warehouse.core.api.web.entity.EntityUtils.getReferenceList;
import static org.apache.commons.lang3.StringUtils.isNotBlank;

/**
 * @author Sergey Grachev
 */
@Component
public class EntityApiManager {

    @Inject
    private BookRepository bookRepository;

    @Inject
    private AuthorRepository authorRepository;

    @Inject
    private EntityRestRegistry registry;

    @Inject
    private EntityManager em;

    public void init() {
        registry.add(book());
        registry.add(author());
    }

    private Endpoint book() {
        return builder("/books/book")
                .add(list(BooksBookList.class).relationship(BooksBookList.Author.class)
                        .run(query -> isNotBlank(query.getSearch())
                                ? bookRepository.findPagedProjectedByTitleContainsIgnoreCaseAndDeletedFalse(query.getSearch(), query.asPageable())
                                : bookRepository.findPagedProjectedByDeletedFalse(query.asPageable())).build())

                .add(create(Book.class)
                        .run(book -> {
                            book.setAuthors(getReferenceList(book.getAuthors(), Author.class, em));
                            return bookRepository.save(book);
                        }).build())

                .add(read(Book.class)
                        .relationship(Author.class)
                        .run(id -> bookRepository.findByIdAndDeletedFalse(UUID.fromString(id))).build())

                .add(update(Book.class)
                        .run((id, book) -> {
                            book.setAuthors(getReferenceList(book.getAuthors(), Author.class, em));
                            return bookRepository.save(book);
                        }).build())

                .add(delete(Book.class)
                        .run(id -> bookRepository.recoverableDelete(UUID.fromString(id))).build())

                .build();
    }

    private Endpoint author() {
        return builder("/books/author")
                .add(list(Author.class)
                        .run(query -> authorRepository.findPagedByDeletedFalse(query.asPageable())).build())

                .add(create(Author.class)
                        .run(entity -> authorRepository.save(entity)).build())

                .add(read(Author.class)
                        .run(id -> authorRepository.findByIdAndDeletedFalse(UUID.fromString(id))).build())

                .add(update(Author.class)
                        .run((id, entity) -> authorRepository.save(entity)).build())

                .add(delete(Author.class)
                        .run(id -> authorRepository.delete(UUID.fromString(id))).build())

                .build();
    }

}
