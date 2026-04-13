package com.bhasaka.fruitables.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ValueMap;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
/**
 * Unit test class for {@link Error404PageModel}.
 * <p>
 * This class verifies the behavior of the Error404PageModel Sling Model
 * using AEM mock context by testing scenarios such as valid data,
 * null values, and empty values.
 * </p>
 */
@ExtendWith(AemContextExtension.class)
class Error404PageModelTest {

    /** AEM mock context used to simulate repository and model adaptation */
    private final AemContext context = new AemContext();

    /**
     * Sets up the test environment by registering the model
     * and loading JSON test data into the mock repository.
     */
    @BeforeEach
    public void setup() {
        context.addModelsForClasses(Error404PageModel.class);
        context.load().json("/Error404Pages.json", "/content/test");
    }

    /**
     * Adapts the resource at the given path to {@link Error404PageModel}.
     *
     * @param path resource path
     * @return adapted Error404PageModel instance
     */
    private Error404PageModel adapt(String path) {
        Resource resource = context.resourceResolver().getResource(path);
        assertNotNull(resource);
        Error404PageModel model = resource.adaptTo(Error404PageModel.class);
        assertNotNull(model);
        return model;
    }

    /**
     * Tests the model with valid component data.
     * Verifies that all properties are correctly mapped from the resource.
     */
    @Test
    public void testValidComponent() {
        Error404PageModel model = adapt("/content/test/validComponent");
        Resource resource = context.resourceResolver().getResource("/content/test/validComponent");
        ValueMap vmp = resource.getValueMap();
        assertEquals(model.getImage(), vmp.get("image"));
        assertEquals(model.getTitle(), vmp.get("title"));
        assertEquals(model.getSubtitle(), vmp.get("subtitle"));
        assertEquals(model.getDescription(), vmp.get("description"));
        assertEquals(model.getCtaLink(), vmp.get("ctaLink"));
        assertEquals(model.getCtaText(), vmp.get("ctaText"));
    }

    /**
     * Tests the model when all properties are null.
     * Ensures getters return null values appropriately.
     */
    @Test
    public void testNullValuesComponent() {
        Error404PageModel model = adapt("/content/test/nullValuesComponent");
        assertNull(model.getImage());
        assertNull(model.getTitle());
        assertNull(model.getSubtitle());
        assertNull(model.getDescription());
        assertNull(model.getCtaLink());
        assertNull(model.getCtaText());
    }

    /**
     * Tests the model when properties are empty strings.
     * Ensures getters return empty values correctly.
     */
    @Test
    public void testEmptyValuesComponent() {
        Error404PageModel model = adapt("/content/test/emptyValuesComponent");
        assertEquals("", model.getImage());
        assertEquals("", model.getTitle());
        assertEquals("", model.getSubtitle());
        assertEquals("", model.getDescription());
        assertEquals("", model.getCtaLink());
        assertEquals("", model.getCtaText());
    }
}