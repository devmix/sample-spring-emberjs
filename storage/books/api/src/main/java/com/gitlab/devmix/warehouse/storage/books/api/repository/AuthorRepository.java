package com.gitlab.devmix.warehouse.storage.books.api.repository;

import com.gitlab.devmix.warehouse.storage.books.api.entitity.Author;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author Sergey Grachev
 */
@Repository
public interface AuthorRepository extends PagingAndSortingRepository<Author, UUID> {

    Page<Author> findPagedByDeletedFalse(Pageable pageable);

    Author findByIdAndDeletedFalse(UUID id);
}
