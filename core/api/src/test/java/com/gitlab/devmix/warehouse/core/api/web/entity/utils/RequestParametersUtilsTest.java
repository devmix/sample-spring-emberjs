package com.gitlab.devmix.warehouse.core.api.web.entity.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.testng.annotations.Test;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Sergey Grachev
 */
@Test
public class RequestParametersUtilsTest {

    public void testQueryToMap() throws IOException {
        final Map<String, Object> query = new LinkedHashMap<>();
        query.put("search", "1");
        query.put("sort[title]", "asc");
        query.put("sort2[title][direction]", "desc");
        query.put("sort2[title][nullable]", "false");
        query.put("sort3[0][nullable]", "asc");
        query.put("sort3[0][direction]", "false");
        query.put("sort3[1][nullable]", "false");
        query.put("sort3[1][direction]", "true");
        query.put("sort4[0][direction][0][test]", "true");
        query.put("sort4[0][direction][1][test]", "true");
        query.put("sort4[0][direction][1][test2]", "true");
        query.put("sort4[1][direction][0][test]", "true");

        @SuppressWarnings("unchecked") final Map<String, Object> expected = new ObjectMapper()
                .readValue(this.getClass().getResourceAsStream("/request-normalized.json"), Map.class);

        assertThat(RequestParametersUtils.queryToMap(query)).isEqualTo(expected);
    }
}
