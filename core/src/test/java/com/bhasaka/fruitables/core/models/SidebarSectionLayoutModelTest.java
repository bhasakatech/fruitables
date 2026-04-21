package com.bhasaka.fruitables.core.models;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * Unit test class for SidebarSectionLayoutModel.
 */
@ExtendWith(AemContextExtension.class)
class SidebarSectionLayoutModelTest {

    private final AemContext context = new AemContext();
    private SidebarSectionLayoutModel model;

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

        mockTagManager(); 
    }

    /**
     * Mock TagManager for category + tag logic
     */
    private void mockTagManager() {

        TagManager tagManager = mock(TagManager.class);

        Tag apples = mock(Tag.class);
        Tag banana = mock(Tag.class);

        //  Register adapter properly
        context.registerAdapter(ResourceResolver.class, TagManager.class, tagManager);

        // Resolve authored tags
        when(tagManager.resolve("/content/cq:tags/fruitables/apples")).thenReturn(apples);
        when(tagManager.resolve("/content/cq:tags/fruitables/banana")).thenReturn(banana);

        // Tag IDs
        when(apples.getTagID()).thenReturn("fruitables:apples");
        when(banana.getTagID()).thenReturn("fruitables:banana");

        // Titles
        when(apples.getTitle()).thenReturn("Apples");
        when(banana.getTitle()).thenReturn("Banana");

        // Paths
        when(apples.getPath()).thenReturn("/content/cq:tags/fruitables/apples");
        when(banana.getPath()).thenReturn("/content/cq:tags/fruitables/banana");
    }   

    @Test
    void testAuthoredSidebarBuildsCategoriesAndFeaturedProducts() {

        model = adaptModel("/content/sidebar/authored");

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
                () -> assertTrue(model.getSortingOptions().get(0).isSelected()),
                () -> assertEquals(2, model.getAdditionalOptions().size()),
                () -> assertEquals("Organic", model.getAdditionalOptions().get(0).getLabel()),
                () -> assertFalse(model.getAdditionalOptions().get(0).isChecked())
        );
    }

    @Test
    void testAuthoredSidebar() {

        model = adaptModel("/content/sidebar/authored");

        assertAll(
                () -> assertEquals(2, model.getCategories().size()),
                () -> assertEquals("Apples", model.getCategories().get(0).getTitle()),
                () -> assertEquals(2, model.getCategories().get(0).getCount()),
                () -> assertEquals("Banana", model.getCategories().get(1).getTitle()),
                () -> assertEquals(1, model.getCategories().get(1).getCount())
        );
    }

    @Test
    void testDefaultsAreAppliedWhenPropertiesAreMissing() {

        model = adaptModel("/content/sidebar/defaults");

        assertAll(
                () -> assertEquals("Fresh fruits shop", model.getSectionTitle()),
                () -> assertEquals("keywords", model.getSearchPlaceholder()),
                () -> assertEquals("Default Sorting:", model.getSortingLabel()),
                () -> assertEquals("Categories", model.getCategoriesTitle()),
                () -> assertEquals("Price", model.getPriceTitle()),
                () -> assertEquals("Additional", model.getAdditionalTitle()),
                () -> assertEquals("Featured products", model.getFeaturedProductsTitle()),
                () -> assertEquals("View More", model.getViewMoreLabel()),
                () -> assertEquals("#", model.getViewMoreLink())
        );
    }

    @Test
    void testFeaturedProductsPreviewSplitsAfterThreeItems() {

        model = adaptModel("/content/sidebar/expanded");

        List<ProductItem> preview = model.getFeaturedProductsPreview();
        List<ProductItem> remaining = model.getRemainingFeaturedProducts();

        assertAll(
                () -> assertEquals(4, model.getFeaturedProducts().size()),
                () -> assertEquals(3, preview.size()),
                () -> assertEquals(1, remaining.size()),
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

    private SidebarSectionLayoutModel adaptModel(String path) {
        Resource resource = context.resourceResolver().getResource(path);
        assertNotNull(resource);
        SidebarSectionLayoutModel m = resource.adaptTo(SidebarSectionLayoutModel.class);
        assertNotNull(m);
        return m;
    }
}
