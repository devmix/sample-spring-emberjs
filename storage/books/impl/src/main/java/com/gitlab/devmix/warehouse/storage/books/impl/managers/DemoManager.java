package com.gitlab.devmix.warehouse.storage.books.impl.managers;

import com.gitlab.devmix.warehouse.core.api.entity.AbstractRecoverableEntity;
import com.gitlab.devmix.warehouse.core.api.entity.security.User;
import com.gitlab.devmix.warehouse.core.api.entity.security.UserRole;
import com.gitlab.devmix.warehouse.core.api.repositories.UserRepository;
import com.gitlab.devmix.warehouse.core.api.services.FileStorageService;
import com.gitlab.devmix.warehouse.core.api.services.filestorage.FileStorageOutputStream;
import com.gitlab.devmix.warehouse.core.api.services.filestorage.FileStreamOpenException;
import com.gitlab.devmix.warehouse.core.api.services.filestorage.FileStreamSelector;
import com.gitlab.devmix.warehouse.storage.books.api.entity.Author;
import com.gitlab.devmix.warehouse.storage.books.api.entity.Book;
import com.gitlab.devmix.warehouse.storage.books.api.entity.Genre;
import com.gitlab.devmix.warehouse.storage.books.api.entity.Publisher;
import com.gitlab.devmix.warehouse.storage.books.api.repository.AuthorRepository;
import com.gitlab.devmix.warehouse.storage.books.api.repository.BookRepository;
import com.gitlab.devmix.warehouse.storage.books.api.repository.GenreRepository;
import com.gitlab.devmix.warehouse.storage.books.api.repository.PublisherRepository;
import com.gitlab.devmix.warehouse.storage.books.impl.listeners.StorageBooksStartupListener;
import lombok.Data;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Scope;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.io.IOException;
import java.io.InputStream;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.StreamSupport;

import static java.util.Arrays.asList;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

/**
 * @author Sergey Grachev
 */
@Component
@Scope("prototype")
public class DemoManager {
    private static final Logger LOGGER = LoggerFactory.getLogger(DemoManager.class);

    private static final String STORAGE_BOOKS_BOOK_COVER = "storage/books/book/cover/";

    private static final int REPEAT = 100;

    @Inject
    private BookRepository bookRepository;

    @Inject
    private AuthorRepository authorRepository;

    @Inject
    private GenreRepository genreRepository;

    @Inject
    private PublisherRepository publisherRepository;

    @Inject
    private FileStorageService storage;

    @Inject
    private UserRepository userRepository;

    @Inject
    private EntityManager em;

    @Inject
    private PasswordEncoder passwordEncoder;

    @Value("${demo.forceInit:false}")
    private boolean foreceDemoInit;

    private Map<Author, UUID> authors;
    private Map<Genre, UUID> genres;
    private Map<Publisher, UUID> publishers;

    @Transactional
    public void init() {
        if (bookRepository.count() > 0 && !foreceDemoInit) {
            return;
        }

        initUser();

        bookRepository.deleteAll();
        authorRepository.deleteAll();
        genreRepository.deleteAll();
        publisherRepository.deleteAll();
        storage.removeAll(FileStreamSelector.builder().folder(STORAGE_BOOKS_BOOK_COVER).build());

        for (int i = 0; i < REPEAT; i++) {
            final Demo demo = (Demo) new Yaml(new Constructor(Demo.class))
                    .load(StorageBooksStartupListener.class.getResourceAsStream("/demo-books.yml"));

            if (this.authors == null) {
                this.authors = StreamSupport.stream(authorRepository
                        .save(demo.books.stream()
                                .flatMap(book -> book.getAuthors().stream())
                                .collect(toSet())).spliterator(), false)
                        .collect(toMap(Function.identity(), Author::getId));
            }

            if (this.genres == null) {
                this.genres = StreamSupport.stream(genreRepository
                        .save(demo.books.stream()
                                .flatMap(book -> book.getGenres().stream())
                                .collect(toSet())).spliterator(), false)
                        .collect(toMap(Function.identity(), Genre::getId));
            }

            if (this.publishers == null) {
                this.publishers = StreamSupport.stream(publisherRepository
                        .save(demo.books.stream()
                                .map(Book::getPublisher)
                                .collect(toSet())).spliterator(), false)
                        .collect(toMap(Function.identity(), Publisher::getId));
            }

            demo.books.forEach(book -> {
                book.setGenres(book.getGenres().stream()
                        .map(genre -> genres.keySet().stream()
                                .filter(e -> Objects.equals(genre.getName(), e.getName()))
                                .findFirst().orElse(null))
                        .filter(Objects::nonNull)
                        .map(Genre::getId)
                        .map(id -> em.getReference(Genre.class, id))
                        .collect(toSet()));

                book.setAuthors(book.getAuthors().stream()
                        .map(author -> authors.keySet().stream()
                                .filter(e -> Objects.equals(author.getFirstName(), e.getFirstName())
                                        && Objects.equals(author.getMiddleName(), e.getMiddleName())
                                        && Objects.equals(author.getLastName(), e.getLastName()))
                                .findFirst().orElse(null))
                        .filter(Objects::nonNull)
                        .map(AbstractRecoverableEntity::getId)
                        .map(id -> em.getReference(Author.class, id))
                        .collect(toSet()));

                book.setPublisher(em.getReference(Publisher.class, publishers.keySet().stream()
                        .filter(e -> Objects.equals(e.getName(), book.getPublisher().getName()))
                        .map(Publisher::getId)
                        .findFirst().orElse(null)));
            });

            bookRepository
                    .save(demo.books)
                    .forEach(book -> {
                        final FileStreamSelector selector = FileStreamSelector.builder()
                                .folder(STORAGE_BOOKS_BOOK_COVER).file(book.getId().toString())
                                .name(book.getIsnb13() + ".jpg").build();
                        try (FileStorageOutputStream stream = storage.openOutputStream(selector)) {
                            final String name = "/demo/books/covers/" + selector.getName();
                            try (InputStream resource = this.getClass().getResourceAsStream(name)) {
                                IOUtils.copy(resource, stream.getOutputStream());
                            }
                        } catch (final IOException | FileStreamOpenException e) {
                            LOGGER.error("Fail to upload file", e);
                        }
                    });
        }
    }

    private void initUser() {
        userRepository.deleteAll();

        User user = new User();
        user.setIdentity("admin");
        user.setPassword(passwordEncoder.encode("admin"));
        user = userRepository.save(user);

        user.setRoles(new HashSet<>(asList(createRole("USER", user), createRole("ADMIN", user))));
        userRepository.save(user);
    }

    private static UserRole createRole(final String type, final User user) {
        final UserRole role = new UserRole();
        role.setType(type);
        role.setUser(user);
        return role;
    }

    @SuppressWarnings("WeakerAccess")
    @Data
    public static final class Demo {
        private List<Book> books;
    }
}
