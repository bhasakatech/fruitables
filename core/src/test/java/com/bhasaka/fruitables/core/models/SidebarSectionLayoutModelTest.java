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
    }

    @Test
    void testAuthoredContentAndConfiguredLists() {
        context.load().json("/sidebar-section-layout.json", "/content/sidebar");
        createTaggedContent("/content/site/products/item-one/jcr:content",
                "/content/cq:tags/fruitables/apples");
        createTaggedContent("/content/site/products/item-two/jcr:content",
                "/content/cq:tags/fruitables/apples",
                "/content/cq:tags/fruitables/banana");
        createProductFragment("/content/dam/fruitables/products/big-banana",
                "Big Banana", "2.99", "kg", "4");
        createProductFragment("/content/dam/fruitables/products/fresh-strawberry",
                "Fresh Strawberry", "3.49", "box", "5");

        SidebarSectionLayoutModel model = adaptModel("/content/sidebar");

        assertAll(
                () -> assertEquals("Fresh fruits shop", model.getSectionTitle()),
                () -> assertEquals("keywords", model.getSearchPlaceholder()),
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
                () -> assertTrue(model.getSortingOptions().get(0).isSelected()),
                () -> assertEquals("nothing", model.getSortingOptions().get(0).getOptionValue()),
                () -> assertEquals(2, model.getAdditionalOptions().size()),
                () -> assertFalse(model.getAdditionalOptions().get(0).isChecked()),
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
                () -> assertEquals(2, model.getFeaturedProducts().size()),
                () -> assertEquals("Big Banana", model.getFeaturedProducts().get(0).getProduct().getProductName()),
                () -> assertEquals("compact", model.getFeaturedProducts().get(0).getCardStyle()),
                () -> assertEquals("Fresh Strawberry", model.getFeaturedProducts().get(1).getProduct().getProductName()),
                () -> assertEquals("standard", model.getFeaturedProducts().get(1).getCardStyle()),
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
    void testDefaultsWhenAuthoringIsMissing() {
        context.create().resource("/content/default-sidebar");

        SidebarSectionLayoutModel model = adaptModel("/content/default-sidebar");

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
    void testFeaturedProductsPreviewAndRemainingSplitAtThreeItems() {
        context.create().resource("/content/sidebar-expanded",
                "featuredProductsTitle", "Featured products",
                "viewMoreLabel", "View More");

        addConfiguredProduct("/content/sidebar-expanded", "item0",
                "/content/dam/fruitables/products/raspberry", "compact");
        addConfiguredProduct("/content/sidebar-expanded", "item1",
                "/content/dam/fruitables/products/banana", "standard");
        addConfiguredProduct("/content/sidebar-expanded", "item2",
                "/content/dam/fruitables/products/apple", "compact");
        addConfiguredProduct("/content/sidebar-expanded", "item3",
                "/content/dam/fruitables/products/orange-group", "standard");

        createProductFragment("/content/dam/fruitables/products/raspberry",
                "Raspberries", "150.0", "kg", "5");
        createProductFragment("/content/dam/fruitables/products/banana",
                "Banana", "60.0", "dozen", "4");
        createProductFragment("/content/dam/fruitables/products/apple",
                "Apple", "180.0", "kg", "4");
        createNestedProductFragment("/content/dam/fruitables/products/orange-group", "orange-item",
                "Orange", "120.0", "kg", "4");

        SidebarSectionLayoutModel model = adaptModel("/content/sidebar-expanded");
        List<ProductItem> preview = model.getFeaturedProductsPreview();
        List<ProductItem> remaining = model.getRemainingFeaturedProducts();

        assertAll(
                () -> assertEquals(4, model.getFeaturedProducts().size()),
                () -> assertEquals(3, preview.size()),
                () -> assertEquals("Raspberries", preview.get(0).getProduct().getProductName()),
                () -> assertEquals("Banana", preview.get(1).getProduct().getProductName()),
                () -> assertEquals("Apple", preview.get(2).getProduct().getProductName()),
                () -> assertEquals(1, remaining.size()),
                () -> assertEquals("Orange", remaining.get(0).getProduct().getProductName()),
                () -> assertTrue(model.isFeaturedProductsExpandable())
        );
    }

    @Test
    void testPriceValuesAreClampedToValidRange() {
        context.create().resource("/content/sidebar-price-bounds",
                "minPrice", 50,
                "maxPrice", 20,
                "selectedPrice", 100);

        SidebarSectionLayoutModel model = adaptModel("/content/sidebar-price-bounds");

        assertAll(
                () -> assertEquals(50, model.getMinPrice()),
                () -> assertEquals(50, model.getMaxPrice()),
                () -> assertEquals(50, model.getSelectedPrice())
        );
    }

    @Test
    void testBannerAltTextFallsBackToMarkupStrippedBannerTitle() {
        context.create().resource("/content/sidebar-banner",
                "bannerTitle", "<strong>Fresh</strong> Fruits <em>Banner</em>");

        SidebarSectionLayoutModel model = adaptModel("/content/sidebar-banner");

        assertEquals("Fresh Fruits Banner", model.getBannerAltText());
    }

    @Test
    void testCategoryCountsAreZeroWhenSearchRootIsMissing() {
        context.create().resource("/content/sidebar-categories",
                "tags", new String[]{
                        "/content/cq:tags/fruitables/apples",
                        "/content/cq:tags/fruitables/oranges"
                });

        SidebarSectionLayoutModel model = adaptModel("/content/sidebar-categories");

        assertAll(
                () -> assertEquals(2, model.getCategories().size()),
                () -> assertEquals("Apples", model.getCategories().get(0).getTitle()),
                () -> assertEquals(0, model.getCategories().get(0).getCount()),
                () -> assertEquals("Oranges", model.getCategories().get(1).getTitle()),
                () -> assertEquals(0, model.getCategories().get(1).getCount())
        );
    }

    @Test
    void testFeaturedProductsSkipBlankAndMissingContentFragments() {
        context.create().resource("/content/sidebar-invalid-products");

        addConfiguredProduct("/content/sidebar-invalid-products", "item0", "", "compact");
        addConfiguredProduct("/content/sidebar-invalid-products", "item1",
                "/content/dam/fruitables/products/missing", "standard");
        addConfiguredProduct("/content/sidebar-invalid-products", "item2",
                "/content/dam/fruitables/products/valid", "compact");

        createProductFragment("/content/dam/fruitables/products/valid",
                "Valid Product", "99.0", "kg", "5");

        SidebarSectionLayoutModel model = adaptModel("/content/sidebar-invalid-products");

        assertAll(
                () -> assertEquals(1, model.getFeaturedProducts().size()),
                () -> assertEquals("Valid Product", model.getFeaturedProducts().get(0).getProduct().getProductName()),
                () -> assertEquals(1, model.getFeaturedProductsPreview().size()),
                () -> assertTrue(model.getRemainingFeaturedProducts().isEmpty())
        );
    }

    private SidebarSectionLayoutModel adaptModel(String resourcePath) {
        Resource resource = context.resourceResolver().getResource(resourcePath);
        assertNotNull(resource);

        SidebarSectionLayoutModel model = resource.adaptTo(SidebarSectionLayoutModel.class);
        assertNotNull(model);
        return model;
    }

    private void addConfiguredProduct(String componentPath, String itemName, String cfPath, String cardStyle) {
        context.create().resource(componentPath + "/products/" + itemName,
                "cfPath", cfPath,
                "cardStyle", cardStyle);
    }

    private void createTaggedContent(String path, String... tags) {
        context.create().resource(path, "cq:tags", tags);
    }

    private void createProductFragment(String fragmentPath, String name, String price, String unit, String rating) {
        context.create().resource(fragmentPath + "/jcr:content/data/master",
                "productName", name,
                "productImage", "/content/dam/fruitables/" + name.toLowerCase().replace(' ', '-') + ".jpg",
                "productPrice", price,
                "unit", unit,
                "productRating", rating);
    }

    private void createNestedProductFragment(String fragmentPath, String childName,
                                             String name, String price, String unit, String rating) {
        context.create().resource(fragmentPath + "/" + childName + "/jcr:content/data/master",
                "productName", name,
                "productImage", "/content/dam/fruitables/" + name.toLowerCase().replace(' ', '-') + ".jpg",
                "productPrice", price,
                "unit", unit,
                "productRating", rating);
    }
}
