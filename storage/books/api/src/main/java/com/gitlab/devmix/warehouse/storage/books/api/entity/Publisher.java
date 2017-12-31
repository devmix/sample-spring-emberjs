package com.gitlab.devmix.warehouse.storage.books.api.entity;

import com.gitlab.devmix.warehouse.core.api.entity.AbstractRecoverableEntity;
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
@Entity(name = Publisher.ENTITY)
@Table(name = "BOOKS_PUBLISHER")
@Data
@EqualsAndHashCode(callSuper = false)
@ToString(callSuper = true)
public class Publisher extends AbstractRecoverableEntity {

    public static final String ENTITY = "booksPublisher";

    @Column(name = "NAME")
    @Size(max = 255)
    @NotNull
    private String name;
}
