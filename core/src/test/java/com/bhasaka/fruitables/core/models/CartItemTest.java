package com.bhasaka.fruitables.core.models;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test class for {@link CartItem}.
 *
 * <p>This class verifies:
 * <ul>
 *     <li>Basic field mapping from resource properties</li>
 *     <li>Total price calculation logic</li>
 *     <li>Dynamic updates when price or quantity changes</li>
 * </ul>
 * </p>
 */
@ExtendWith(AemContextExtension.class)
class CartItemTest {

    private final AemContext context = new AemContext();
    private CartItem cartItem;

    /**
     * Sets up test data before each test.
     *
     * <p>Loads JSON data, reads properties from resource,
     * and initializes {@link CartItem}.</p>
     */
    @BeforeEach
    void setUp() {
        context.load().json("/cartitem.json", "/content/cart");
        Resource resource = context.resourceResolver().getResource("/content/cart");
        assertNotNull(resource, "Resource should not be null");

        ValueMap properties = resource.getValueMap();

        cartItem = new CartItem();

        cartItem.setName(properties.get("name", String.class));
        cartItem.setImage(properties.get("image", String.class));
        cartItem.setProductPath(properties.get("productPath", String.class));

        Double price = properties.get("price", Double.class);
        if (price != null) {
            cartItem.setPrice(price);
        }

        Integer qty = properties.get("qty", Integer.class);
        if (qty != null) {
            cartItem.setQty(qty);
        }

        assertNotNull(cartItem, "CartItem should be instantiated");
    }

    /**
     * Tests basic field values of cart item.
     */
    @Test
    void testCartItemBasicFields() {
        assertEquals("Mango", cartItem.getName());
        assertEquals(75.50, cartItem.getPrice(), 0.001);
        assertEquals("/assets/images/mango.jpg", cartItem.getImage());
        assertEquals(3, cartItem.getQty());
        assertEquals("/content/fruits/mango", cartItem.getProductPath());
    }

    /**
     * Tests total calculation (price * quantity).
     */
    @Test
    void testTotalCalculation() {
        assertEquals(226.5, cartItem.getTotal(), 0.001,
                "Total should be price * quantity");
    }

    /**
     * Tests that total updates when price or quantity changes.
     */
    @Test
    void testTotalUpdatesWhenPriceOrQtyChanges() {

        cartItem.setPrice(100.0);
        assertEquals(300.0, cartItem.getTotal(), 0.001);

        cartItem.setQty(5);
        assertEquals(500.0, cartItem.getTotal(), 0.001);
    }
}