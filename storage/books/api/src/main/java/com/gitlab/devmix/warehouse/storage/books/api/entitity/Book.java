package com.gitlab.devmix.warehouse.storage.books.api.entitity;

import com.gitlab.devmix.warehouse.core.api.entity.AbstractEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;
import javax.persistence.Table;
import java.util.Date;
import java.util.Set;

/**
 * @author Sergey Grachev
 */
@Entity(name = Book.ENTITY)
@Table(name = "BOOKS_BOOK")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@NamedEntityGraphs({
        @NamedEntityGraph(name = "booksBook.list",
                attributeNodes = {
                        @NamedAttributeNode(value = "authors"),
                }),
        @NamedEntityGraph(name = "booksBook.read",
                attributeNodes = {
//                        @NamedAttributeNode(value = "authors"),
//                        @NamedAttributeNode(value = "genres"),
//                        @NamedAttributeNode(value = "publisher"),
                })
})
public class Book extends AbstractEntity {

    public static final String ENTITY = "booksBook";

    @Column(name = "TITLE", length = Short.MAX_VALUE)
    private String title;

    @Column(name = "LANGUAGE", length = 3)
    private String language;

    @Column(name = "PUBLISH_DATE")
    private Date publishDate;

    @Column(name = "ISBN13", length = 14)
    private String isnb13;

    @Column(name = "DESCRIPTION", length = Short.MAX_VALUE)
    private String description;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "BOOKS_BOOK_AUTHORS")
    @Where(clause = "deleted = false")
    private Set<Author> authors;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "BOOKS_BOOK_GENRES")
    @Where(clause = "deleted = false")
    private Set<Genre> genres;

    @ManyToOne(fetch = FetchType.LAZY)
    @Where(clause = "deleted = false")
    private Publisher publisher;

}
