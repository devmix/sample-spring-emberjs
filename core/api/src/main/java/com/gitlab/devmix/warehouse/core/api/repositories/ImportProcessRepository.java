package com.gitlab.devmix.warehouse.core.api.repositories;

import com.gitlab.devmix.warehouse.core.api.entity.importexport.ImportProcess;
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
public interface ImportProcessRepository extends PagingAndSortingRepository<ImportProcess, UUID> {

    @Nullable
    ImportProcess findFirstByState(ImportProcess.State state);

    @Transactional
    @Modifying
    @Query("update sysImportProcess e set e.state = :to where e.state = :from")
    void switchState(@Param("from") ImportProcess.State from, @Param("to") ImportProcess.State to);
}
