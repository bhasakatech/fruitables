package com.bhasaka.fruitables.core.models;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test class for {@link MenuItem}.
 *
 * <p>This class verifies menu item properties and
 * nested child item mappings.</p>
 */
@ExtendWith(AemContextExtension.class)
class MenuItemTest {

    private final AemContext context = new AemContext();
    private MenuItem model;

    /**
     * Sets up test context before each test.
     *
     * <p>Loads JSON data and adapts resource to {@link MenuItem}.</p>
     */
    @BeforeEach
    void setUp() {

        context.addModelsForClasses(MenuItem.class, ChildItem.class);

        context.load().json("/menuitem.json", "/content/menu");

        Resource resource = context.resourceResolver().getResource("/content/menu");
        assertNotNull(resource);

        model = resource.adaptTo(MenuItem.class);
        assertNotNull(model);
    }

    /**
     * Tests basic fields of menu item.
     */
    @Test
    void testBasicFields() {
        assertEquals("Shop", model.getLabel());
        assertEquals("/shop", model.getLink());
    }

    /**
     * Tests child menu items mapping.
     */
    @Test
    void testChildren() {

        List<ChildItem> children = model.getChildren();

        assertNotNull(children);
        assertEquals(2, children.size());

        ChildItem child1 = children.get(0);
        assertEquals("Fruits", child1.getChildLabel());
        assertEquals("/shop/fruits", child1.getChildLink());

        ChildItem child2 = children.get(1);
        assertEquals("Vegetables", child2.getChildLabel());
        assertEquals("/shop/vegetables", child2.getChildLink());
    }
}