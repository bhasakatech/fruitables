package com.bhasaka.fruitables.core.models;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test class for {@link ProductBannerModel}.
 * <p>
 * This class uses AEM Mocks to simulate repository content and verify
 * that the ProductBannerModel correctly maps resource properties.
 * </p>
 */
@ExtendWith(AemContextExtension.class)
class ProductBannerModelTest {

    /**
     * AEM mock context for simulating Sling and JCR environment.
     */
    private final AemContext context = new AemContext();

    /**
     * Instance of ProductBannerModel under test.
     */
    private ProductBannerModel model;

    /**
     * Sets up the test environment before each test case.
     * Registers the model class with the AEM context.
     */
    @BeforeEach
    void setUp() {
        context.addModelsForClasses(ProductBannerModel.class);
    }

    /**
     * Tests model adaptation when all properties are present.
     * <p>
     * Verifies that all getter methods return expected values
     * from the loaded JSON content.
     * </p>
     */
    @Test
    void testModelWithAllFields() {
        context.load().json("/product-banner.json", "/content/test");

        Resource resource = context.resourceResolver().getResource("/content/test");
        model = resource.adaptTo(ProductBannerModel.class);

        assertNotNull(model);

        assertEquals("Fresh Exotic Fruits", model.getHeading());
        assertEquals("in Our Store", model.getSubheading());
        assertEquals("Test Description", model.getDescription());
        assertEquals("BUY", model.getButtonLabel());
        assertEquals("/content/test-page", model.getButtonLink());
        assertEquals("/content/dam/test/apple.png", model.getProductImage());

        assertEquals("1", model.getMainPrice());
        assertEquals("50", model.getDecimalPrice());
        assertEquals("$", model.getCurrency());
        assertEquals("kg", model.getUnit());
    }

    /**
     * Tests model adaptation when resource has no properties.
     * <p>
     * Ensures that the model is created successfully but all
     * getter methods return null values.
     * </p>
     */
    @Test
    void testModelWithEmptyFields() {
        context.create().resource("/content/test2");

        Resource resource = context.resourceResolver().getResource("/content/test2");
        model = resource.adaptTo(ProductBannerModel.class);

        assertNotNull(model);

        assertNull(model.getHeading());
        assertNull(model.getSubheading());
        assertNull(model.getDescription());
        assertNull(model.getButtonLabel());
        assertNull(model.getButtonLink());
        assertNull(model.getProductImage());

        assertNull(model.getMainPrice());
        assertNull(model.getDecimalPrice());
        assertNull(model.getCurrency());
        assertNull(model.getUnit());
    }
}