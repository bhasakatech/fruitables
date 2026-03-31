package com.bhasaka.fruitables.core.models;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(AemContextExtension.class)
class MenuItemTest {

    private final AemContext context = new AemContext();
    private MenuItem model;

    @BeforeEach
    void setUp() {

        // Register models
        context.addModelsForClasses(MenuItem.class, ChildItem.class);

        // Load JSON
        context.load().json("/menuitem.json", "/content/menu");

        // Get resource
        Resource resource = context.resourceResolver().getResource("/content/menu");
        assertNotNull(resource);

        // Adapt
        model = resource.adaptTo(MenuItem.class);
        assertNotNull(model);
    }

    @Test
    void testBasicFields() {
        assertEquals("Shop", model.getLabel());
        assertEquals("/shop", model.getLink());
    }

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