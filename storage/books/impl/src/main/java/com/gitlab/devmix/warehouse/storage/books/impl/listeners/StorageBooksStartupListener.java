package com.gitlab.devmix.warehouse.storage.books.impl.listeners;

import com.gitlab.devmix.warehouse.storage.books.impl.managers.BooksEntityApiManager;
import com.gitlab.devmix.warehouse.storage.books.impl.managers.DemoManager;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

/**
 * @author Sergey Grachev
 */
@Component
public class StorageBooksStartupListener implements ApplicationListener<ApplicationReadyEvent> {

    @Inject
    private BooksEntityApiManager apiManager;

    @Inject
    private DemoManager demoManager;

    @Override
    public void onApplicationEvent(final ApplicationReadyEvent event) {
        apiManager.init();
        demoManager.init();
    }
}
