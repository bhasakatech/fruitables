package com.bhasaka.fruitables.core.servlets;

import io.wcm.testing.mock.aem.junit5.AemContext;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.servlet.http.HttpServletResponse;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class AddToCartServletTest {

    private final AemContext context = new AemContext();
    private AddToCartServlet servlet;

    @BeforeEach
    void setUp() throws Exception {
        servlet = new AddToCartServlet();
        ResourceResolverFactory factoryMock = mock(ResourceResolverFactory.class);
        ResourceResolver resolver = context.resourceResolver();
        when(factoryMock.getServiceResourceResolver(anyMap())).thenReturn(resolver);
        servlet.factory = factoryMock;

        context.create().resource("/content/usergenerated");
    }

    @Test
    void testAddItemToCartCreatesNewCartAndItem() throws Exception {
        context.request().setParameterMap(Collections.singletonMap("productId", "/content/products/apple"));

        servlet.doPost(context.request(), context.response());

        String sessionId = context.request().getSession().getId();
        Resource userCart = context.resourceResolver().getResource("/content/usergenerated/cart/" + sessionId);
        assertNotNull(userCart, "User cart should be created");

        Resource item = context.resourceResolver().getResource(userCart.getPath() + "/apple");
        assertNotNull(item, "Cart item should be created");

        Integer quantity = item.getValueMap().get("quantity", Integer.class);
        assertEquals(1, quantity, "Quantity should be 1 for first addition");
    }

    @Test
    void testAddItemIncrementsQuantity() throws Exception {
        context.request().setParameterMap(Collections.singletonMap("productId", "/content/products/apple"));
        servlet.doPost(context.request(), context.response());
        servlet.doPost(context.request(), context.response());

        String sessionId = context.request().getSession().getId();
        Resource item = context.resourceResolver().getResource("/content/usergenerated/cart/" + sessionId + "/apple");
        assertEquals(2, item.getValueMap().get("quantity", Integer.class));
    }

    @Test
    void testMissingProductIdReturnsBadRequest() throws Exception {
        context.request().setParameterMap(Collections.emptyMap());
        servlet.doPost(context.request(), context.response());

        assertEquals(HttpServletResponse.SC_BAD_REQUEST, context.response().getStatus());
    }
}