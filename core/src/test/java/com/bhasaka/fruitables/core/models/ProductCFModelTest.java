package com.bhasaka.fruitables.core.models;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test class for {@link ProductCFModel}.
 *
 * This class validates the functionality of the ProductCFModel Sling Model
 * by using AEM Mocks to simulate repository content and resource adaptation.
 */
@ExtendWith(AemContextExtension.class)
class ProductCFModelTest {

    /**
     * AEM context used to mock the JCR repository and resources.
     */
    private final AemContext context = new AemContext(ResourceResolverType.JCR_MOCK);

    /**
     * Instance of the model under test.
     */
    private ProductCFModel model;

    /**
     * Sets up the test environment before each test case.
     *
     * Loads the model class and mock JSON content into the AEM context.
     */
    @BeforeEach
    void setUp() {
        context.addModelsForClasses(ProductCFModel.class);
        context.load().json("/productcfmodel.json", "/content");
    }

    /**
     * Helper method to adapt a resource at the given path to ProductCFModel.
     *
     * @param path the resource path
     * @return adapted ProductCFModel instance
     */
    private ProductCFModel getModel(String path) {
        context.currentResource(path);
        return context.currentResource().adaptTo(ProductCFModel.class);
    }

    /**
     * Tests a valid product scenario.
     *
     * Verifies that all product fields are correctly populated and
     * rating is properly converted into filled and empty stars.
     */
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

    /**
     * Tests behavior when an invalid rating value is provided.
     *
     * Ensures that the model defaults to zero filled stars and
     * maximum empty stars when rating is not valid.
     */
    @Test
    void testInvalidRating() {
        model = getModel("/content/product-invalid");

        assertNotNull(model);
        assertEquals(0, model.getFilledStars().size());
        assertEquals(5, model.getEmptyStars().size());
    }

    /**
     * Tests behavior when the rating is empty or missing.
     *
     * Ensures that the model handles empty values gracefully
     * and defaults to zero filled stars.
     */
    @Test
    void testEmptyRating() {
        model = getModel("/content/product-empty");

        assertNotNull(model);
        assertEquals(0, model.getFilledStars().size());
        assertEquals(5, model.getEmptyStars().size());
    }
}