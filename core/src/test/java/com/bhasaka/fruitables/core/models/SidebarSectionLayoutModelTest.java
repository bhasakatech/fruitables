package com.bhasaka.fruitables.core.models;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(AemContextExtension.class)
class SidebarSectionLayoutModelTest {

    private final AemContext context = new AemContext();

    @BeforeEach
    void setUp() {
        context.addModelsForClasses(
                SidebarSectionLayoutModel.class,
                SidebarSectionLayoutModel.SortingOption.class,
                SidebarSectionLayoutModel.AdditionalOption.class,
                ProductCFModel.class,
                ProductResource.class
        );
        context.load().json("/sidebarSectionLayoutModelTest.json", "/content");
    }

    @Test
    void testAuthoredSidebarBuildsCategoriesAndFeaturedProducts() {
        SidebarSectionLayoutModel model = adaptModel("/content/sidebar/authored");
        List<ProductItem> featuredProducts = model.getFeaturedProducts();

        assertAll(
                () -> assertEquals("Fresh fruits shop", model.getSectionTitle()),
                () -> assertEquals("keywords", model.getSearchPlaceholder()),
                () -> assertEquals("/content/dam/fruitables/search.svg", model.getSearchImage()),
                () -> assertEquals("Default Sorting:", model.getSortingLabel()),
                () -> assertEquals("Categories", model.getCategoriesTitle()),
                () -> assertEquals("Price", model.getPriceTitle()),
                () -> assertEquals("Additional", model.getAdditionalTitle()),
                () -> assertEquals("Featured products", model.getFeaturedProductsTitle()),
                () -> assertEquals("View More", model.getViewMoreLabel()),
                () -> assertEquals("/shop/products", model.getViewMoreLink()),
                () -> assertEquals("/content/dam/fruitables/banner-fruits.jpg", model.getBannerImage()),
                () -> assertEquals("Fresh Fruits Banner", model.getBannerAltText()),
                () -> assertEquals("Fresh Fruits Banner", model.getBannerTitle())
        );

        assertAll(
                () -> assertEquals(2, model.getSortingOptions().size()),
                () -> assertEquals("Nothing", model.getSortingOptions().get(0).getOptionText()),
                () -> assertEquals("nothing", model.getSortingOptions().get(0).getOptionValue()),
                () -> assertTrue(model.getSortingOptions().get(0).isSelected()),
                () -> assertEquals(2, model.getAdditionalOptions().size()),
                () -> assertEquals("Organic", model.getAdditionalOptions().get(0).getLabel()),
                () -> assertFalse(model.getAdditionalOptions().get(0).isChecked()),
                () -> assertEquals("Expired", model.getAdditionalOptions().get(1).getLabel()),
                () -> assertTrue(model.getAdditionalOptions().get(1).isChecked())
        );

        assertAll(
                () -> assertEquals(2, model.getCategories().size()),
                () -> assertEquals("Apples", model.getCategories().get(0).getTitle()),
                () -> assertEquals(2, model.getCategories().get(0).getCount()),
                () -> assertEquals("Banana", model.getCategories().get(1).getTitle()),
                () -> assertEquals(1, model.getCategories().get(1).getCount())
        );

        assertAll(
                () -> assertEquals(2, featuredProducts.size()),
                () -> assertEquals("Big Banana", featuredProducts.get(0).getProduct().getProductName()),
                () -> assertEquals("compact", featuredProducts.get(0).getCardStyle()),
                () -> assertEquals("/content/dam/fruitables/products/big-banana", featuredProducts.get(0).getProductPath()),
                () -> assertEquals("Fresh Strawberry", featuredProducts.get(1).getProduct().getProductName()),
                () -> assertEquals("standard", featuredProducts.get(1).getCardStyle()),
                () -> assertEquals(2, model.getFeaturedProductsPreview().size()),
                () -> assertTrue(model.getRemainingFeaturedProducts().isEmpty()),
                () -> assertFalse(model.isFeaturedProductsExpandable())
        );

        assertAll(
                () -> assertEquals(0, model.getMinPrice()),
                () -> assertEquals(500, model.getMaxPrice()),
                () -> assertEquals(112, model.getSelectedPrice()),
                () -> assertTrue(model.getSearchIconId().startsWith(model.getDomId())),
                () -> assertTrue(model.getSortingId().startsWith(model.getDomId())),
                () -> assertTrue(model.getFeaturedProductsListId().startsWith(model.getDomId()))
        );
    }

