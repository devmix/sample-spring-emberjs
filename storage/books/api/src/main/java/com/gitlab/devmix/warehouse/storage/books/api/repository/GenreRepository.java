package com.gitlab.devmix.warehouse.storage.books.api.repository;

import com.gitlab.devmix.warehouse.storage.books.api.entitity.Genre;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author Sergey Grachev
 */
@Repository
public interface GenreRepository extends PagingAndSortingRepository<Genre, UUID> {

    Page<Genre> findPagedByDeletedFalse(Pageable pageable);

    Genre findByIdAndDeletedFalse(UUID id);
}
