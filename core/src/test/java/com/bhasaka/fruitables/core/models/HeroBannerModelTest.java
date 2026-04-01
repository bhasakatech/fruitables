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
        context.addModelsForClasses(HeroBannerModel.class, SlideModel.class);
    }

    @Test
    void testModelWithSlides() {
        context.load().json("/hero-banner.json", "/content/test");

        Resource resource = context.resourceResolver().getResource("/content/test");
        model = resource.adaptTo(HeroBannerModel.class);

        assertNotNull(model);

        // subtitle & title
        assertEquals("Test Subtitle", model.getSubtitle());
        assertEquals("Test Title", model.getTitle());

        // slides
        assertNotNull(model.getSlides());
        assertEquals(2, model.getSlides().size());

        assertEquals("/content/dam/test1.jpg", model.getSlides().get(0).getImage());
        assertEquals("Slide 1", model.getSlides().get(0).getLabel());
    }

    @Test
    void testModelWithoutSlides() {
        context.create().resource("/content/test2",
                "subtitle", "No Slides Subtitle",
                "title", "No Slides Title"
        );

        Resource resource = context.resourceResolver().getResource("/content/test2");
        model = resource.adaptTo(HeroBannerModel.class);

        assertNotNull(model);

        // subtitle & title
        assertEquals("No Slides Subtitle", model.getSubtitle());
        assertEquals("No Slides Title", model.getTitle());

        // slides should be empty list (covers null case)
        assertNotNull(model.getSlides());
        assertTrue(model.getSlides().isEmpty());
    }
}