package com.gitlab.devmix.warehouse.core.impl.config;

import com.p6spy.engine.spy.appender.MessageFormattingStrategy;
import org.hibernate.engine.jdbc.internal.BasicFormatterImpl;
import org.hibernate.engine.jdbc.internal.Formatter;

/**
 * @author Sergey Grachev
 */
public class SimpleFormatter implements MessageFormattingStrategy {

    private static final Formatter HIBERNATE_SQL_FORMATTER = new BasicFormatterImpl();

    @Override
    public String formatMessage(final int connectionId, final String now, final long elapsed,
                                final String category, final String prepared, final String sql) {
        if (sql.isEmpty()) {
            return "";
        }
        final String batch = "batch".equals(category) ? " add to batch " : "";
        return String.format("Hibernate: %s %s {elapsed: %dms}", batch, HIBERNATE_SQL_FORMATTER.format(sql), elapsed);
    }
}
