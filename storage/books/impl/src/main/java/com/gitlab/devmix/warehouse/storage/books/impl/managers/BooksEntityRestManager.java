package com.gitlab.devmix.warehouse.storage.books.impl.managers;

import com.gitlab.devmix.warehouse.core.api.services.entity.EntityRestRegistry;
import com.gitlab.devmix.warehouse.core.api.web.entity.Endpoint;
import com.gitlab.devmix.warehouse.storage.books.api.entity.Author;
import com.gitlab.devmix.warehouse.storage.books.api.entity.Book;
import com.gitlab.devmix.warehouse.storage.books.api.entity.Genre;
import com.gitlab.devmix.warehouse.storage.books.api.entity.Publisher;
import com.gitlab.devmix.warehouse.storage.books.api.repository.AuthorRepository;
import com.gitlab.devmix.warehouse.storage.books.api.repository.BookRepository;
import com.gitlab.devmix.warehouse.storage.books.api.repository.GenreRepository;
import com.gitlab.devmix.warehouse.storage.books.api.repository.PublisherRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
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
import static com.gitlab.devmix.warehouse.core.api.web.entity.handlers.OperationHandlers.jpaRead;
import static com.gitlab.devmix.warehouse.core.api.web.entity.handlers.OperationHandlers.jpaReadList;
import static com.gitlab.devmix.warehouse.core.api.web.entity.jpa.JpaUtils.getReferenceSet;

/**
 * @author Sergey Grachev
 */
@Component
public class BooksEntityRestManager {

    @Inject
    private BookRepository bookRepository;

    @Inject
    private AuthorRepository authorRepository;

    @Inject
    private GenreRepository genreRepository;

    @Inject
    private PublisherRepository publisherRepository;

    @Inject
    private EntityRestRegistry registry;

    @Inject
    private EntityManager em;

    public void init() {
        registry.add(book());
        registry.add(dictionaryEndpoint("/books/author", Author.class, authorRepository));
        registry.add(dictionaryEndpoint("/books/genre", Genre.class, genreRepository));
        registry.add(dictionaryEndpoint("/books/publisher", Publisher.class, publisherRepository));
    }

    private Endpoint book() {
        return builder("/books/book")
                .add(list(Book.class).include(Author.class)
                        .projection(Book.PROJECTION_LIST)
                        .handler(jpaReadList(Book.class).searchPredicate(Book.SEARCH_LIST).build())
                        .build())

                .add(create(Book.class)
                        .handler(book -> updateBookRelationships(bookRepository.save(book))).build())

                .add(read(Book.class)
                        .include(Author.class, Genre.class, Publisher.class)
                        .handler(jpaRead(Book.class).id(UUID::fromString).build())
                        .build())

                .add(update(Book.class)
                        .handler((id, book) -> updateBookRelationships(bookRepository.save(book))).build())

                .add(delete(Book.class)
                        .handler(id -> bookRepository.recoverableDelete(UUID.fromString(id))).build())

                .build();
    }

    private Book updateBookRelationships(final Book book) {
        book.setAuthors(getReferenceSet(book.getAuthors(), Author.class, em));
        book.setGenres(getReferenceSet(book.getGenres(), Genre.class, em));
        if (book.getPublisher() != null) {
            book.setPublisher(em.getReference(Publisher.class, book.getPublisher().getId()));
        }
        return book;
    }

    private static <E> Endpoint dictionaryEndpoint(final String uri, final Class<E> e,
                                                   final PagingAndSortingRepository<E, UUID> r) {
        return builder(uri)
                .add(list(e).handler(jpaReadList(e).build()).build())
                .add(create(e).handler(r::save).build())
                .add(read(e).handler(jpaRead(e).id(UUID::fromString).build()).build())
                .add(update(e).handler((id, entity) -> r.save(entity)).build())
                .add(delete(e).handler(id -> r.delete(UUID.fromString(id))).build())
                .build();
    }
}
