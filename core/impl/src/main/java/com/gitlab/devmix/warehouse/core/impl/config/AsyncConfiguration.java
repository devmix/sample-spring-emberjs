package com.gitlab.devmix.warehouse.core.impl.config;

import org.springframework.aop.interceptor.AsyncUncaughtExceptionHandler;
import org.springframework.aop.interceptor.SimpleAsyncUncaughtExceptionHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.AsyncTaskExecutor;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

/**
 * @author Sergey Grachev
 */
@Configuration
@EnableAsync
public class AsyncConfiguration implements AsyncConfigurer {

    public static final String SYS_EXPORT_ENTITY_TASK_EXECUTOR = "sysExportEntityTaskExecutor";
    public static final String SYS_IMPORT_ENTITY_TASK_EXECUTOR = "sysImportEntityTaskExecutor";
    public static final String SYS_ASYNC_TASK_EXECUTOR = "sysAsyncTaskExecutor";

    @Bean(SYS_ASYNC_TASK_EXECUTOR)
    public AsyncTaskExecutor asyncTaskExecutor() {
        final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setThreadNamePrefix("async-");
        return executor;
    }

    @Override
    public Executor getAsyncExecutor() {
        return asyncTaskExecutor();
    }

    @Override
    public AsyncUncaughtExceptionHandler getAsyncUncaughtExceptionHandler() {
        return new SimpleAsyncUncaughtExceptionHandler();
    }

    @Bean(SYS_EXPORT_ENTITY_TASK_EXECUTOR)
    public ThreadPoolTaskExecutor sysExportEntityTaskExecutor() {
        final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setThreadNamePrefix("entity-export-");
        return executor;
    }

    @Bean(SYS_IMPORT_ENTITY_TASK_EXECUTOR)
    public ThreadPoolTaskExecutor sysImportEntityTaskExecutor() {
        final ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setThreadNamePrefix("entity-import-");
        return executor;
    }
}
