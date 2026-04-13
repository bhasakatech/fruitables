package com.bhasaka.fruitables.core.models;

import static org.junit.jupiter.api.Assertions.*;

import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

@ExtendWith(AemContextExtension.class)
class ProductReviewFeedbackFormModelTest {

    private final AemContext context = new AemContext();

    @BeforeEach
    void setUp() {
        context.addModelsForClasses(ProductReviewFeedbackFormModel.class);
        context.load().json("/product-review-form.json", "/content/test");
    }

    private ProductReviewFeedbackFormModel adapt(String path) {
        Resource resource = context.resourceResolver().getResource(path);
        assertNotNull(resource);
        ProductReviewFeedbackFormModel model = resource.adaptTo(ProductReviewFeedbackFormModel.class);
        assertNotNull(model);
        return model;
    }

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