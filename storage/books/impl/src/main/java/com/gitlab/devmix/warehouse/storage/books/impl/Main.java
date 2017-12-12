package com.gitlab.devmix.warehouse.storage.books.impl;

import com.gitlab.devmix.warehouse.core.api.web.entity.Metadata;
import com.gitlab.devmix.warehouse.storage.books.api.projections.BooksBookList;
import lombok.Value;

import java.util.UUID;

/**
 * @author Sergey Grachev
 */
public class Main {
    public static void main(final String[] args) {
//        query()
//                .fetch((ctx) -> {
//                    ctx.store(Book.class, db.select(Book.class));
//                })
//                .fetch((ctx) -> {
//                    ctx.store(Author.class, Book.class, db.select(Author.class).whereIn("book", ctx.idsOf(Book.class)));
//                })
//                .fetch((ctx) -> {
//                    ctx.store(Genre.class, Author.class, db.select(Genre.class).whereIn("author", ctx.idsOf(Author.class)));
//                })
//                .collect(Book.class);
//        RestResponse.of(Book.class)
//                .include(Author.class)
//                .add(emptyList())
//                .projection()
//                .build();

        System.out.println(Metadata.of(BooksBookList.class));
    }

    /**
     * book -> author -> genre
     * <p>
     * - 1, book1, 1, author1, 1, genre1
     * - 1, book1, 1, author1, 2, genre2
     * - 1, book1, 2, author2, 1, genre1
     * - 1, book1, 2, author2, 2, genre2
     * - 2, book2, 1, author1, 1, genre1
     * - 2, book2, 1, author1, 2, genre2
     * - 2, book2, 2, author2, 1, genre1
     * - 2, book2, 2, author2, 2, genre2
     */
    @Value
    public static final class BookList {
        private UUID id;
        private String title;
        private UUID authorId;
        private String authorFirstName;
        private UUID authorGenreId;
        private String authorGenreName;
    }
}
