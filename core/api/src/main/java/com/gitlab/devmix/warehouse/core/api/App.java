package com.gitlab.devmix.warehouse.core.api;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * @author Sergey Grachev
 */
@Component("coreApp")
public class App implements ApplicationContextAware {

    private static ApplicationContext ctx;

    @Override
    public void setApplicationContext(final ApplicationContext ctx) throws BeansException {
        App.ctx = ctx;
    }

    public static <T> T getBean(final Class<T> beanClass) {
        return ctx.getBean(beanClass);
    }
}
