package com.gitlab.devmix.warehouse.storage.books.impl.managers;

import com.gitlab.devmix.warehouse.core.api.services.EntityRestRegistry;
import com.gitlab.devmix.warehouse.core.api.web.entity.Endpoint;
import com.gitlab.devmix.warehouse.storage.books.api.entitity.Author;
import com.gitlab.devmix.warehouse.storage.books.api.entitity.Book;
import com.gitlab.devmix.warehouse.storage.books.api.entitity.Genre;
import com.gitlab.devmix.warehouse.storage.books.api.entitity.Publisher;
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
import static com.gitlab.devmix.warehouse.core.api.web.entity.EntityUtils.getReferenceSet;
import static com.gitlab.devmix.warehouse.storage.books.impl.managers.EntityQueryUtils.listOfEntities;

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
                .add(list(Book.class).relationship(Author.class)
                        .projection(
                                "id", "title", "description",
                                "authors.id", "authors.firstName", "authors.middleName", "authors.lastName",
                                "publishers.id", "publishers.name")
                        .run((op, query) -> listOfEntities(Book.class, em, op, query, (parameters, cb, e) ->
                                cb.like(cb.lower(e.get("title")), "%" + parameters.getSearch().toLowerCase() + "%"))).build())

                .add(create(Book.class)
                        .run(book -> updateBookRelationships(bookRepository.save(book))).build())

                .add(read(Book.class)
                        .relationship(Author.class)
                        .relationship(Genre.class)
                        .relationship(Publisher.class)
                        .run(id -> bookRepository.findByIdAndDeletedFalse(UUID.fromString(id))).build())

                .add(update(Book.class)
                        .run((id, book) -> updateBookRelationships(bookRepository.save(book))).build())

                .add(delete(Book.class)
                        .run(id -> bookRepository.recoverableDelete(UUID.fromString(id))).build())

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
                .add(list(e).run((operation, query) -> r.findAll(query.asPageable())).build())
                .add(create(e).run(r::save).build())
                .add(read(e).run(id -> r.findOne(UUID.fromString(id))).build())
                .add(update(e).run((id, entity) -> r.save(entity)).build())
                .add(delete(e).run(id -> r.delete(UUID.fromString(id))).build())
                .build();
    }
}
