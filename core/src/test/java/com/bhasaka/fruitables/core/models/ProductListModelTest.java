package com.bhasaka.fruitables.core.models;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(AemContextExtension.class)
class ProductListModelTest {

    private final AemContext context = new AemContext(ResourceResolverType.JCR_MOCK);
    private ProductListModel model;

    @BeforeEach
    void setUp() {
        context.addModelsForClasses(ProductListModel.class, ProductCFModel.class, ProductResource.class);
        context.load().json("/productlistmodel.json", "/content");

        context.currentResource("/content/component");
        model = context.currentResource().adaptTo(ProductListModel.class);
    }

    @Test
    void testSectionDetails() {
        assertNotNull(model);
        assertEquals("Fresh Vegetables", model.getSectionTitle());
        assertEquals("Organic and fresh items", model.getSectionDescription());
        assertEquals("Add to Cart", model.getCartButtonText());
    }

    @Test
    void testItemsSize() {
        assertNotNull(model.getItems());
        assertEquals(4, model.getItems().size(), "Only valid products should be added");
    }

    @Test
    void testItemValues() {
        List<ProductItem> items = model.getItems();

        assertTrue(items.stream().anyMatch(i -> "Tomato".equals(i.getProduct().getProductName())));
        assertTrue(items.stream().anyMatch(i -> "Potato".equals(i.getProduct().getProductName())));
        assertTrue(items.stream().anyMatch(i -> "Carrot".equals(i.getProduct().getProductName())));
        assertTrue(items.stream().anyMatch(i -> "Onion".equals(i.getProduct().getProductName())));
    }

    @Test
    void testDirectMasterNode() {
        boolean found = model.getItems().stream()
                .anyMatch(item -> "Tomato".equals(item.getProduct().getProductName()));

        assertTrue(found, "Direct master node should be processed");
    }

    @Test
    void testChildMasterNode() {
        boolean found = model.getItems().stream()
                .anyMatch(item -> "Onion".equals(item.getProduct().getProductName()));

        assertTrue(found, "Child master node should be processed");
    }

    @Test
    void testNoMasterNode() {
        boolean found = model.getItems().stream()
                .anyMatch(item -> "no-master".equals(item.getProduct().getProductName()));

        assertFalse(found, "Products without master should not be added");
    }

    @Test
    void testInvalidCFPath() {
        boolean found = model.getItems().stream()
                .anyMatch(item -> item.getProduct() == null);

        assertFalse(found, "Invalid CF paths should be ignored");
    }
    @Test
    void testGetCategories() {
        List<String> categories = model.getCategories();

        assertNotNull(categories);
        assertEquals(1, categories.size());
        assertEquals("Vegetables", categories.get(0));
    }

    @Test
    void testGetCategoriesNoDuplicates() {
        List<String> categories = model.getCategories();

        long count = categories.stream()
                .filter(cat -> cat.equals("Vegetables"))
                .count();

        assertEquals(1, count);
    }

    @Test
    void testGetCategoriesTrim() {
        List<String> categories = model.getCategories();

        categories.forEach(cat ->
                assertEquals(cat.trim(), cat)
        );
    }
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