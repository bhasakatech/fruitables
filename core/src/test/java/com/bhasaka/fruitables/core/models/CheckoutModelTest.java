package com.bhasaka.fruitables.core.models;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.SlingHttpServletRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit test class for {@link CheckoutModel}.
 *
 * <p>This class verifies:
 * <ul>
 *     <li>Shipping calculation based on request parameter</li>
 *     <li>Subtotal retrieval from {@link CartModel}</li>
 *     <li>Total calculation logic</li>
 *     <li>Cart items handling</li>
 * </ul>
 * </p>
 */
@ExtendWith(AemContextExtension.class)
class CheckoutModelTest {

    private final AemContext context = new AemContext();
    private CheckoutModel checkoutModel;

    /**
     * Sets up test context before each test.
     *
     * <p>Loads mock data, sets request parameters,
     * and registers a mocked {@link CartModel}.</p>
     */
    @BeforeEach
    void setUp() {

        context.addModelsForClasses(CheckoutModel.class, CartModel.class);
        context.load().json("/checkout.json", "/content/checkout");

        context.currentResource("/content/checkout/jcr:content");

        context.request().setParameterMap(
                java.util.Collections.singletonMap("shipping", "pickup")
        );

        CartModel mockCart = mock(CartModel.class);
        when(mockCart.getSubtotal()).thenReturn(100.0);
        when(mockCart.getItems()).thenReturn(java.util.Collections.emptyList());

        context.registerAdapter(SlingHttpServletRequest.class, CartModel.class, mockCart);

        checkoutModel = context.request().adaptTo(CheckoutModel.class);

        assertNotNull(checkoutModel);
    }

    /**
     * Tests pickup shipping calculation.
     */
    @Test
    void testPickupShipping() {
        assertEquals(20.0, checkoutModel.getShipping(),
                "Shipping should be pickupRate when shipping=pickup");
    }

    /**
     * Tests subtotal retrieval from CartModel.
     */
    @Test
    void testSubtotal() {
        assertEquals(100.0, checkoutModel.getSubtotal(),
                "Subtotal should come from CartModel");
    }

    /**
     * Tests total calculation (subtotal + shipping).
     */
    @Test
    void testTotalCalculation() {
        assertEquals(120.0, checkoutModel.getTotal(),
                "Total should be subtotal + shipping");
    }

    /**
     * Tests that items list is not null.
     */
    @Test
    void testItemsNotNull() {
        assertNotNull(checkoutModel.getItems(),
                "Items list should not be null");
    }

    /**
     * Tests flat rate shipping when delivery is selected.
     */
    @Test
    void testFlatRateShipping() {
        context.request().setParameterMap(
                Collections.singletonMap("shipping", "delivery")
        );

        CheckoutModel model = context.request().adaptTo(CheckoutModel.class);

        assertEquals(50.0, model.getShipping(),
                "Shipping should be flatRate for delivery");
    }
}