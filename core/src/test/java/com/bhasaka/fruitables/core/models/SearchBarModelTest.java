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

        // Load JSON
        context.load().json("/search-bar.json", "/content");

        // Set current resource
        Resource resource = context.resourceResolver().getResource("/content/search");
        context.currentResource(resource);

        // Adapt model
        model = resource.adaptTo(SearchBarModel.class);
    }

    @Test
    void testValuesFromJson() {
        assertNotNull(model);

        assertEquals("Search Fruits", model.getPlaceholder());
        assertEquals("Find Now", model.getButtonLabel());
    }

    @Test
    void testDefaultValuesWhenEmpty() {

        // Create empty resource (no properties)
        context.build().resource("/content/emptySearch");

        Resource resource = context.resourceResolver().getResource("/content/emptySearch");
        SearchBarModel emptyModel = resource.adaptTo(SearchBarModel.class);

        assertNotNull(emptyModel);

        // Validate defaults
        assertEquals("Search", emptyModel.getPlaceholder());
        assertEquals("Submit Now", emptyModel.getButtonLabel());
    }
}
