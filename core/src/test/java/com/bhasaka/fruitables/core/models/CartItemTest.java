package com.bhasaka.fruitables.core.models;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(AemContextExtension.class)
class CartItemTest {

    private final AemContext context = new AemContext();
    private CartItem cartItem;

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

    @Test
    void testCartItemBasicFields() {
        assertEquals("Mango", cartItem.getName());
        assertEquals(75.50, cartItem.getPrice(), 0.001);
        assertEquals("/assets/images/mango.jpg", cartItem.getImage());
        assertEquals(3, cartItem.getQty());
        assertEquals("/content/fruits/mango", cartItem.getProductPath());
    }

    @Test
    void testTotalCalculation() {
        assertEquals(226.5, cartItem.getTotal(), 0.001,
                "Total should be price * quantity");
    }

    @Test
    void testTotalUpdatesWhenPriceOrQtyChanges() {

        cartItem.setPrice(100.0);
        assertEquals(300.0, cartItem.getTotal(), 0.001);


        cartItem.setQty(5);
        assertEquals(500.0, cartItem.getTotal(), 0.001);
    }
}