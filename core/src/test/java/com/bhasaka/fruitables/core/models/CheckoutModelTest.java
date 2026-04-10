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

@ExtendWith(AemContextExtension.class)
class CheckoutModelTest {

    private final AemContext context = new AemContext();
    private CheckoutModel checkoutModel;
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

    @Test
    void testPickupShipping() {
        assertEquals(20.0, checkoutModel.getShipping(),
                "Shipping should be pickupRate when shipping=pickup");
    }

    @Test
    void testSubtotal() {
        assertEquals(100.0, checkoutModel.getSubtotal(),
                "Subtotal should come from CartModel");
    }

    @Test
    void testTotalCalculation() {
        assertEquals(120.0, checkoutModel.getTotal(),
                "Total should be subtotal + shipping");
    }

    @Test
    void testItemsNotNull() {
        assertNotNull(checkoutModel.getItems(),
                "Items list should not be null");
    }

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