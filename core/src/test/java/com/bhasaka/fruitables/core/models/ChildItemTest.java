package com.bhasaka.fruitables.core.models;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test class for {@link ChildItem}.
 *
 * <p>This class verifies mapping of child menu item properties
 * from resource to model.</p>
 */
@ExtendWith(AemContextExtension.class)
class ChildItemTest {

    private final AemContext context = new AemContext();
    private ChildItem model;

    /**
     * Sets up test context before each test.
     *
     * <p>Loads JSON data and adapts resource to {@link ChildItem}.</p>
     */
    @BeforeEach
    void setUp() {

        context.addModelsForClasses(ChildItem.class);

        context.load().json("/childitem.json", "/content/child");

        Resource resource = context.resourceResolver().getResource("/content/child");
        assertNotNull(resource);

        model = resource.adaptTo(ChildItem.class);
        assertNotNull(model);
    }

    /**
     * Tests child label and link mapping.
     */
    @Test
    void testChildFields() {
        assertEquals("Fruits", model.getChildLabel());
        assertEquals("/shop/fruits", model.getChildLink());
    }
}