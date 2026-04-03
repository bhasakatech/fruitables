package com.bhasaka.fruitables.core.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(AemContextExtension.class)
class ShopBannerModelTest {

    private final AemContext context = new AemContext();

    @BeforeEach
    void setUp() {
        context.addModelsForClasses(ShopBannerModel.class);
        context.load().json("/ShopBannerModelTest.json", "/content");
    }

    @Test
    void testModelWithValues() {
        Resource resource = context.resourceResolver().getResource("/content/shop-detail/jcr:content/root/shop-banner");
        ShopBannerModel model = resource.adaptTo(ShopBannerModel.class);
        assertEquals("Shop Detail", model.getShopBannerTitle());
        assertEquals("Shop Detail", model.getAuthoredShopBannerTitle());
        assertEquals("/content/dam/fruitables/banner/shop-banner.jpg", model.getShopBannerImage());
    }

    @Test
    void testModelFallsBackToPageNameWhenTitleMissing() {
        Resource resource = context.resourceResolver().getResource("/content/category-page/jcr:content/root/shop-banner");
        ShopBannerModel model = resource.adaptTo(ShopBannerModel.class);
        assertEquals("category-page", model.getShopBannerTitle());
        assertNull(model.getAuthoredShopBannerTitle());
        assertNull(model.getShopBannerImage());
    }

    @Test
    void testModelFallsPageTitleIsBlank() {
        Resource resource = context.resourceResolver().getResource("/content/fresh-fruits/jcr:content/root/shop-banner");
        ShopBannerModel model = resource.adaptTo(ShopBannerModel.class);
        assertEquals("fresh-fruits", model.getShopBannerTitle());
        assertEquals("   ", model.getAuthoredShopBannerTitle());
    }

    @Test
    void testModelReturnsNullTitle() {
        Resource resource = context.resourceResolver().getResource("/content/orphan-banner");
        ShopBannerModel model = resource.adaptTo(ShopBannerModel.class);
        assertNull(model.getShopBannerTitle());
        assertNull(model.getAuthoredShopBannerTitle());
        assertEquals("/content/dam/fruitables/banner/orphan-banner.jpg", model.getShopBannerImage());
    }
}
