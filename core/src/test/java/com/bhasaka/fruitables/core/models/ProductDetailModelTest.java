package com.bhasaka.fruitables.core.models;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.request.RequestPathInfo;
import org.apache.sling.api.resource.ResourceResolver;
import org.junit.jupiter.api.Test;

import java.lang.reflect.Field;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit test class for {@link ProductDetailModel}.
 * <p>
 * This class verifies:
 * <ul>
 *     <li>Initialization logic based on request selectors.</li>
 *     <li>Fallback behavior when product is not found.</li>
 *     <li>Star rating calculations (filled and empty stars).</li>
 *     <li>Getter methods functionality.</li>
 * </ul>
 * Mockito is used for mocking dependencies like request and product model.
 * </p>
 */
class ProductDetailModelTest {

    /**
     * Test-specific subclass to override product fetching logic.
     */
    static class TestModel extends ProductDetailModel {

        /**
         * Mock product to return during testing.
         */
        ProductCFModel productToReturn;

        /**
         * Overrides fetchProduct to return a controlled mock product.
         *
         * @param slug product slug
         * @return mocked ProductCFModel
         */
        @Override
        protected ProductCFModel fetchProduct(String slug) {
            return productToReturn;
        }
    }

    /**
     * Utility method to inject mocked request and resolver into the model.
     *
     * @param request mocked SlingHttpServletRequest
     * @param resolver mocked ResourceResolver
     * @param product mocked ProductCFModel
     * @return initialized TestModel
     * @throws Exception reflection-related exceptions
     */
    private TestModel setup(SlingHttpServletRequest request,
                            ResourceResolver resolver,
                            ProductCFModel product) throws Exception {

        TestModel model = new TestModel();
        model.productToReturn = product;

        Field req = ProductDetailModel.class.getDeclaredField("request");
        req.setAccessible(true);
        req.set(model, request);

        Field res = ProductDetailModel.class.getDeclaredField("resolver");
        res.setAccessible(true);
        res.set(model, resolver);

        return model;
    }

    /**
     * Tests initialization with a valid selector.
     * Expected: product should be set correctly.
     */
    @Test
    void testInit_validSelector() throws Exception {
        SlingHttpServletRequest request = mock(SlingHttpServletRequest.class);
        ResourceResolver resolver = mock(ResourceResolver.class);
        RequestPathInfo pathInfo = mock(RequestPathInfo.class);

        when(request.getRequestPathInfo()).thenReturn(pathInfo);
        when(pathInfo.getSelectors()).thenReturn(new String[]{"Apple"});

        ProductCFModel product = mock(ProductCFModel.class);

        TestModel model = setup(request, resolver, product);
        model.init();

        assertEquals(product, model.getProduct());
    }

    /**
     * Tests initialization when selectors are null.
     * Expected: default fallback product is used.
     */
    @Test
    void testInit_selectorsNull() throws Exception {
        SlingHttpServletRequest request = mock(SlingHttpServletRequest.class);
        ResourceResolver resolver = mock(ResourceResolver.class);
        RequestPathInfo pathInfo = mock(RequestPathInfo.class);

        when(request.getRequestPathInfo()).thenReturn(pathInfo);
        when(pathInfo.getSelectors()).thenReturn(null);

        ProductCFModel product = mock(ProductCFModel.class);

        TestModel model = setup(request, resolver, product);
        model.init();

        assertEquals(product, model.getProduct());
    }

    /**
     * Tests initialization when selectors array is empty.
     * Expected: default fallback product is used.
     */
    @Test
    void testInit_selectorsEmptyArray() throws Exception {
        SlingHttpServletRequest request = mock(SlingHttpServletRequest.class);
        ResourceResolver resolver = mock(ResourceResolver.class);
        RequestPathInfo pathInfo = mock(RequestPathInfo.class);

        when(request.getRequestPathInfo()).thenReturn(pathInfo);
        when(pathInfo.getSelectors()).thenReturn(new String[]{});

        ProductCFModel product = mock(ProductCFModel.class);

        TestModel model = setup(request, resolver, product);
        model.init();

        assertEquals(product, model.getProduct());
    }