    @Test
    void testDefaultsAreAppliedWhenPropertiesAreMissing() {
        SidebarSectionLayoutModel model = adaptModel("/content/sidebar/defaults");

        assertAll(
                () -> assertEquals("Fresh fruits shop", model.getSectionTitle()),
                () -> assertEquals("keywords", model.getSearchPlaceholder()),
                () -> assertEquals("Default Sorting:", model.getSortingLabel()),
                () -> assertEquals("Categories", model.getCategoriesTitle()),
                () -> assertEquals("Price", model.getPriceTitle()),
                () -> assertEquals("Additional", model.getAdditionalTitle()),
                () -> assertEquals("Featured products", model.getFeaturedProductsTitle()),
                () -> assertEquals("View More", model.getViewMoreLabel()),
                () -> assertEquals("#", model.getViewMoreLink()),
                () -> assertEquals("Fresh Fruits Banner", model.getBannerAltText()),
                () -> assertEquals("Fresh Fruits Banner", model.getBannerTitle())
        );

        assertAll(
                () -> assertEquals(4, model.getSortingOptions().size()),
                () -> assertEquals("Nothing", model.getSortingOptions().get(0).getOptionText()),
                () -> assertTrue(model.getSortingOptions().get(0).isSelected()),
                () -> assertEquals(5, model.getAdditionalOptions().size()),
                () -> assertEquals("Expired", model.getAdditionalOptions().get(4).getLabel()),
                () -> assertTrue(model.getAdditionalOptions().get(4).isChecked())
        );

        assertAll(
                () -> assertTrue(model.getCategories().isEmpty()),
                () -> assertTrue(model.getFeaturedProducts().isEmpty()),
                () -> assertTrue(model.getFeaturedProductsPreview().isEmpty()),
                () -> assertTrue(model.getRemainingFeaturedProducts().isEmpty()),
                () -> assertFalse(model.isFeaturedProductsExpandable()),
                () -> assertEquals(0, model.getMinPrice()),
                () -> assertEquals(500, model.getMaxPrice()),
                () -> assertEquals(0, model.getSelectedPrice())
        );
    }

    @Test
    void testFeaturedProductsPreviewSplitsAfterThreeItems() {
        SidebarSectionLayoutModel model = adaptModel("/content/sidebar/expanded");
        List<ProductItem> preview = model.getFeaturedProductsPreview();
        List<ProductItem> remaining = model.getRemainingFeaturedProducts();

        assertAll(
                () -> assertEquals(4, model.getFeaturedProducts().size()),
                () -> assertEquals(3, preview.size()),
                () -> assertEquals("Apple", preview.get(0).getProduct().getProductName()),
                () -> assertEquals("Banana", preview.get(1).getProduct().getProductName()),
                () -> assertEquals("Orange", preview.get(2).getProduct().getProductName()),
                () -> assertEquals(1, remaining.size()),
                () -> assertEquals("Raspberries", remaining.get(0).getProduct().getProductName()),
                () -> assertTrue(model.isFeaturedProductsExpandable())
        );
    }

    @Test
    void testPriceAndBannerFallbacksAreNormalized() {
        SidebarSectionLayoutModel priceModel = adaptModel("/content/sidebar/price-bounds");
        SidebarSectionLayoutModel bannerModel = adaptModel("/content/sidebar/banner");

        assertAll(
                () -> assertEquals(50, priceModel.getMinPrice()),
                () -> assertEquals(50, priceModel.getMaxPrice()),
                () -> assertEquals(50, priceModel.getSelectedPrice()),
                () -> assertEquals("Fresh Fruits Banner", bannerModel.getBannerAltText())
        );
    }

    private SidebarSectionLayoutModel adaptModel(String resourcePath) {
        Resource resource = context.resourceResolver().getResource(resourcePath);
        assertNotNull(resource, "Resource not found: " + resourcePath);

        SidebarSectionLayoutModel model = resource.adaptTo(SidebarSectionLayoutModel.class);
        assertNotNull(model);
        return model;
    }
}
