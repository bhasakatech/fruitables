package com.bhasaka.fruitables.core.models;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(AemContextExtension.class)
class PromoBannerModelTest {

    private final AemContext context = new AemContext();

    @BeforeEach
    void setUp() {
        // ✅ Register models
        context.addModelsForClasses(PromoBannerModel.class, PromoCardModel.class);

        // ✅ Load JSON
        context.load().json("/promo-banner.json", "/content");

        // ✅ Set current resource
        context.currentResource("/content/banner");
    }

    @Test
    void testCardsNotNull() {
        PromoBannerModel model = context.currentResource().adaptTo(PromoBannerModel.class);

        assertNotNull(model);
        assertNotNull(model.getCards());
    }

    @Test
    void testCardsSize() {
        PromoBannerModel model = context.currentResource().adaptTo(PromoBannerModel.class);

        List<PromoCardModel> cards = model.getCards();
        assertEquals(2, cards.size());
    }

    @Test
    void testCardValues() {
        PromoBannerModel model = context.currentResource().adaptTo(PromoBannerModel.class);

        PromoCardModel card = model.getCards().get(0);

        assertEquals("image1.jpg", card.getImage());
        assertEquals("Fresh Fruits", card.getSubtitle());
        assertEquals("20% OFF", card.getOffer());
        assertEquals("dark", card.getOverlayStyle());
        assertEquals("#ffffff", card.getBgColor());
    }
}
