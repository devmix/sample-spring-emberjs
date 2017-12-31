package com.gitlab.devmix.warehouse.launcher.webserver.config.security;

import com.gitlab.devmix.warehouse.core.api.services.security.DbUserDetailsService;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import javax.inject.Inject;

import static org.springframework.http.HttpMethod.OPTIONS;

/**
 * @author Sergey Grachev
 */
@Configuration
@EnableWebSecurity
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    private final DbUserDetailsService userDetailsService;

    @Inject
    public WebSecurityConfig(final DbUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void configure(final HttpSecurity http) throws Exception {
        configureSecurity(http);
    }

    @Inject
    public void configureGlobal(final AuthenticationManagerBuilder context) throws Exception {
        context.authenticationProvider(authenticationProvider());
    }

    @Bean
    @Override
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        final DaoAuthenticationProvider provider = new DaoAuthenticationProvider();
        provider.setUserDetailsService(userDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    public static void configureSecurity(final HttpSecurity http) throws Exception {
        http.authorizeRequests()
                .antMatchers(OPTIONS, "/api/**").permitAll()
                .antMatchers("/api/core/storage/file/storage/books/book/cover/**").permitAll()
                .antMatchers("/api/sys/export/**").permitAll()
                .antMatchers("/api/**").permitAll()
//                .antMatchers("/console/**").permitAll()
                .anyRequest().permitAll();
    }
}
