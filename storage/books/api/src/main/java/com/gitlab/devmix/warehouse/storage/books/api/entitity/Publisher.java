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
@Entity(name = Publisher.ENTITY)
@Table(name = "BOOKS_PUBLISHER")
@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class Publisher extends AbstractEntity {

    public static final String ENTITY = "booksPublisher";

    @Column(name = "NAME", length = Byte.MAX_VALUE)
    private String name;
}
