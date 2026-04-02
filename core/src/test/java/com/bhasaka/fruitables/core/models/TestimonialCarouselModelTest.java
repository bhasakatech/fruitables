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
    void testValidComponent() {
        TestimonialCarouselModel model = adapt("/content/test/validComponent");
        assertEquals("Our Testimonial", model.getSubtitle());
        assertEquals("Our Client Saying!", model.getTitle());
        List<TestimonialItemsModel> testimonials = model.getTestimonials();
        assertNotNull(testimonials);
        assertEquals(2, testimonials.size());
        assertEquals("/content/dam/client1.png", testimonials.get(0).getClientImage());
        assertEquals("Client One", testimonials.get(0).getClientName());
        assertEquals("Designer", testimonials.get(0).getProfession());
        assertEquals("Excellent service", testimonials.get(0).getReviewText());
        assertEquals("5", testimonials.get(0).getRating());
        assertEquals("/content/dam/client2.png", testimonials.get(1).getClientImage());
        assertEquals("Client Two", testimonials.get(1).getClientName());
        assertEquals("Developer", testimonials.get(1).getProfession());
        assertEquals("Very good support", testimonials.get(1).getReviewText());
        assertEquals("4", testimonials.get(1).getRating());
    }

    @Test
    void testNullValuesComponent() {
        TestimonialCarouselModel model = adapt("/content/test/nullValuesComponent");
        assertEquals("", model.getSubtitle());
        assertEquals("", model.getTitle());
        List<TestimonialItemsModel> testimonials = model.getTestimonials();
        assertNotNull(testimonials);
        assertEquals(1, testimonials.size());
        TestimonialItemsModel item = testimonials.get(0);
        assertNull(item.getClientImage());
        assertEquals("", item.getClientName());
        assertNull(item.getProfession());
        assertNull(item.getReviewText());
        assertNull(item.getRating());
    }

    @Test
    void testNoTestimonialsComponent() {
        TestimonialCarouselModel model = adapt("/content/test/noTestimonialsComponent");
        assertEquals("Only Heading", model.getSubtitle());
        assertEquals("No Items", model.getTitle());
        List<TestimonialItemsModel> testimonials = model.getTestimonials();
        assertNotNull(testimonials);
        assertEquals(0, testimonials.size());
    }
}
