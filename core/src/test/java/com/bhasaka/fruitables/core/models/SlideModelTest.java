package com.bhasaka.fruitables.core.models;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(AemContextExtension.class)
class SlideModelTest {

    private final AemContext context = new AemContext();
    private SlideModel model;
    @BeforeEach
    void setUp() {
        context.addModelsForClasses(SlideModel.class);
    }

    @Test
    void testSlideModelWithValues() {
        context.load().json("/slide.json", "/content/slide");
        Resource resource = context.resourceResolver().getResource("/content/slide");
        model = resource.adaptTo(SlideModel.class);
        assertNotNull(model);
        assertEquals("/content/dam/sample.jpg", model.getImage());
        assertEquals("Test Slide", model.getLabel());
    }

    @Test
    void testSlideModelWithoutValues() {
        context.create().resource("/content/empty-slide");
        Resource resource = context.resourceResolver().getResource("/content/empty-slide");
        model = resource.adaptTo(SlideModel.class);
        assertNotNull(model);
        assertNull(model.getImage());
        assertNull(model.getLabel());
    }
}