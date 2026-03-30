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
class BottomHeaderModelTest {

    private final AemContext context = new AemContext();
    private BottomHeaderModel model;

    @BeforeEach
    void setUp() {

        // Register ALL models
        context.addModelsForClasses(
                BottomHeaderModel.class,
                MenuItem.class,
                ChildItem.class
        );

        // Load JSON
        context.load().json("/bottomheader.json", "/content/header");

        // Get resource
        Resource resource = context.resourceResolver().getResource("/content/header");
        assertNotNull(resource);

        // Adapt to model
        model = resource.adaptTo(BottomHeaderModel.class);
        assertNotNull(model);
    }

    @Test
    void testLogo() {
        assertEquals("Fruitables", model.getLogoText());
        assertEquals("/content/dam/logo.png", model.getLogoImage());
    }

    @Test
    void testMenuStructure() {

        List<MenuItem> menu = model.getMenu();

        assertNotNull(menu);
        assertEquals(2, menu.size());

        // ----- Item 1 -----
        MenuItem item1 = menu.get(0);
        assertEquals("Home", item1.getLabel());
        assertEquals("/home", item1.getLink());
        assertNull(item1.getChildren()); // no children

        // ----- Item 2 -----
        MenuItem item2 = menu.get(1);
        assertEquals("Shop", item2.getLabel());
        assertEquals("/shop", item2.getLink());

        // ----- Children -----
        List<ChildItem> children = item2.getChildren();
        assertNotNull(children);
        assertEquals(2, children.size());

        ChildItem child1 = children.get(0);
        assertEquals("Fruits", child1.getChildLabel());
        assertEquals("/shop/fruits", child1.getChildLink());

        ChildItem child2 = children.get(1);
        assertEquals("Vegetables", child2.getChildLabel());
        assertEquals("/shop/vegetables", child2.getChildLink());
    }

    @Test
    void testFlags() {
        assertTrue(model.isEnableSearch());
        assertTrue(model.isEnableCart());
        assertFalse(model.isEnableProfile());
    }

    @Test
    void testCartDetails() {
        assertEquals("/content/cart", model.getCartPagePath());
        assertEquals(5, model.getDefaultCartCount());
    }
}