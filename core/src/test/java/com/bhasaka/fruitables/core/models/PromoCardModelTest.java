package com.bhasaka.fruitables.core.models;


import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(AemContextExtension.class)
class PromoCardModelTest {

    private final AemContext context = new AemContext();

    @BeforeEach
    void setUp() {
        // Register model class
        context.addModelsForClasses(PromoCardModel.class);

        // Load JSON into repository
        context.load().json("/promo-card.json", "/content");

        // Set current resource
        context.currentResource("/content/banner/cards/item0");
    }

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