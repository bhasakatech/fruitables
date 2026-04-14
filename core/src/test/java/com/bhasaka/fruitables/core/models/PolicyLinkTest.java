package com.bhasaka.fruitables.core.models;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test class for {@link PolicyLink}.
 *
 * <p>This class verifies mapping of policy link text and URL
 * from resource to model.</p>
 */
@ExtendWith(AemContextExtension.class)
class PolicyLinkTest {

    private final AemContext context = new AemContext();
    private PolicyLink model;

    /**
     * Sets up test context before each test.
     *
     * <p>Loads JSON data and adapts resource to {@link PolicyLink}.</p>
     */
    @BeforeEach
    void setUp() {

        context.addModelsForClasses(PolicyLink.class);

        context.load().json("/policylink.json", "/content/policy");

        Resource resource = context.resourceResolver().getResource("/content/policy");
        assertNotNull(resource);

        model = resource.adaptTo(PolicyLink.class);
        assertNotNull(model);
    }

    /**
     * Tests policy link text mapping.
     */
    @Test
    void testLinkText() {
        assertEquals("Privacy Policy", model.getLinkText());
    }

    /**
     * Tests policy link URL mapping.
     */
    @Test
    void testLinkUrl() {
        assertEquals("/privacy", model.getLinkUrl());
    }
}