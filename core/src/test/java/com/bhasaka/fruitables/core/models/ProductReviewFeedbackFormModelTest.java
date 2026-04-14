package com.bhasaka.fruitables.core.models;

import static org.junit.jupiter.api.Assertions.*;

import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
/**
 * Unit test class for {@link ProductReviewFeedbackFormModel}.
 * <p>
 * This class validates the behavior of the ProductReviewFeedbackFormModel
 * Sling Model using AEM mock context by testing various scenarios such as
 * valid data, null values, empty resources, and partially populated data.
 * </p>
 */
@ExtendWith(AemContextExtension.class)
class ProductReviewFeedbackFormModelTest {

    /** AEM mock context used for simulating repository and model adaptation */
    private final AemContext context = new AemContext();

    /**
     * Initializes the test setup by registering the model
     * and loading JSON test data into the mock repository.
     */
    @BeforeEach
    void setUp() {
        context.addModelsForClasses(ProductReviewFeedbackFormModel.class);
        context.load().json("/product-review-form.json", "/content/test");
    }

    /**
     * Adapts the resource at the given path to {@link ProductReviewFeedbackFormModel}.
     *
     * @param path resource path
     * @return adapted ProductReviewFeedbackFormModel instance
     */
    private ProductReviewFeedbackFormModel adapt(String path) {
        Resource resource = context.resourceResolver().getResource(path);
        assertNotNull(resource);
        ProductReviewFeedbackFormModel model = resource.adaptTo(ProductReviewFeedbackFormModel.class);
        assertNotNull(model);
        return model;
    }

    /**
     * Tests the model with valid component data.
     * Verifies that all fields are correctly mapped.
     */
    @Test
    void testValidComponent() {
        ProductReviewFeedbackFormModel model = adapt("/content/test/validComponent");
        assertEquals("Write a Review", model.getSectionTitle());
        assertEquals("Enter your name", model.getNamePlaceholder());
        assertEquals("Enter your email", model.getEmailPlaceholder());
        assertEquals("Write your review here...", model.getReviewPlaceholder());
        assertEquals("Rating", model.getRatingLabel());
        assertEquals("Submit Review", model.getButtonText());
    }

    /**
     * Tests the model when some properties contain null or empty values.
     * Ensures proper handling of missing data.
     */
    @Test
    void testNullValuesComponent() {
        ProductReviewFeedbackFormModel model = adapt("/content/test/nullValuesComponent");
        assertEquals("", model.getSectionTitle());
        assertNull(model.getNamePlaceholder());
        assertNull(model.getEmailPlaceholder());
        assertNull(model.getReviewPlaceholder());
        assertNull(model.getRatingLabel());
        assertNull(model.getButtonText());
    }

    /**
     * Tests the model when no properties are defined.
     * Verifies that all values return null.
     */
    @Test
    void testEmptyComponent() {
        ProductReviewFeedbackFormModel model = adapt("/content/test/emptyComponent");
        assertNull(model.getSectionTitle());
        assertNull(model.getNamePlaceholder());
        assertNull(model.getEmailPlaceholder());
        assertNull(model.getReviewPlaceholder());
        assertNull(model.getRatingLabel());
        assertNull(model.getButtonText());
    }

    /**
     * Tests the model with partially populated data.
     * Verifies that available fields are mapped and others remain null.
     */
    @Test
    void testPartialComponent() {
        ProductReviewFeedbackFormModel model = adapt("/content/test/partialComponent");
        assertEquals("Only Title", model.getSectionTitle());
        assertEquals("Submit", model.getButtonText());
        assertNull(model.getNamePlaceholder());
        assertNull(model.getEmailPlaceholder());
        assertNull(model.getReviewPlaceholder());
        assertNull(model.getRatingLabel());
    }
}