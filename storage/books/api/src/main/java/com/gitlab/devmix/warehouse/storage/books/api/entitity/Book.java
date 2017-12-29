package com.gitlab.devmix.warehouse.storage.books.api.entitity;

import com.gitlab.devmix.warehouse.core.api.entity.AbstractEntity;
import com.gitlab.devmix.warehouse.core.api.web.entity.Request;
import com.gitlab.devmix.warehouse.core.api.web.entity.jpa.JpaQuery;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.hibernate.annotations.Where;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinTable;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.NamedAttributeNode;
import javax.persistence.NamedEntityGraph;
import javax.persistence.NamedEntityGraphs;
import javax.persistence.Table;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.JoinType;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.Date;
import java.util.Set;

/**
 * @author Sergey Grachev
 */
@Entity(name = Book.ENTITY)
@Table(name = "BOOKS_BOOK")
@Data
@EqualsAndHashCode(callSuper = true, exclude = {"authors", "genres", "publisher"})
@ToString(callSuper = true, exclude = {"authors", "genres", "publisher"})
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

    public static final String[] PROJECTION_LIST = {
            "id", "title", "description",
            "authors.id", "authors.firstName", "authors.middleName", "authors.lastName",
            "publisher.id", "publisher.name"
    };

    public static final JpaQuery.SearchBuilder<Book, Request> SEARCH_LIST = (q, c, e) -> {
        if (q.hasSearch()) {
            final String search = "%" + q.getSearch().toLowerCase() + "%";
            final Join<Object, Object> authors = e.join("authors", JoinType.LEFT);
            return c.or(
                    c.like(c.lower(e.get("title")), search),
                    c.like(c.lower(e.get("isnb13")), search),
                    c.like(c.lower(authors.get("firstName")), search),
                    c.like(c.lower(authors.get("middleName")), search),
                    c.like(c.lower(authors.get("lastName")), search));
        }
        return null;
    };

    @Column(name = "TITLE")
    @Size(max = 255)
    @NotNull
    private String title;

    @Column(name = "LANGUAGE", length = 3)
    @Size(max = 3)
    private String language;

    @Column(name = "PUBLISH_DATE")
    private Date publishDate;

    @Column(name = "ISBN13", length = 14)
    @Size(max = 14)
    @NotNull
    private String isnb13;

    @Lob
    @Column(name = "DESCRIPTION", length = Short.MAX_VALUE)
    @Size(max = Short.MAX_VALUE)
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
