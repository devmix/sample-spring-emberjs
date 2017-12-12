package com.gitlab.devmix.warehouse.storage.books.impl.managers;

import com.gitlab.devmix.warehouse.core.api.entity.FileEntity;
import com.gitlab.devmix.warehouse.core.api.services.FileStorageService;
import com.gitlab.devmix.warehouse.storage.books.api.entitity.Author;
import com.gitlab.devmix.warehouse.storage.books.api.entitity.Book;
import com.gitlab.devmix.warehouse.storage.books.api.repository.AuthorRepository;
import com.gitlab.devmix.warehouse.storage.books.api.repository.BookRepository;
import com.gitlab.devmix.warehouse.storage.books.impl.listeners.StorageBooksStartupListener;
import lombok.Data;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.Constructor;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Function;
import java.util.stream.StreamSupport;

import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;
import static java.util.stream.Collectors.toSet;

/**
 * @author Sergey Grachev
 */
@Component
@Scope("prototype")
public class DemoManager {

    private static final String STORAGE_BOOKS_BOOK_COVER = "storage/books/book/cover/";

    private static final int REPEAT = 100;

    @Inject
    private BookRepository bookRepository;

    @Inject
    private AuthorRepository authorRepository;

    @Inject
    private FileStorageService storage;

    @Inject
    private EntityManager em;

    private Map<Author, UUID> authors;

    @Transactional
    public void init() {
        if (bookRepository.count() > 0) {
            return;
        }

        bookRepository.deleteAll();
        authorRepository.deleteAll();
        storage.removeAll(STORAGE_BOOKS_BOOK_COVER);

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

            demo.books.forEach(book -> {
                book.setAuthors(book.getAuthors().stream()
                        .map(authors::get)
                        .filter(Objects::nonNull)
                        .map(id -> em.getReference(Author.class, id))
                        .collect(toList()));
            });

            bookRepository
                    .save(demo.books)
                    .forEach(book -> {
                        final FileEntity file = new FileEntity();
                        file.setId(STORAGE_BOOKS_BOOK_COVER + book.getId().toString());
                        file.setFileName(book.getIsnb13() + ".jpg");
                        storage.saveStream(file,
                                this.getClass().getResourceAsStream("/demo/books/covers/" + file.getFileName()));
                    });
        }
    }

    @SuppressWarnings("WeakerAccess")
    @Data
    public static final class Demo {
        private List<Book> books;
    }
}
