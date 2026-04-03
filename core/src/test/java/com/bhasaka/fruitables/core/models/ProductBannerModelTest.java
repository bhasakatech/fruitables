package com.bhasaka.fruitables.core.models;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(AemContextExtension.class)
class ProductBannerModelTest {

    private final AemContext context = new AemContext();

    private ProductBannerModel model;

    @BeforeEach
    void setUp() {
        context.addModelsForClasses(ProductBannerModel.class);
    }

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