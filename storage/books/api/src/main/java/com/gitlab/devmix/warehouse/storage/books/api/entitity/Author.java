package com.gitlab.devmix.warehouse.storage.books.api.entitity;

import com.gitlab.devmix.warehouse.core.api.entity.AbstractEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author Sergey Grachev
 */
@Entity(name = Author.ENTITY)
@Table(name = "BOOKS_AUTHOR")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Author extends AbstractEntity {

    public static final String ENTITY = "booksAuthor";

    @Column(name = "FIRST_NAME")
    @Size(max = 255)
    @NotNull
    private String firstName;

    @Column(name = "MIDDLE_NAME")
    @Size(max = 255)
    private String middleName;

    @Column(name = "LAST_NAME")
    @Size(max = 255)
    @NotNull
    private String lastName;
}
