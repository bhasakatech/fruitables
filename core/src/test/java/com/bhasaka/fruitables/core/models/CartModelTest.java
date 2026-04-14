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

/**
 * Unit test class for {@link CartModel}.
 *
 * <p>This class verifies:
 * <ul>
 *     <li>Cart item retrieval from repository</li>
 *     <li>Product details mapping</li>
 *     <li>Subtotal, shipping, and total calculations</li>
 *     <li>Handling of empty cart scenarios</li>
 *     <li>Authorable dialog field values and defaults</li>
 * </ul>
 * </p>
 */
@ExtendWith(AemContextExtension.class)
class CartModelTest {

    private final AemContext context = new AemContext();
    private CartModel cartModel;
    private CartModel model;
    private String sessionId;
    private final String cartBasePath = "/content/usergenerated/cart";

    /**
     * Sets up test context before each test.
     *
     * <p>Initializes session, loads mock data, and adapts request to {@link CartModel}.</p>
     */
    @BeforeEach
    void setUp() {

        context.addModelsForClasses(CartModel.class, CartItem.class);

        SlingHttpServletRequest request = context.request();
        request.getSession(true);
        sessionId = request.getSession().getId();

        context.request().setResource(context.create().resource("/content/test"));

        context.load().json("/products.json", "/content/products");
        context.load().json("/cart.json", cartBasePath + "/" + sessionId);

        cartModel = request.adaptTo(CartModel.class);

        assertNotNull(cartModel, "CartModel should not be null");

        model = spy(cartModel);
    }

    /**
     * Tests that cart model returns list of items.
     */
    @Test
    void testCartModelReturnsItems() {
        List<CartItem> items = cartModel.getItems();

        assertNotNull(items);
        assertEquals(2, items.size());
    }

    /**
     * Tests details of Mango item in cart.
     */
    @Test
    void testCartItemDetails_Mango() {
        List<CartItem> items = cartModel.getItems();

        CartItem mango = items.stream()
                .filter(item -> "Mango".equals(item.getName()))
                .findFirst()
                .orElse(null);

        assertNotNull(mango);
        assertEquals("Mango", mango.getName());
        assertEquals(75.50, mango.getPrice(), 0.001);
        assertEquals(2, mango.getQty());
        assertEquals("/content/products/mango", mango.getProductPath());
        assertTrue(mango.getImage().contains("mango") || mango.getImage().contains("default"));
        assertEquals(151.0, mango.getTotal(), 0.001);
    }

    /**
     * Tests details of Apple item in cart.
     */
    @Test
    void testCartItemDetails_Apple() {
        List<CartItem> items = cartModel.getItems();

        CartItem apple = items.stream()
                .filter(item -> "Apple".equals(item.getName()))
                .findFirst()
                .orElse(null);

        assertNotNull(apple);
        assertEquals("Apple", apple.getName());
        assertEquals(120.0, apple.getPrice(), 0.001);
        assertEquals(3, apple.getQty());
        assertEquals("/content/products/apple", apple.getProductPath());
        assertEquals(360.0, apple.getTotal(), 0.001);
    }

    /**
     * Tests behavior when cart resource is not present.
     */
    @Test
    void testEmptyCartWhenNoCartResource() {
        AemContext freshContext = new AemContext();
        freshContext.addModelsForClasses(CartModel.class, CartItem.class);

        SlingHttpServletRequest request = freshContext.request();
        request.getSession(true);

        CartModel emptyCartModel = request.adaptTo(CartModel.class);

        assertNotNull(emptyCartModel);

        List<CartItem> items = emptyCartModel.getItems();

        assertNotNull(items);
        assertTrue(items.isEmpty());
    }

    /**
     * Tests subtotal calculation using mocked items.
     */
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

    /**
     * Tests total calculation (subtotal + shipping).
     */
    @Test
    void testGetTotal() {
        CartItem item1 = new CartItem();
        item1.setPrice(20);
        item1.setQty(1);

        doReturn(Arrays.asList(item1)).when(model).getItems();

        double total = model.getTotal();

        assertEquals(23.0, total);
    }

    /**
     * Tests checkout link when not configured.
     */
    @Test
    void testCheckoutLink_WhenNotPresent() {
        CartModel model = context.request().adaptTo(CartModel.class);

        assertNotNull(model);
        assertNull(model.getCheckoutLink(), "checkoutLink should be null when not set");
    }

    /**
     * Tests authorable fields populated from resource.
     */
    @Test
    void testAuthorableFields_FromResource() {

        var props = context.currentResource("/content/test")
                .adaptTo(org.apache.sling.api.resource.ModifiableValueMap.class);

        props.put("subtotalLabel", "My Subtotal");
        props.put("shippingLabel", "Delivery");
        props.put("totalLabel", "Final Total");
        props.put("checkoutBtnText", "Place Order");
        props.put("couponPlaceholder", "Enter Code");
        props.put("couponButtonText", "Apply Now");

        CartModel model = context.request().adaptTo(CartModel.class);

        assertEquals("My Subtotal", model.getSubtotalLabel());
        assertEquals("Delivery", model.getShippingLabel());
        assertEquals("Final Total", model.getTotalLabel());
        assertEquals("Place Order", model.getCheckoutBtnText());
        assertEquals("Enter Code", model.getCouponPlaceholder());
        assertEquals("Apply Now", model.getCouponButtonText());
    }

    /**
     * Tests default values when authorable fields are not set.
     */
    @Test
    void testAuthorableFields_DefaultValues() {

        CartModel model = context.request().adaptTo(CartModel.class);

        assertNull(model.getSubtotalLabel());
        assertNull(model.getShippingLabel());
        assertNull(model.getTotalLabel());
        assertNull(model.getCheckoutBtnText());
        assertNull(model.getCouponPlaceholder());
        assertNull(model.getCouponButtonText());
    }

    /**
     * Tests header field values from resource.
     */
    @Test
    void testHeaderFields_FromResource() {

        var props = context.currentResource("/content/test")
                .adaptTo(org.apache.sling.api.resource.ModifiableValueMap.class);

        props.put("productsHeader", "My Products");
        props.put("nameHeader", "Item Name");
        props.put("priceHeader", "Cost");
        props.put("quantityHeader", "Qty");
        props.put("totalHeaderTable", "Amount");
        props.put("handleHeader", "Actions");

        CartModel model = context.request().adaptTo(CartModel.class);

        assertEquals("My Products", model.getProductsHeader());
        assertEquals("Item Name", model.getNameHeader());
        assertEquals("Cost", model.getPriceHeader());
        assertEquals("Qty", model.getQuantityHeader());
        assertEquals("Amount", model.getTotalHeaderTable());
        assertEquals("Actions", model.getHandleHeader());
    }

    /**
     * Tests default values for header fields.
     */
    @Test
    void testHeaderFields_DefaultValues() {

        CartModel model = context.request().adaptTo(CartModel.class);

        assertNull(model.getProductsHeader());
        assertNull(model.getNameHeader());
        assertNull(model.getPriceHeader());
        assertNull(model.getQuantityHeader());
        assertNull(model.getTotalHeaderTable());
        assertNull(model.getHandleHeader());
    }

    /**
     * Tests checkout link when configured.
     */
    @Test
    void testCheckoutLink_WhenPresent() {

        context.currentResource("/content/test")
                .adaptTo(org.apache.sling.api.resource.ModifiableValueMap.class)
                .put("checkoutLink", "/content/checkout");

        CartModel model = context.request().adaptTo(CartModel.class);

        assertEquals("/content/checkout", model.getCheckoutLink());
    }
}