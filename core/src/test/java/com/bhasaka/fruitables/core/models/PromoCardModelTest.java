package com.bhasaka.fruitables.core.models;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test class for {@link PromoCardModel}.
 * <p>
 * This class uses AEM Mocks (AemContext) to simulate the JCR repository
 * and test the Sling Model adaptation and property mapping.
 * </p>
 */
@ExtendWith(AemContextExtension.class)
class PromoCardModelTest {

    /**
     * AEM mock context used for testing Sling Models and repository structure.
     */
    private final AemContext context = new AemContext();

    /**
     * Sets up the test environment before each test case.
     * <p>
     * This includes:
     * <ul>
     *     <li>Registering the model class.</li>
     *     <li>Loading JSON test content into the mock repository.</li>
     *     <li>Setting the current resource for adaptation.</li>
     * </ul>
     * </p>
     */
    @BeforeEach
    void setUp() {
        context.addModelsForClasses(PromoCardModel.class);
        context.load().json("/promo-card.json", "/content");
        context.currentResource("/content/banner/cards/item0");
    }

    /**
     * Tests successful adaptation of resource to PromoCardModel
     * and verifies all getter methods return expected values.
     */
    @Test
    void testPromoCardModelGetters() {
        PromoCardModel model = context.currentResource().adaptTo(PromoCardModel.class);
        assertNotNull(model);
        assertEquals("image1.jpg", model.getImage());
        assertEquals("Fresh Fruits", model.getSubtitle());
        assertEquals("20% OFF", model.getOffer());
        assertEquals("dark", model.getOverlayStyle());
        assertEquals("#ffffff", model.getBgColor());
    }

    /**
     * Tests behavior when resource has no properties.
     * <p>
     * Ensures that model adapts successfully but returns null
     * for all fields when properties are missing.
     * </p>
     */
    @Test
    void testNullCase() {
        Resource emptyResource = context.create().resource("/content/empty");
        PromoCardModel emptyModel = emptyResource.adaptTo(PromoCardModel.class);

        assertNotNull(emptyModel);
        assertNull(emptyModel.getImage());
        assertNull(emptyModel.getSubtitle());
        assertNull(emptyModel.getOffer());
        assertNull(emptyModel.getOverlayStyle());
        assertNull(emptyModel.getBgColor());
    }
}