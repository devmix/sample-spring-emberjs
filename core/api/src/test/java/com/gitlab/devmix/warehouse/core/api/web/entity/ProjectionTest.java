package com.gitlab.devmix.warehouse.core.api.web.entity;

import org.testng.annotations.Test;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Sergey Grachev
 */
@Test
public class ProjectionTest {

    public void testParser() {
        final Projection projection = Projection.of("id", "name", "bars.id", "bars.name", "baz.*");

        assertThat(projection.isAny()).isFalse();

        // id

        final ProjectionProperty id = projection.next("id");
        assertThat(id).isNotNull();
        assertThat(id.isAny()).isFalse();
        assertThat(id.next("")).isNull();

        // name

        assertThat(projection.next("name")).isNotNull();

        // bars

        final ProjectionProperty bars = projection.next("bars");
        assertThat(bars).isNotNull();
        assertThat(bars.isAny()).isFalse();

        final ProjectionProperty barsId = bars.next("id");
        assertThat(barsId).isNotNull();
        assertThat(barsId.isAny()).isFalse();

        final ProjectionProperty barsName = bars.next("name");
        assertThat(barsName).isNotNull();
        assertThat(barsName.isAny()).isFalse();

        // baz

        final ProjectionProperty baz = projection.next("baz");
        assertThat(baz).isNotNull();
        assertThat(baz.isAny()).isFalse();

        final ProjectionProperty bazAnyId = baz.next("id");
        assertThat(bazAnyId).isNotNull();
        assertThat(bazAnyId.isAny()).isTrue();
    }
}
