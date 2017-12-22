package com.gitlab.devmix.warehouse.storage.books.api.entitity;

import com.gitlab.devmix.warehouse.core.api.entity.AbstractEntity;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

/**
 * @author Sergey Grachev
 */
@Entity(name = Genre.ENTITY)
@Table(name = "BOOKS_GENRE")
@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class Genre extends AbstractEntity {

    public static final String ENTITY = "booksGenre";

    @Column(name = "NAME", length = Byte.MAX_VALUE)
    private String name;
}