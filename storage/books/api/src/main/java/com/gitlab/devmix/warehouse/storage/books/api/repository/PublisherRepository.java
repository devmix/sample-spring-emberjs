package com.gitlab.devmix.warehouse.storage.books.api.repository;

import com.gitlab.devmix.warehouse.storage.books.api.entitity.Publisher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author Sergey Grachev
 */
@Repository
public interface PublisherRepository extends PagingAndSortingRepository<Publisher, UUID> {

    Page<Publisher> findPagedByDeletedFalse(Pageable pageable);

    Publisher findByIdAndDeletedFalse(UUID id);
}