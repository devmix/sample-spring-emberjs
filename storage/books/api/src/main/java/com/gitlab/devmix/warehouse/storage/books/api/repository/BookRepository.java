package com.gitlab.devmix.warehouse.storage.books.api.repository;

import com.gitlab.devmix.warehouse.storage.books.api.entitity.Book;
import com.gitlab.devmix.warehouse.storage.books.api.projections.BooksBookList;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * @author Sergey Grachev
 */
@Repository
public interface BookRepository extends PagingAndSortingRepository<Book, UUID> {

    @Override
    @EntityGraph(value = "booksBook.list")
    Iterable<Book> findAll();

    // HHH000104: firstResult/maxResults specified with collection fetch; applying in memory!
//    @EntityGraph(value = "booksBook.list")
    Page<BooksBookList> findPagedProjectedByDeletedFalse(Pageable pageable);

    // HHH000104: firstResult/maxResults specified with collection fetch; applying in memory!
//    @EntityGraph(value = "booksBook.list")
    Page<BooksBookList> findPagedProjectedByTitleContainsIgnoreCaseAndDeletedFalse(String title, Pageable pageable);

    @Transactional
    @Modifying(clearAutomatically = true)
    @Query("update booksBook e set e.deleted = true where e.id = :id")
    void recoverableDelete(@Param("id") UUID id);

    @EntityGraph(value = "booksBook.read")
    Book findByIdAndDeletedFalse(UUID id);
}
