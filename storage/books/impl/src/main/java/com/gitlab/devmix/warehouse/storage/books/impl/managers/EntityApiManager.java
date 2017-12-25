package com.gitlab.devmix.warehouse.storage.books.impl.managers;

import com.gitlab.devmix.warehouse.core.api.services.EntityRestRegistry;
import com.gitlab.devmix.warehouse.core.api.web.entity.Endpoint;
import com.gitlab.devmix.warehouse.storage.books.api.entitity.Author;
import com.gitlab.devmix.warehouse.storage.books.api.entitity.Book;
import com.gitlab.devmix.warehouse.storage.books.api.entitity.Genre;
import com.gitlab.devmix.warehouse.storage.books.api.entitity.Publisher;
import com.gitlab.devmix.warehouse.storage.books.api.projections.BooksBookList;
import com.gitlab.devmix.warehouse.storage.books.api.repository.AuthorRepository;
import com.gitlab.devmix.warehouse.storage.books.api.repository.BookRepository;
import com.gitlab.devmix.warehouse.storage.books.api.repository.GenreRepository;
import com.gitlab.devmix.warehouse.storage.books.api.repository.PublisherRepository;
import org.apache.commons.collections4.MultiValuedMap;
import org.apache.commons.collections4.multimap.HashSetValuedHashMap;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.gitlab.devmix.warehouse.core.api.web.entity.Endpoint.builder;
import static com.gitlab.devmix.warehouse.core.api.web.entity.Endpoint.create;
import static com.gitlab.devmix.warehouse.core.api.web.entity.Endpoint.delete;
import static com.gitlab.devmix.warehouse.core.api.web.entity.Endpoint.list;
import static com.gitlab.devmix.warehouse.core.api.web.entity.Endpoint.read;
import static com.gitlab.devmix.warehouse.core.api.web.entity.Endpoint.update;
import static com.gitlab.devmix.warehouse.core.api.web.entity.EntityUtils.getReferenceSet;
import static java.util.stream.Collectors.toMap;
import static org.apache.commons.collections4.CollectionUtils.isNotEmpty;
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
    private GenreRepository genreRepository;

    @Inject
    private PublisherRepository publisherRepository;

    @Inject
    private EntityRestRegistry registry;

    @Inject
    private EntityManager em;

    public void init() {
        registry.add(book());
        registry.add(author());
        registry.add(genre());
        registry.add(publisher());
    }

    private Endpoint book() {
        return builder("/books/book")
                // TODO SG very slow with lazy associations
                // TODO SG it's ~7 times slower than using a custom query via EntityManager
                .add(list(BooksBookList.class).relationship(BooksBookList.Author.class)
                        .run(query -> {
//                            return findBooks(query.getSearch(), query.asPageable());
                            return isNotBlank(query.getSearch())
                                    ? bookRepository.findPagedProjectedByTitleContainsIgnoreCaseAndDeletedFalse(query.getSearch(), query.asPageable())
                                    : bookRepository.findPagedProjectedByDeletedFalse(query.asPageable());
                        }).build())

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

    private Endpoint genre() {
        return builder("/books/genre")
                .add(list(Genre.class)
                        .run(query -> genreRepository.findPagedByDeletedFalse(query.asPageable())).build())

                .add(create(Genre.class)
                        .run(entity -> genreRepository.save(entity)).build())

                .add(read(Genre.class)
                        .run(id -> genreRepository.findByIdAndDeletedFalse(UUID.fromString(id))).build())

                .add(update(Genre.class)
                        .run((id, entity) -> genreRepository.save(entity)).build())

                .add(delete(Genre.class)
                        .run(id -> genreRepository.delete(UUID.fromString(id))).build())

                .build();
    }

    private Endpoint publisher() {
        return builder("/books/publisher")
                .add(list(Publisher.class)
                        .run(query -> publisherRepository.findPagedByDeletedFalse(query.asPageable())).build())

                .add(create(Publisher.class)
                        .run(entity -> publisherRepository.save(entity)).build())

                .add(read(Publisher.class)
                        .run(id -> publisherRepository.findByIdAndDeletedFalse(UUID.fromString(id))).build())

                .add(update(Publisher.class)
                        .run((id, entity) -> publisherRepository.save(entity)).build())

                .add(delete(Publisher.class)
                        .run(id -> publisherRepository.delete(UUID.fromString(id))).build())

                .build();
    }

    private Page<Book> findBooks(final Pageable pageable) {
        final List<Book> books = em.createQuery("select e from booksBook e where e.deleted = false", Book.class)
                .setFirstResult(pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        final Set<UUID> bookIds = books.stream().map(Book::getId).collect(Collectors.toSet());
        final List<Object[]> bookToAuthors = em.createQuery(
                "select e.id, a.id from booksBook e left join e.authors a where e.deleted = false and e.id in :ids")
                .setParameter("ids", bookIds)
                .getResultList();

        final MultiValuedMap<UUID, UUID> map = new HashSetValuedHashMap<>();
        for (final Object[] link : bookToAuthors) {
            final UUID authorId = (UUID) link[1];
            if (authorId != null) {
                map.put((UUID) link[0], authorId);
            }
        }

        final Map<UUID, Author> authors = em.createQuery(
                "select e from booksAuthor e where e.deleted = false and e.id in :ids", Author.class)
                .setParameter("ids", new HashSet<>(map.values()))
                .getResultList().stream().collect(toMap(Author::getId, Function.identity()));

        books.forEach(book -> {
            final Collection<UUID> bookAuthors = map.get(book.getId());
            if (isNotEmpty(bookAuthors)) {
                book.setAuthors(bookAuthors.stream().map(authors::get).collect(Collectors.toSet()));
                book.setGenres(null);
                book.setPublisher(null);
            }
        });

        return new PageImpl<>(books, pageable,
                em.createQuery("select count(e) from booksBook e where e.deleted = false", Long.class)
                        .getSingleResult());
    }
}
