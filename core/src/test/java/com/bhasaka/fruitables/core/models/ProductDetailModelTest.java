package com.bhasaka.fruitables.core.models;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.request.RequestPathInfo;
import org.apache.sling.api.resource.ResourceResolver;
import org.junit.jupiter.api.Test;
import java.lang.reflect.Field;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
class ProductDetailModelTest {
    static class TestModel extends ProductDetailModel {
        ProductCFModel productToReturn;
        @Override
        protected ProductCFModel fetchProduct(String slug) {
            return productToReturn;
        }
    }
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
    @Test
    void testProductNull() {
        ProductDetailModel model = new ProductDetailModel();
        assertTrue(model.getReviewFilledStars().isEmpty());
        assertTrue(model.getReviewEmptyStars().isEmpty());
    }
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