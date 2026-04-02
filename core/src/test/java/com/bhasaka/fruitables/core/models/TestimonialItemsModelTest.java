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

@ExtendWith(AemContextExtension.class)
class TestimonialItemsModelTest {

    private final AemContext context = new AemContext();

    @BeforeEach
    void setUp() {
        context.addModelsForClasses(TestimonialItemsModel.class);
        context.load().json("/testimonial-items.json", "/content/test");
    }

    private TestimonialItemsModel adapt(String path) {
        Resource resource = context.resourceResolver().getResource(path);
        assertNotNull(resource);
        TestimonialItemsModel model = resource.adaptTo(TestimonialItemsModel.class);
        assertNotNull(model);
        return model;
    }

    @Test
    void testValidItem() {
        TestimonialItemsModel model = adapt("/content/test/validItem");

        assertEquals("/content/dam/client1.png", model.getClientImage());
        assertEquals("Client One", model.getClientName());
        assertEquals("Designer", model.getProfession());
        assertEquals("Excellent service", model.getReviewText());
        assertEquals("5", model.getRating());
    }

    @Test
    void testNullValuesItem() {
        TestimonialItemsModel model = adapt("/content/test/nullValuesItem");

        assertNull(model.getClientImage());
        assertEquals("", model.getClientName());
        assertNull(model.getProfession());
        assertNull(model.getReviewText());
        assertNull(model.getRating());
    }

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
