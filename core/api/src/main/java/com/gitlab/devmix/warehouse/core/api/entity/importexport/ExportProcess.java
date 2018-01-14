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
@Entity(name = ExportProcess.ENTITY)
@Table(name = "SYS_EXPORT_PROCESS", indexes = {
        @Index(name = "SYS_EXPORT_PROCESS_STATE_IDX", columnList = "state")
})
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ExportProcess extends AbstractProcess {

    public static final String ENTITY = "sysExportProcess";

}
