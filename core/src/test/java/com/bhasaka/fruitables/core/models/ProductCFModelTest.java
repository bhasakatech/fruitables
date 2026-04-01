package com.bhasaka.fruitables.core.models;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(AemContextExtension.class)
class ProductCFModelTest {

    private final AemContext context = new AemContext(ResourceResolverType.JCR_MOCK);
    private ProductCFModel model;

    @BeforeEach
    void setUp() {
        context.addModelsForClasses(ProductCFModel.class);
        context.load().json("/productcfmodel.json", "/content");
    }

    private ProductCFModel getModel(String path) {
        context.currentResource(path);
        return context.currentResource().adaptTo(ProductCFModel.class);
    }

    @Test
    void testValidProduct() {
        model = getModel("/content/product-valid");

        assertNotNull(model);
        assertEquals("Tomato", model.getProductName());
        assertEquals("Fresh Tomato", model.getProductDescription());
        assertEquals("Vegetables", model.getProductCategory());
        assertEquals("/content/dam/tomato.png", model.getProductImage());
        assertEquals("30", model.getProductPrice());
        assertEquals("kg", model.getUnit());
        assertEquals("4.2", model.getProductRating());
        assertEquals(4, model.getFilledStars().size());
        assertEquals(1, model.getEmptyStars().size());
    }

    @Test
    void testInvalidRating() {
        model = getModel("/content/product-invalid");

        assertNotNull(model);
        assertEquals(0, model.getFilledStars().size());
        assertEquals(5, model.getEmptyStars().size());
    }

    @Test
    void testEmptyRating() {
        model = getModel("/content/product-empty");

        assertNotNull(model);
        assertEquals(0, model.getFilledStars().size());
        assertEquals(5, model.getEmptyStars().size());
    }
}
