package com.gitlab.devmix.warehouse.core.api.entity;

import lombok.Data;

import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;

/**
 * @author Sergey Grachev
 */
@Data
@Entity(name = "sysFileEntity")
@Table(name = "SYS_FILE_ENTITY")
public class FileEntity implements BaseEntity<String> {

    @Id
    @Column(name = "ID")
    private String id;

    @Column(name = "FILE_NAME", length = 0xFF)
    private String fileName;

    @Lob
    @Basic(fetch = FetchType.EAGER)
    @Column(name = "CONTENT")
    private byte[] content;
}
