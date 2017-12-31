package com.gitlab.devmix.warehouse.core.api.web.entity;

import com.gitlab.devmix.warehouse.core.api.App;
import com.gitlab.devmix.warehouse.core.api.entity.security.User;
import com.gitlab.devmix.warehouse.core.api.web.entity.metadata.EntityMetadata;
import org.testng.annotations.Test;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Sergey Grachev
 */
@Test
public class EntityMetadataTest {

    static {
        EntityMetadata.scanClassBase(singletonList(App.class));
    }

    public void testFindByName() {
        final EntityMetadata.Descriptor descriptor = EntityMetadata.of(User.ENTITY);

        assertThat(descriptor).isNotNull();
        assertThat(descriptor.getEntityName()).isEqualTo(User.ENTITY);
    }
}
