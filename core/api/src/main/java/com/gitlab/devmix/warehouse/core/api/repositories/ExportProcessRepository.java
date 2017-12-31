package com.gitlab.devmix.warehouse.core.api.repositories;

import com.gitlab.devmix.warehouse.core.api.entity.export.ExportProcess;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Nullable;
import java.util.UUID;

/**
 * @author Sergey Grachev
 */
public interface ExportProcessRepository extends PagingAndSortingRepository<ExportProcess, UUID> {

    @Nullable
    ExportProcess findFirstByState(ExportProcess.State state);

    @Transactional
    @Modifying
    @Query("update sysExportProcess e set e.state = :to where e.state = :from")
    void switchState(@Param("from") ExportProcess.State from, @Param("to") ExportProcess.State to);
}
