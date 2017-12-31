package com.gitlab.devmix.warehouse.core.api.entity;

import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.UUID;

/**
 * @author Sergey Grachev
 */
@Data
@MappedSuperclass
public abstract class AbstractIdEntity implements BaseEntity<UUID> {

    @Column(name = "ID")
    @Id
    @GeneratedValue(generator = "UUID")
    @GenericGenerator(name = "UUID", strategy = "org.hibernate.id.UUIDGenerator")
    private UUID id;
}
