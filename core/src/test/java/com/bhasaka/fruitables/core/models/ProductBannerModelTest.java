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
 *
 * <p>This test class uses AEM Mocks ({@link AemContext}) to simulate
 * an AEM repository and validate the behavior of the ProductBannerModel.</p>
 *
 * <p>The tests ensure:
 * <ul>
 *     <li>All authored properties are correctly injected into the model</li>
 *     <li>The model handles missing or empty properties gracefully</li>
 *     <li>Model adaptation from {@link Resource} works as expected</li>
 * </ul>
 * </p>
 */
@ExtendWith(AemContextExtension.class)
class ProductBannerModelTest {

    /**
     * AEM mock context used for simulating resource resolution
     * and Sling Model adaptation.
     */
    private final AemContext context = new AemContext();

    /**
     * Instance of {@link ProductBannerModel} under test.
     */
    private ProductBannerModel model;

    /**
     * Sets up the test environment before each test execution.
     *
     * <p>Registers the Sling Model class to enable adaptation
     * from resources.</p>
     */
    @BeforeEach
    void setUp() {
        context.addModelsForClasses(ProductBannerModel.class);
    }

    /**
     * Tests the model when all fields are authored and available.
     *
     * <p>Loads mock JSON content and verifies that all properties
     * are correctly injected into the model.</p>
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
     * Tests the model behavior when no properties are authored.
     *
     * <p>Ensures that all getter methods return {@code null}
     * when properties are not present in the resource.</p>
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