package com.gitlab.devmix.warehouse.launcher.webserver.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * @author Sergey Grachev
 */
@Configuration
@ComponentScan({
        "com.gitlab.devmix.warehouse.core",
        "com.gitlab.devmix.warehouse.storage"
})
public class WebConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addResourceHandlers(final ResourceHandlerRegistry registry) {
        if (!registry.hasMappingForPattern("/webjars/**")) {
            registry.addResourceHandler("/webjars/**")
                    .addResourceLocations("classpath:/META-INF/resources/webjars/");
        }
        if (!registry.hasMappingForPattern("/**")) {
            registry.addResourceHandler("/**")
                    .addResourceLocations(
                            "classpath:/static/",
                            "classpath:/public/",
                            "classpath:/META-INF/resources/",
                            "classpath:/webapp/webui/");
        }
    }

    @Override
    public void addViewControllers(final ViewControllerRegistry registry) {
        registry.addViewController("/").setViewName("index.html");
    }

    @Override
    public void addCorsMappings(final CorsRegistry registry) {
        registry.addMapping("/api/**")
                .allowedOrigins("*")
                .allowedHeaders("*")
                .allowedMethods("*")
                .allowCredentials(true)
                .exposedHeaders("Access-Control-Allow-Origin", "Access-Control-Allow-Credentials");
    }
}