    /**
     * Tests initialization when selector is an empty string.
     * Expected: default fallback product is used.
     */
    @Test
    void testInit_selectorEmptyString() throws Exception {
        SlingHttpServletRequest request = mock(SlingHttpServletRequest.class);
        ResourceResolver resolver = mock(ResourceResolver.class);
        RequestPathInfo pathInfo = mock(RequestPathInfo.class);

        when(request.getRequestPathInfo()).thenReturn(pathInfo);
        when(pathInfo.getSelectors()).thenReturn(new String[]{""});

        ProductCFModel product = mock(ProductCFModel.class);

        TestModel model = setup(request, resolver, product);
        model.init();

        assertEquals(product, model.getProduct());
    }

    /**
     * Tests initialization when selector contains null.
     * Expected: default fallback product is used.
     */
    @Test
    void testInit_selectorContainsNull() throws Exception {
        SlingHttpServletRequest request = mock(SlingHttpServletRequest.class);
        ResourceResolver resolver = mock(ResourceResolver.class);
        RequestPathInfo pathInfo = mock(RequestPathInfo.class);

        when(request.getRequestPathInfo()).thenReturn(pathInfo);
        when(pathInfo.getSelectors()).thenReturn(new String[]{null});

        ProductCFModel product = mock(ProductCFModel.class);

        TestModel model = setup(request, resolver, product);
        model.init();

        assertEquals(product, model.getProduct());
    }

    /**
     * Tests fallback logic when first fetch returns null.
     * Expected: second fetch (fallback) returns a valid product.
     */
    @Test
    void testInit_fallback() throws Exception {
        SlingHttpServletRequest request = mock(SlingHttpServletRequest.class);
        ResourceResolver resolver = mock(ResourceResolver.class);
        RequestPathInfo pathInfo = mock(RequestPathInfo.class);

        when(request.getRequestPathInfo()).thenReturn(pathInfo);
        when(pathInfo.getSelectors()).thenReturn(new String[]{"apple"});

        TestModel model = new TestModel() {
            int call = 0;

            @Override
            protected ProductCFModel fetchProduct(String slug) {
                call++;
                if (call == 1) return null;
                return mock(ProductCFModel.class);
            }
        };

        Field req = ProductDetailModel.class.getDeclaredField("request");
        req.setAccessible(true);
        req.set(model, request);

        Field res = ProductDetailModel.class.getDeclaredField("resolver");
        res.setAccessible(true);
        res.set(model, resolver);

        model.init();

        assertNotNull(model.getProduct());
    }

    /**
     * Tests generation of filled stars based on rating.
     */
    @Test
    void testFilledStars() throws Exception {
        ProductDetailModel model = new ProductDetailModel();
        ProductCFModel product = mock(ProductCFModel.class);

        when(product.getRating()).thenReturn(4);

        Field f = ProductDetailModel.class.getDeclaredField("product");
        f.setAccessible(true);
        f.set(model, product);

        List<Integer> stars = model.getReviewFilledStars();

        assertEquals(4, stars.size());
    }

    /**
     * Tests generation of empty stars based on rating.
     */
    @Test
    void testEmptyStars() throws Exception {
        ProductDetailModel model = new ProductDetailModel();
        ProductCFModel product = mock(ProductCFModel.class);

        when(product.getRating()).thenReturn(2);

        Field f = ProductDetailModel.class.getDeclaredField("product");
        f.setAccessible(true);
        f.set(model, product);

        List<Integer> stars = model.getReviewEmptyStars();

        assertEquals(3, stars.size());
    }

    /**
     * Tests behavior when rating is zero.
     */
    @Test
    void testRatingZero() throws Exception {
        ProductDetailModel model = new ProductDetailModel();
        ProductCFModel product = mock(ProductCFModel.class);

        when(product.getRating()).thenReturn(0);

        Field f = ProductDetailModel.class.getDeclaredField("product");
        f.setAccessible(true);
        f.set(model, product);

        assertEquals(0, model.getReviewFilledStars().size());
        assertEquals(5, model.getReviewEmptyStars().size());
    }

    /**
     * Tests behavior when product is null.
     */
    @Test
    void testProductNull() {
        ProductDetailModel model = new ProductDetailModel();

        assertTrue(model.getReviewFilledStars().isEmpty());
        assertTrue(model.getReviewEmptyStars().isEmpty());
    }

    /**
     * Tests getter for product.
     */
    @Test
    void testGetProduct() throws Exception {
        ProductDetailModel model = new ProductDetailModel();
        ProductCFModel product = mock(ProductCFModel.class);

        Field f = ProductDetailModel.class.getDeclaredField("product");
        f.setAccessible(true);
        f.set(model, product);

        assertEquals(product, model.getProduct());
    }
}