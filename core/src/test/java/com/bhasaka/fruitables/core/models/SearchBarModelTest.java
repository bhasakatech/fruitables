package com.bhasaka.fruitables.core.models;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.lang.reflect.Field;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(AemContextExtension.class)
class SearchBarModelTest {

    private final AemContext context = new AemContext();

    private SearchBarModel model;

    @BeforeEach
    void setUp() {
        context.addModelsForClasses(SearchBarModel.class);
    }

    @Test
    void testModelWithValues() throws Exception {
        context.load().json("/search-bar.json", "/content/test");

        Resource resource = context.resourceResolver().getResource("/content/test");
        model = resource.adaptTo(SearchBarModel.class);

        assertNotNull(model);

        Field placeholderField = SearchBarModel.class.getDeclaredField("placeholder");
        placeholderField.setAccessible(true);

        Field buttonLabelField = SearchBarModel.class.getDeclaredField("buttonLabel");
        buttonLabelField.setAccessible(true);

        assertEquals("Search fruits", placeholderField.get(model));
        assertEquals("Submit Now", buttonLabelField.get(model));
    }

    @Test
    void testModelWithoutValues() throws Exception {
        context.create().resource("/content/empty");

        Resource resource = context.resourceResolver().getResource("/content/empty");
        model = resource.adaptTo(SearchBarModel.class);

        assertNotNull(model);

        Field placeholderField = SearchBarModel.class.getDeclaredField("placeholder");
        placeholderField.setAccessible(true);

        Field buttonLabelField = SearchBarModel.class.getDeclaredField("buttonLabel");
        buttonLabelField.setAccessible(true);

        assertNull(placeholderField.get(model));
        assertNull(buttonLabelField.get(model));
    }
}