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
 * Unit test class for {@link BottomHeaderModel}.
 *
 * <p>This class verifies:
 * <ul>
 *     <li>Logo properties</li>
 *     <li>Menu and nested child structure</li>
 *     <li>Feature flags</li>
 *     <li>Cart-related configurations</li>
 *     <li>Home link mapping</li>
 * </ul>
 * </p>
 */
@ExtendWith(AemContextExtension.class)
class BottomHeaderModelTest {

    private final AemContext context = new AemContext();
    private BottomHeaderModel model;

    /**
     * Sets up test context before each test.
     *
     * <p>Loads JSON data, registers models, and adapts resource
     * to {@link BottomHeaderModel}.</p>
     */
    @BeforeEach
    void setUp() {

        context.addModelsForClasses(
                BottomHeaderModel.class,
                MenuItem.class,
                ChildItem.class
        );

        context.load().json("/bottomheader.json", "/content/header");

        Resource resource = context.resourceResolver().getResource("/content/header");
        assertNotNull(resource);

        model = resource.adaptTo(BottomHeaderModel.class);
        assertNotNull(model);
    }

    /**
     * Tests logo text and image mapping.
     */
    @Test
    void testLogo() {
        assertEquals("Fruitables", model.getLogoText());
        assertEquals("/content/dam/logo.png", model.getLogoImage());
    }

    /**
     * Tests menu structure including nested child items.
     */
    @Test
    void testMenuStructure() {

        List<MenuItem> menu = model.getMenu();

        assertNotNull(menu);
        assertEquals(2, menu.size());

        MenuItem item1 = menu.get(0);
        assertEquals("Home", item1.getLabel());
        assertEquals("/home", item1.getLink());
        assertNull(item1.getChildren());

        MenuItem item2 = menu.get(1);
        assertEquals("Shop", item2.getLabel());
        assertEquals("/shop", item2.getLink());

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

    /**
     * Tests feature flags such as search, cart, and profile.
     */
    @Test
    void testFlags() {
        assertTrue(model.isEnableSearch());
        assertTrue(model.isEnableCart());
        assertFalse(model.isEnableProfile());
    }

    /**
     * Tests cart page path and default cart count.
     */
    @Test
    void testCartDetails() {
        assertEquals("/content/cart", model.getCartPagePath());
        assertEquals(5, model.getDefaultCartCount());
    }

    /**
     * Tests home link property mapping separately.
     */
    @Test
    void testHomeLink() {

        Resource resource = context.create().resource("/content/test",
                "homeLink", "/content/home");

        BottomHeaderModel model = resource.adaptTo(BottomHeaderModel.class);

        assertNotNull(model);
        assertEquals("/content/home", model.getHomeLink());
    }
}