package com.gitlab.devmix.warehouse.storage.books.impl.listeners;

import com.gitlab.devmix.warehouse.storage.books.impl.managers.DemoManager;
import com.gitlab.devmix.warehouse.storage.books.impl.managers.EntityApiManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

/**
 * @author Sergey Grachev
 */
@Component
public class StorageBooksStartupListener implements ApplicationListener<ApplicationReadyEvent> {

    private final Logger logger = LoggerFactory.getLogger(this.getClass());

    @Inject
    private EntityApiManager apiManager;

    @Inject
    private DemoManager demoManager;

    @Override
    public void onApplicationEvent(final ApplicationReadyEvent event) {
        apiManager.init();
        demoManager.init();
    }
}
