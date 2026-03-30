package com.bhasaka.fruitables.core.models;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(AemContextExtension.class)
class ChildItemTest {

    private final AemContext context = new AemContext();
    private ChildItem model;

    @BeforeEach
    void setUp() {

        // Register model
        context.addModelsForClasses(ChildItem.class);

        // Load JSON
        context.load().json("/childitem.json", "/content/child");

        // Get resource
        Resource resource = context.resourceResolver().getResource("/content/child");
        assertNotNull(resource);

        // Adapt
        model = resource.adaptTo(ChildItem.class);
        assertNotNull(model);
    }

    @Test
    void testChildFields() {
        assertEquals("Fruits", model.getChildLabel());
        assertEquals("/shop/fruits", model.getChildLink());
    }
}