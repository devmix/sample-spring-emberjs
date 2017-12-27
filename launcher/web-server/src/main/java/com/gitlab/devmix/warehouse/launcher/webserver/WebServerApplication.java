package com.gitlab.devmix.warehouse.launcher.webserver;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * @author Sergey Grachev
 */
@SpringBootApplication
@EnableAutoConfiguration
@SuppressWarnings("checkstyle:hideutilityclassconstructor")
public class WebServerApplication {

    public static void main(final String[] args) {
        SpringApplication.run(WebServerApplication.class, args);
    }
}
