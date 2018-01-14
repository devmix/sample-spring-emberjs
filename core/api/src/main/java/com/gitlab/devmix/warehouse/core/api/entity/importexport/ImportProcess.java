package com.gitlab.devmix.warehouse.core.api.entity.importexport;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Entity;
import javax.persistence.Index;
import javax.persistence.Table;

/**
 * @author Sergey Grachev
 */
@Entity(name = ImportProcess.ENTITY)
@Table(name = "SYS_IMPORT_PROCESS", indexes = {
        @Index(name = "SYS_IMPORT_PROCESS_STATE_IDX", columnList = "state")
})
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ImportProcess extends AbstractProcess {

    public static final String ENTITY = "sysImportProcess";
}
