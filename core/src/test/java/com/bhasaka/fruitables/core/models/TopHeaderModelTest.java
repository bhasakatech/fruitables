package com.bhasaka.fruitables.core.models;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
@ExtendWith(AemContextExtension.class)
class TopHeaderModelTest {

    private final AemContext context = new AemContext();
    private TopHeaderModel model;

    @BeforeEach
    void setUp() {


        context.load().json("/topheader.json", "/content/header");
        // Register models
        context.addModelsForClasses(TopHeaderModel.class, PolicyLink.class);

        Resource resource = context.resourceResolver().getResource("/content/header");

        model = resource.adaptTo(TopHeaderModel.class);
    }

    @Test
    void testAddress() {
        assertEquals("Hyderabad, India", model.getAddress());
    }

    @Test
    void testEmail() {
        assertEquals("test@example.com", model.getEmail());
    }

    @Test
    void testPolicyLinks() {
        List<PolicyLink> links = model.getPolicyLinks();

        assertNotNull(links);
        assertEquals(2, links.size());

        assertEquals("Privacy Policy", links.get(0).getLinkText());
        assertEquals("/privacy", links.get(0).getLinkUrl());
    }
}