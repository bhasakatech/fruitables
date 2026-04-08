package com.bhasaka.fruitables.core.models;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.SlingHttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(AemContextExtension.class)
class CartModelTest {

    private final AemContext context = new AemContext();
    private CartModel cartModel;
    private String sessionId;
    private final String cartBasePath = "/content/usergenerated/cart";

    @BeforeEach
    void setUp() {
        // Register model classes
        context.addModelsForClasses(CartModel.class, CartItem.class);

        // Create session and capture session ID
        SlingHttpServletRequest request = context.request();
        request.getSession(true);
        sessionId = request.getSession().getId();

        // Load product and cart data
        context.load().json("/products.json", "/content/products");
        context.load().json("/cart.json", cartBasePath + "/" + sessionId);

        // Adapt request to CartModel
        cartModel = request.adaptTo(CartModel.class);
        assertNotNull(cartModel, "CartModel should not be null");
    }

    @Test
    void testCartModelReturnsItems() {
        List<CartItem> items = cartModel.getItems();

        assertNotNull(items, "Items list should not be null");
        assertEquals(2, items.size(), "Should return 2 cart items");
    }

    @Test
    void testCartItemDetails_Mango() {
        List<CartItem> items = cartModel.getItems();

        CartItem mango = items.stream()
                .filter(item -> "Mango".equals(item.getName()))
                .findFirst()
                .orElse(null);

        assertNotNull(mango, "Mango item should be present");
        assertEquals("Mango", mango.getName());
        assertEquals(75.50, mango.getPrice(), 0.001);
        assertEquals(2, mango.getQty());
        assertEquals("/content/products/mango", mango.getProductPath());
        assertTrue(mango.getImage().contains("mango") || mango.getImage().contains("default"),
                "Image should contain mango or fallback");
        assertEquals(151.0, mango.getTotal(), 0.001);
    }

    @Test
    void testCartItemDetails_Apple() {
        List<CartItem> items = cartModel.getItems();

        CartItem apple = items.stream()
                .filter(item -> "Apple".equals(item.getName()))
                .findFirst()
                .orElse(null);

        assertNotNull(apple, "Apple item should be present");
        assertEquals("Apple", apple.getName());
        assertEquals(120.0, apple.getPrice(), 0.001);
        assertEquals(3, apple.getQty());
        assertEquals("/content/products/apple", apple.getProductPath());
        assertEquals(360.0, apple.getTotal(), 0.001);
    }

    @Test
    void testEmptyCartWhenNoCartResource() {
        // Create a fresh AemContext
        AemContext freshContext = new AemContext();
        freshContext.addModelsForClasses(CartModel.class, CartItem.class);

        // Fresh request and session, no cart data loaded
        CartModel emptyCartModel = freshContext.request().adaptTo(CartModel.class);
        assertNotNull(emptyCartModel, "CartModel should not be null for new session");

        List<CartItem> items = emptyCartModel.getItems();
        assertNotNull(items, "Items list should not be null");
        assertTrue(items.isEmpty(), "Cart should be empty for a session with no cart data");
    }
}