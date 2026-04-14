package com.bhasaka.fruitables.core.models;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test class for {@link ProductListModel}.
 *
 * This class validates the behavior of the ProductListModel Sling Model,
 * including section details, product aggregation, filtering logic,
 * and category extraction using AEM mock context.
 */
@ExtendWith(AemContextExtension.class)
class ProductListModelTest {

    /**
     * AEM context used to simulate repository structure and resources.
     */
    private final AemContext context = new AemContext(ResourceResolverType.JCR_MOCK);

    /**
     * Instance of the model under test.
     */
    private ProductListModel model;

    /**
     * Initializes the test setup before each test case.
     *
     * Loads required models and mock JSON data, and adapts the component
     * resource to ProductListModel.
     */
    @BeforeEach
    void setUp() {
        context.addModelsForClasses(ProductListModel.class, ProductCFModel.class, ProductResource.class);
        context.load().json("/productlistmodel.json", "/content");

        context.currentResource("/content/component");
        model = context.currentResource().adaptTo(ProductListModel.class);
    }

    /**
     * Tests whether section-level details are correctly populated.
     */
    @Test
    void testSectionDetails() {
        assertNotNull(model);
        assertEquals("Fresh Vegetables", model.getSectionTitle());
        assertEquals("Organic and fresh items", model.getSectionDescription());
        assertEquals("Add to Cart", model.getCartButtonText());
    }

    /**
     * Tests whether only valid product items are included in the list.
     */
    @Test
    void testItemsSize() {
        assertNotNull(model.getItems());
        assertEquals(4, model.getItems().size(), "Only valid products should be added");
    }

    /**
     * Verifies that expected product values are present in the items list.
     */
    @Test
    void testItemValues() {
        List<ProductItem> items = model.getItems();

        assertTrue(items.stream().anyMatch(i -> "Tomato".equals(i.getProduct().getProductName())));
        assertTrue(items.stream().anyMatch(i -> "Potato".equals(i.getProduct().getProductName())));
        assertTrue(items.stream().anyMatch(i -> "Carrot".equals(i.getProduct().getProductName())));
        assertTrue(items.stream().anyMatch(i -> "Onion".equals(i.getProduct().getProductName())));
    }

    /**
     * Tests handling of products with direct master nodes.
     */
    @Test
    void testDirectMasterNode() {
        boolean found = model.getItems().stream()
                .anyMatch(item -> "Tomato".equals(item.getProduct().getProductName()));

        assertTrue(found, "Direct master node should be processed");
    }

    /**
     * Tests handling of products with child master nodes.
     */
    @Test
    void testChildMasterNode() {
        boolean found = model.getItems().stream()
                .anyMatch(item -> "Onion".equals(item.getProduct().getProductName()));

        assertTrue(found, "Child master node should be processed");
    }

    /**
     * Ensures that products without a master node are excluded.
     */
    @Test
    void testNoMasterNode() {
        boolean found = model.getItems().stream()
                .anyMatch(item -> "no-master".equals(item.getProduct().getProductName()));

        assertFalse(found, "Products without master should not be added");
    }

    /**
     * Verifies that invalid Content Fragment paths are ignored.
     */
    @Test
    void testInvalidCFPath() {
        boolean found = model.getItems().stream()
                .anyMatch(item -> item.getProduct() == null);

        assertFalse(found, "Invalid CF paths should be ignored");
    }

    /**
     * Tests category extraction from product items.
     */
    @Test
    void testGetCategories() {
        List<String> categories = model.getCategories();

        assertNotNull(categories);
        assertEquals(1, categories.size());
        assertEquals("Vegetables", categories.get(0));
    }

    /**
     * Ensures that duplicate categories are not included.
     */
    @Test
    void testGetCategoriesNoDuplicates() {
        List<String> categories = model.getCategories();

        long count = categories.stream()
                .filter(cat -> cat.equals("Vegetables"))
                .count();

        assertEquals(1, count);
    }

    /**
     * Verifies that category values are trimmed (no leading/trailing spaces).
     */
    @Test
    void testGetCategoriesTrim() {
        List<String> categories = model.getCategories();

        categories.forEach(cat ->
                assertEquals(cat.trim(), cat)
        );
    }

    /**
     * Tests behavior when the model is adapted from an empty resource.
     *
     * Ensures that items and categories return empty lists without errors.
     */
    @Test
    void testEmptyModel() {

        context.create().resource("/content/empty");
        context.currentResource("/content/empty");

        ProductListModel emptyModel = context.currentResource().adaptTo(ProductListModel.class);

        assertNotNull(emptyModel);
        assertTrue(emptyModel.getItems().isEmpty());
        assertTrue(emptyModel.getCategories().isEmpty());
    }
}