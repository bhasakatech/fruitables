package com.bhasaka.fruitables.core.models;

import org.apache.sling.api.resource.Resource;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
/**
 * Unit test class for {@link TestimonialItemsModel}.
 * <p>
 * This class validates the behavior of the TestimonialItemsModel Sling Model
 * by testing different scenarios such as null values and empty resource data
 * using AEM mock context.
 * </p>
 */
@ExtendWith(AemContextExtension.class)
class TestimonialItemsModelTest {

    /** AEM mock context used to simulate repository and model adaptation */
    private final AemContext context = new AemContext();

    /**
     * Sets up the test environment by registering the model
     * and loading JSON test data into the mock repository.
     */
    @BeforeEach
    void setUp() {
        context.addModelsForClasses(TestimonialItemsModel.class);
        context.load().json("/testimonial-items.json", "/content/test");
    }

    /**
     * Adapts the resource at the given path to {@link TestimonialItemsModel}.
     *
     * @param path resource path
     * @return adapted TestimonialItemsModel instance
     */
    private TestimonialItemsModel adapt(String path) {
        Resource resource = context.resourceResolver().getResource(path);
        assertNotNull(resource);
        TestimonialItemsModel model = resource.adaptTo(TestimonialItemsModel.class);
        assertNotNull(model);
        return model;
    }

    /**
     * Tests the model when resource contains null or empty values.
     * Verifies that fields are correctly mapped or handled as null.
     */
    @Test
    void testNullValuesItem() {
        TestimonialItemsModel model = adapt("/content/test/nullValuesItem");
        assertNull(model.getClientImage());
        assertEquals("", model.getClientName());
        assertNull(model.getProfession());
        assertNull(model.getReviewText());
        assertNull(model.getRating());
    }

    /**
     * Tests the model when resource has no properties.
     * Ensures all fields return null values.
     */
    @Test
    void testEmptyItem() {
        TestimonialItemsModel model = adapt("/content/test/emptyItem");
        assertNull(model.getClientImage());
        assertNull(model.getClientName());
        assertNull(model.getProfession());
        assertNull(model.getReviewText());
        assertNull(model.getRating());
    }
}