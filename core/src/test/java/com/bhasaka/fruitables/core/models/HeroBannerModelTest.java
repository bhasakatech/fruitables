package com.bhasaka.fruitables.core.models;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test class for {@link HeroBannerModel}.
 *
 * <p>This class uses AEM Mocks ({@link AemContext}) to simulate
 * an AEM environment and validate the behavior of the HeroBannerModel.</p>
 *
 * <p>The tests cover:
 * <ul>
 *     <li>Injection of subtitle and title properties</li>
 *     <li>Handling of child resources (slides)</li>
 *     <li>Behavior when slides are present</li>
 *     <li>Behavior when slides are absent (empty list handling)</li>
 * </ul>
 * </p>
 */
@ExtendWith(AemContextExtension.class)
class HeroBannerModelTest {

    /**
     * AEM mock context used to simulate resource resolution,
     * content structure, and Sling Model adaptation.
     */
    private final AemContext context = new AemContext();

    /**
     * Instance of {@link HeroBannerModel} under test.
     */
    private HeroBannerModel model;

    /**
     * Sets up the test environment before each test execution.
     *
     * <p>Registers the required Sling Models so that they can be
     * adapted from resources during testing.</p>
     */
    @BeforeEach
    void setUp() {
        context.addModelsForClasses(HeroBannerModel.class, SlideModel.class);
    }

    /**
     * Tests the model when slide child resources are present.
     *
     * <p>Loads mock JSON content and verifies:
     * <ul>
     *     <li>Subtitle and title are correctly injected</li>
     *     <li>Slides list is populated</li>
     *     <li>Slide properties (image and label) are correctly mapped</li>
     * </ul>
     * </p>
     */
    @Test
    void testModelWithSlides() {
        context.load().json("/hero-banner.json", "/content/test");
        Resource resource = context.resourceResolver().getResource("/content/test");

        model = resource.adaptTo(HeroBannerModel.class);

        assertNotNull(model);
        assertEquals("Test Subtitle", model.getSubtitle());
        assertEquals("Test Title", model.getTitle());

        assertNotNull(model.getSlides());
        assertEquals(2, model.getSlides().size());

        assertEquals("/content/dam/test1.jpg", model.getSlides().get(0).getImage());
        assertEquals("Slide 1", model.getSlides().get(0).getLabel());
    }

    /**
     * Tests the model behavior when no slide child resources are present.
     *
     * <p>Verifies that:
     * <ul>
     *     <li>Subtitle and title are correctly injected</li>
     *     <li>Slides list is not null</li>
     *     <li>An empty list is returned instead of null</li>
     * </ul>
     * </p>
     */
    @Test
    void testModelWithoutSlides() {
        context.create().resource("/content/test2",
                "subtitle", "No Slides Subtitle",
                "title", "No Slides Title"
        );

        Resource resource = context.resourceResolver().getResource("/content/test2");
        model = resource.adaptTo(HeroBannerModel.class);

        assertNotNull(model);
        assertEquals("No Slides Subtitle", model.getSubtitle());
        assertEquals("No Slides Title", model.getTitle());

        assertNotNull(model.getSlides());
        assertTrue(model.getSlides().isEmpty());
    }
}