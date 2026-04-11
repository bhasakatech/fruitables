package com.bhasaka.fruitables.core.models;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.testing.mock.sling.ResourceResolverType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(AemContextExtension.class)
class CategorysTagsModelTest {

    private final AemContext context = new AemContext();

    @BeforeEach
    void setUp() {
        context.addModelsForClasses(
                CategorysTagsModel.class,
                ProductCFModelTag.class
        );
        context.load().json("/CategorysTagsModelTest.json", "/content");
    }

    @Test
    void testConfiguredModelBuildsCategoriesAndSelectionState() {
        Map<String, Object> parameters = new LinkedHashMap<>();
        parameters.put("sort", "popular");
        parameters.put("category", "fruitables:apples");

        CategorysTagsModel model = adaptRequestModel(
                "/content/page/jcr:content/root/categorys-tags",
                parameters
        );

        List<CategorysTagsModel.CategoryItem> categories = model.getCategories();
        List<ProductItem> products = model.getProducts();

        assertAll(
                () -> assertTrue(model.isConfigured()),
                () -> assertTrue(model.isHasSelection()),
                () -> assertFalse(model.isHasProducts()),
                () -> assertEquals("Browse by category", model.getCategoryTitle()),
                () -> assertEquals("Apples", model.getSelectedCategoryTitle()),
                () -> assertEquals("Add now", model.getCartButtonText()),
                () -> assertEquals("featured", model.getCardStyle()),
                () -> assertEquals("/kg", model.getPriceUnit()),
                () -> assertFalse(model.isShowDescription()),
                () -> assertTrue(model.isShowRating()),
                () -> assertFalse(model.isShowCategory()),
                () -> assertTrue(model.isShowBorder()),
                () -> assertEquals("green", model.getCategoryColor()),
                () -> assertEquals("bottom-right", model.getBadgePosition()),
                () -> assertEquals("Pick a category to start shopping.", model.getEmptySelectionMessage()),
                () -> assertEquals("No products match this category.", model.getNoResultsMessage())
        );

        assertAll(
                () -> assertEquals(2, categories.size()),
                () -> assertEquals("fruitables:apples", categories.get(0).getTagId()),
                () -> assertEquals("Apples", categories.get(0).getTitle()),
                () -> assertEquals(0, categories.get(0).getCount()),
                () -> assertTrue(categories.get(0).isSelected()),
                () -> assertTrue(categories.get(0).getLink().contains("sort=popular")),
                () -> assertTrue(categories.get(0).getLink().contains("category=fruitables%3Aapples")),
                () -> assertEquals("fruitables:citrus", categories.get(1).getTagId()),
                () -> assertEquals("Citrus", categories.get(1).getTitle()),
                () -> assertEquals(0, categories.get(1).getCount()),
                () -> assertFalse(categories.get(1).isSelected())
        );

        assertTrue(products.isEmpty());
    }

    @Test
    void testUnknownSelectionDoesNotMarkCategoryAsSelected() {
        CategorysTagsModel model = adaptRequestModel(
                "/content/page/jcr:content/root/categorys-tags",
                Collections.singletonMap("category", "fruitables:unknown")
        );

        assertAll(
                () -> assertTrue(model.isConfigured()),
                () -> assertFalse(model.isHasSelection()),
                () -> assertFalse(model.isHasProducts()),
                () -> assertEquals("Products", model.getSelectedCategoryTitle()),
                () -> assertEquals(2, model.getCategories().size()),
                () -> assertFalse(model.getCategories().get(0).isSelected()),
                () -> assertFalse(model.getCategories().get(1).isSelected())
        );
    }

    @Test
    void testDefaultsAreUsedWhenComponentIsNotConfigured() {
        CategorysTagsModel model = adaptRequestModel(
                "/content/page/jcr:content/root/categorys-tags-defaults",
                Collections.emptyMap()
        );

        assertAll(
                () -> assertFalse(model.isConfigured()),
                () -> assertFalse(model.isHasSelection()),
                () -> assertFalse(model.isHasProducts()),
                () -> assertEquals("Categories", model.getCategoryTitle()),
                () -> assertTrue(model.getCategories().isEmpty()),
                () -> assertEquals("Products", model.getSelectedCategoryTitle()),
                () -> assertEquals("Add to cart", model.getCartButtonText()),
                () -> assertEquals("standard", model.getCardStyle()),
                () -> assertEquals("orange", model.getCategoryColor()),
                () -> assertEquals("top-left", model.getBadgePosition()),
                () -> assertTrue(model.isShowDescription()),
                () -> assertTrue(model.isShowRating()),
                () -> assertTrue(model.isShowCategory()),
                () -> assertFalse(model.isShowBorder()),
                () -> assertEquals("Select a category to load the matching products.", model.getEmptySelectionMessage()),
                () -> assertEquals("No products were found for the selected category.", model.getNoResultsMessage()),
                () -> assertTrue(model.getComponentId().startsWith("categorys-tags-"))
        );
    }

    private CategorysTagsModel adaptRequestModel(String resourcePath, Map<String, Object> parameters) {
        Resource resource = context.resourceResolver().getResource(resourcePath);
        assertNotNull(resource);

        context.currentResource(resource);
        context.request().setResource(resource);
        context.request().setParameterMap(parameters);

        CategorysTagsModel model = context.request().adaptTo(CategorysTagsModel.class);
        assertNotNull(model);
        return model;
    }
}
