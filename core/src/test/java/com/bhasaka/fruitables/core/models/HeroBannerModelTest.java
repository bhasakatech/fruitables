package com.bhasaka.fruitables.core.models;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(AemContextExtension.class)
class HeroBannerModelTest {

    private final AemContext context = new AemContext();
    private HeroBannerModel model;

    @BeforeEach
    void setUp() {
        // Register model
        context.addModelsForClasses(HeroBannerModel.class);

        // Load JSON
        context.load().json("/hero-banner.json", "/content");

        // Set current resource
        Resource resource = context.resourceResolver().getResource("/content/hero");
        context.currentResource(resource);

        // Adapt model from RESOURCE (correct way)
        model = resource.adaptTo(HeroBannerModel.class);
    }

    @Test
    void testModelWithSlides() {

        assertNotNull(model);

        // Validate subtitle & title
        assertEquals("Fresh & Organic", model.getSubtitle());
        assertEquals("Healthy Fruits Collection", model.getTitle());

        // Validate slides
        assertNotNull(model.getSlides());
        assertEquals(2, model.getSlides().size());

        // Slide 1
        assertEquals("/content/dam/fruits/apple.jpg", model.getSlides().get(0).getImage());
        assertEquals("Apple", model.getSlides().get(0).getLabel());

        // Slide 2
        assertEquals("/content/dam/fruits/banana.jpg", model.getSlides().get(1).getImage());
        assertEquals("Banana", model.getSlides().get(1).getLabel());
    }
}
