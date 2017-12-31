package com.gitlab.devmix.warehouse.storage.books.api.repository;

import com.gitlab.devmix.warehouse.storage.books.api.entity.Genre;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.UUID;

/**
 * @author Sergey Grachev
 */
@Repository
public interface GenreRepository extends PagingAndSortingRepository<Genre, UUID> {

}
