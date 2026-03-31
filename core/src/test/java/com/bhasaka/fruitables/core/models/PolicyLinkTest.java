package com.bhasaka.fruitables.core.models;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(AemContextExtension.class)
class PolicyLinkTest {

    private final AemContext context = new AemContext();
    private PolicyLink model;

    @BeforeEach
    void setUp() {

        // Register model
        context.addModelsForClasses(PolicyLink.class);

        // Load JSON
        context.load().json("/policylink.json", "/content/policy");

        // Get resource
        Resource resource = context.resourceResolver().getResource("/content/policy");

        assertNotNull(resource);

        // Adapt to model
        model = resource.adaptTo(PolicyLink.class);

        assertNotNull(model);
    }

    @Test
    void testLinkText() {
        assertEquals("Privacy Policy", model.getLinkText());
    }

    @Test
    void testLinkUrl() {
        assertEquals("/privacy", model.getLinkUrl());
    }
}