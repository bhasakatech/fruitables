package com.bhasaka.fruitables.core.models;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test class for {@link StatCard}.
 *
 * <p>This class verifies mapping of stat card properties
 * such as icon image, title, and value.</p>
 */
@ExtendWith(AemContextExtension.class)
class StatCardTest {

    private final AemContext context = new AemContext();
    private StatCard model;

    /**
     * Sets up test context before each test.
     *
     * <p>Loads JSON data and adapts resource to {@link StatCard}.</p>
     */
    @BeforeEach
    void setUp() {
        context.addModelsForClasses(StatCard.class);
        context.load().json("/statcard.json", "/content/card");
        Resource resource = context.resourceResolver().getResource("/content/card");
        assertNotNull(resource);
        model = resource.adaptTo(StatCard.class);
        assertNotNull(model);
    }

    /**
     * Tests stat card field mappings.
     */
    @Test
    void testStatCardFields() {
        assertEquals("/content/dam/icons/apple.png", model.getIconImage());
        assertEquals("Apples Sold", model.getTitle());
        assertEquals("150", model.getValue());
    }
}