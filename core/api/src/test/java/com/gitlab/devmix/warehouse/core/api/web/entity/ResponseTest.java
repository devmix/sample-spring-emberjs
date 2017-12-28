package com.gitlab.devmix.warehouse.core.api.web.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Data;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Sergey Grachev
 */
@Test
public class ResponseTest {

    public void testProjection() throws IOException {
        final Response<Foo> response = Response.of(Foo.class).include(Bar.class).include(Baz.class)
                .add(Foo.builder()
                        .id("1").name("n1").secret("s1")
                        .bars(asList(
                                Bar.builder().id("11").name("bar1").secret("s11").build(),
                                Bar.builder().id("12").name("bar2").secret("s12").build()))
                        .baz(Baz.builder()
                                .id("9").name("baz1").secret("sbaz").build())
                        .build())
                .projection("id", "name", "bars.id", "bars.name", "baz.*")
                .single();

        @SuppressWarnings("unchecked") final Response<Foo> expected = new ObjectMapper()
                .readValue(this.getClass().getResourceAsStream("/response-with-projection.json"), Response.class);

        assertThat(response).isEqualTo(expected);
    }

    @Builder
    @Data
    private static final class Foo {
        @Metadata.Id
        public String id;

        public String name;

        public String secret;

        @Metadata.Many
        public List<Bar> bars;

        @Metadata.One
        public Baz baz;
    }

    @Builder
    @Data
    private static final class Bar {
        @Metadata.Id
        public String id;

        public String name;

        public String secret;
    }


    @Builder
    @Data
    private static final class Baz {
        @Metadata.Id
        public String id;

        public String name;

        public String secret;
    }
}
