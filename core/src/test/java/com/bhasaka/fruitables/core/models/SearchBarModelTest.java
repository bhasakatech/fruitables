package com.bhasaka.fruitables.core.models;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(AemContextExtension.class)
class SearchBarModelTest {

    private final AemContext context = new AemContext();

    private SearchBarModel model;

    @BeforeEach
    void setUp() {
        context.addModelsForClasses(SearchBarModel.class);
    }

    @Test
    void testModelWithValues() {
        context.load().json("/search-bar.json", "/content/test");

        Resource resource = context.resourceResolver().getResource("/content/test");
        model = resource.adaptTo(SearchBarModel.class);

        assertNotNull(model);
        assertEquals("Search fruits", model.getPlaceholder());
        assertEquals("Submit Now", model.getButtonLabel());
    }

    @Test
    void testModelWithoutValues() {
        context.create().resource("/content/empty");

        Resource resource = context.resourceResolver().getResource("/content/empty");
        model = resource.adaptTo(SearchBarModel.class);

        assertNotNull(model);
        assertNull(model.getPlaceholder());
        assertNull(model.getButtonLabel());
    }
}