package com.bhasaka.fruitables.core.models;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.SlingHttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.spy;

@ExtendWith(AemContextExtension.class)
class CartModelTest {

    private final AemContext context = new AemContext();
    private CartModel cartModel;
    private String sessionId;
    private final String cartBasePath = "/content/usergenerated/cart";
    private final AemContext ctx = new AemContext();

    private CartModel model;
    @BeforeEach
    void setUp() {

        context.addModelsForClasses(CartModel.class, CartItem.class);
        SlingHttpServletRequest request = context.request();
        request.getSession(true);
        sessionId = request.getSession().getId();

        context.load().json("/products.json", "/content/products");
        context.load().json("/cart.json", cartBasePath + "/" + sessionId);

        cartModel = request.adaptTo(CartModel.class);
        assertNotNull(cartModel, "CartModel should not be null");

        model = spy(new CartModel());
        SlingHttpServletRequest req = ctx.request();


        ctx.request().setAttribute("request", req);
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

        AemContext freshContext = new AemContext();
        freshContext.addModelsForClasses(CartModel.class, CartItem.class);
        CartModel emptyCartModel = freshContext.request().adaptTo(CartModel.class);
        assertNotNull(emptyCartModel, "CartModel should not be null for new session");

        List<CartItem> items = emptyCartModel.getItems();
        assertNotNull(items, "Items list should not be null");
        assertTrue(items.isEmpty(), "Cart should be empty for a session with no cart data");
    }
    @Test
    void testGetSubtotal() {


        CartItem item1 = new CartItem();
        item1.setPrice(10);
        item1.setQty(2);
        CartItem item2 = new CartItem();
        item2.setPrice(15);
        item2.setQty(1);

        doReturn(Arrays.asList(item1, item2)).when(model).getItems();
        double subtotal = model.getSubtotal();
        assertEquals(35.0, subtotal);
    }


    @Test
    void testGetTotal() {
        CartItem item1 = new CartItem();
        item1.setPrice(20);
        item1.setQty(1);

        doReturn(Arrays.asList(item1)).when(model).getItems();
        double total = model.getTotal();
        assertEquals(23.0, total); 
    }
}