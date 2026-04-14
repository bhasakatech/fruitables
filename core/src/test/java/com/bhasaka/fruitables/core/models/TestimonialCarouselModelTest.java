package com.bhasaka.fruitables.core.models;

import java.util.List;

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
 * Unit test class for {@link TestimonialCarouselModel}.
 * <p>
 * This class verifies the behavior of the TestimonialCarouselModel Sling Model
 * using AEM mock context. It specifically tests scenarios where testimonial
 * items are not present and ensures proper handling of such cases.
 * </p>
 */
@ExtendWith(AemContextExtension.class)
class TestimonialCarouselModelTest {

    /** AEM mock context used to simulate repository and model adaptation */
    private final AemContext context = new AemContext();

    /**
     * Sets up the test environment by registering the models
     * and loading JSON test data into the mock repository.
     */
    @BeforeEach
    void setUp() {
        context.addModelsForClasses(TestimonialCarouselModel.class, TestimonialItemsModel.class);
        context.load().json("/testimonial-carousel.json", "/content/test");
    }

    /**
     * Adapts the resource at the given path to {@link TestimonialCarouselModel}.
     *
     * @param path resource path
     * @return adapted TestimonialCarouselModel instance
     */
    private TestimonialCarouselModel adapt(String path) {
        Resource resource = context.resourceResolver().getResource(path);
        assertNotNull(resource);
        TestimonialCarouselModel model = resource.adaptTo(TestimonialCarouselModel.class);
        assertNotNull(model);
        return model;
    }

    /**
     * Tests the scenario where no testimonials are configured.
     * Verifies that subtitle and title are populated and
     * testimonials list is null.
     */
    @Test
    void testNoTestimonialsComponent() {
        TestimonialCarouselModel model = adapt("/content/test/noTestimonialsComponent");
        assertEquals("Only Heading", model.getSubtitle());
        assertEquals("No Items", model.getTitle());
        List<TestimonialItemsModel> testimonials = model.getTestimonials();
        assertNull(testimonials);
    }
}