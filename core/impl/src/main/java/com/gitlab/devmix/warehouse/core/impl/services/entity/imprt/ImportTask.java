package com.gitlab.devmix.warehouse.core.impl.services.entity.imprt;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * @author Sergey Grachev
 */
@Component
@Slf4j
public class ImportTask implements Runnable {

    private final Object monitor = new Object();

    private volatile boolean active;

    public void trigger() {
        synchronized (monitor) {
            monitor.notify();
        }
    }

    @Override
    public void run() {

    }

    public void active(final boolean active) {
        this.active = active;
        if (active) {
            trigger();
        }
    }

    public boolean isActive() {
        return active;
    }
}
