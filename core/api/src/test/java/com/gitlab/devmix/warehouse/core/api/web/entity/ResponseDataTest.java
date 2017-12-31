package com.gitlab.devmix.warehouse.core.api.web.entity;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.Builder;
import lombok.Data;
import org.testng.annotations.Test;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import java.io.IOException;
import java.util.List;

import static java.util.Arrays.asList;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Sergey Grachev
 */
@Test
public class ResponseDataTest {

    public void testProjection() throws IOException {
        final ResponseData<Foo> responseData = ResponseData.of(Foo.class).include(Bar.class).include(Baz.class)
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

        @SuppressWarnings("unchecked") final ResponseData<Foo> expected = new ObjectMapper()
                .readValue(this.getClass().getResourceAsStream("/response-with-projection.json"), ResponseData.class);

        assertThat(responseData).isEqualTo(expected);
    }

    @Builder
    @Entity
    @Data
    public static final class Foo {
        @Id
        public String id;

        public String name;

        public String secret;

        @OneToMany
        public List<Bar> bars;

        @OneToOne
        public Baz baz;
    }

    @Builder
    @Entity
    @Data
    public static final class Bar {
        @Id
        public String id;

        public String name;

        public String secret;
    }

    @Builder
    @Entity
    @Data
    public static final class Baz {
        @Id
        public String id;

        public String name;

        public String secret;
    }
}
