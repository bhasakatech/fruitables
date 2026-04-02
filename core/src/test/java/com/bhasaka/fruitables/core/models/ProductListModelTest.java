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
    void testSectionTitleAndDescription() {
        assertEquals("Fresh Vegetables", model.getSectionTitle());
        assertEquals("Organic and fresh items", model.getSectionDescription());
        assertEquals("Add to Cart", model.getCartButtonText());
    }

    @Test
    void testItemsSize() {
        assertEquals(3, model.getItems().size());
    }

    @Test
    void testItemValues() {
        List<ProductItem> items = model.getItems();

        assertEquals("Tomato", items.get(0).getProduct().getProductName());
        assertEquals("standard", items.get(0).getCardStyle());

        assertEquals("Potato", items.get(1).getProduct().getProductName());
        assertEquals("compact", items.get(1).getCardStyle());

        assertEquals("Carrot", items.get(2).getProduct().getProductName());
        assertEquals("compact", items.get(2).getCardStyle());
    }

    @Test
    void testNestedChildBranch() {
        boolean found = model.getItems().stream()
                .anyMatch(item -> "Carrot".equals(item.getProduct().getProductName()));
        assertTrue(found);
    }

    @Test
    void testInitWithNoProducts() {
        context.create().resource("/content/empty");
        context.currentResource("/content/empty");

        ProductListModel emptyModel = context.currentResource().adaptTo(ProductListModel.class);
        assertEquals(0, emptyModel.getItems().size());
    }

    @Test
    void testCartButtonGetter() {
        assertEquals("Add to Cart", model.getCartButtonText());
    }
}
