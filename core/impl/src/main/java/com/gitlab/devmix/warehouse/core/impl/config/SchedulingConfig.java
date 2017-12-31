package com.gitlab.devmix.warehouse.core.impl.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;

/**
 * @author Sergey Grachev
 */
@Configuration
@EnableScheduling
public class SchedulingConfig /*implements SchedulingConfigurer*/ {

//    @Override
//    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
//        taskRegistrar.setScheduler(taskExecutor());
//    }
//
//    @Bean(destroyMethod = "shutdown")
//    public ExecutorService taskExecutor() {
//        return Executors.newScheduledThreadPool(100);
//    }

}
