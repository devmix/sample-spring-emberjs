package com.gitlab.devmix.warehouse.core.api.entity.export;

import com.gitlab.devmix.warehouse.core.api.entity.AbstractIdEntity;
import com.gitlab.devmix.warehouse.core.api.entity.security.User;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.Index;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author Sergey Grachev
 */
@Entity(name = ExportProcess.ENTITY)
@Table(name = "SYS_EXPORT_PROCESS", indexes = {
        @Index(name = "state", columnList = "state")
})
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class ExportProcess extends AbstractIdEntity {

    public static final String ENTITY = "sysExportProcess";

    @Enumerated(EnumType.STRING)
    @Column(name = "STATE", nullable = false)
    @NotNull
    private State state = State.WAITING;

    @Column(name = "CREATE_DATE", nullable = false)
    @NotNull
    private Date createDate = new Date();

    @Column(name = "START_DATE")
    private Date startDate;

    @Column(name = "FINISH_DATE")
    private Date finishDate;

    @Column(name = "PERCENT")
    private float percent;

    @Lob
    @Column(name = "OPTIONS")
    private String options;

    @Column(name = "ENTITIES")
    private String entities;

    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "USER_ID", nullable = false)
    private User user;

    public enum State {
        WAITING,
        PROCESSING,
        FINISHED,
        FAILED
    }
}
