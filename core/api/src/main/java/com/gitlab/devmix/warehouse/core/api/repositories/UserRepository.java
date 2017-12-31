package com.gitlab.devmix.warehouse.core.api.repositories;

import com.gitlab.devmix.warehouse.core.api.entity.security.User;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import javax.annotation.Nullable;
import java.util.UUID;

/**
 * @author Sergey Grachev
 */
@Repository
public interface UserRepository extends PagingAndSortingRepository<User, UUID> {

    @Nullable
    User findByIdentity(String identity);
}
