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

@ExtendWith(AemContextExtension.class)
class TestimonialCarouselModelTest {

    private final AemContext context = new AemContext();

    @BeforeEach
    void setUp() {
        context.addModelsForClasses(TestimonialCarouselModel.class, TestimonialItemsModel.class);
        context.load().json("/testimonial-carousel.json", "/content/test");
    }

    private TestimonialCarouselModel adapt(String path) {
        Resource resource = context.resourceResolver().getResource(path);
        assertNotNull(resource);
        TestimonialCarouselModel model = resource.adaptTo(TestimonialCarouselModel.class);
        assertNotNull(model);
        return model;
    }


    @Test
    void testNoTestimonialsComponent() {
        TestimonialCarouselModel model = adapt("/content/test/noTestimonialsComponent");
        assertEquals("Only Heading", model.getSubtitle());
        assertEquals("No Items", model.getTitle());
        List<TestimonialItemsModel> testimonials = model.getTestimonials();
        assertNull(testimonials);
    }
}
