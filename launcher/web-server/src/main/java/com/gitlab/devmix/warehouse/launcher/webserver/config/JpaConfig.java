package com.gitlab.devmix.warehouse.launcher.webserver.config;

import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * @author Sergey Grachev
 */
@Configuration
@EnableJpaRepositories({
        "com.gitlab.devmix.warehouse.core",
        "com.gitlab.devmix.warehouse.storage"
})
@EntityScan({
        "com.gitlab.devmix.warehouse.core",
        "com.gitlab.devmix.warehouse.storage"
})
@EnableTransactionManagement
public class JpaConfig {
}
