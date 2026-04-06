package com.bhasaka.fruitables.core.models;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(AemContextExtension.class)
class StatCardTest {

    private final AemContext context = new AemContext();
    private StatCard model;

    @BeforeEach
    void setUp() {
        context.addModelsForClasses(StatCard.class);
        context.load().json("/statcard.json", "/content/card");
        Resource resource = context.resourceResolver().getResource("/content/card");
        assertNotNull(resource);
        model = resource.adaptTo(StatCard.class);
        assertNotNull(model);
    }

    @Test
    void testStatCardFields() {
        assertEquals("/content/dam/icons/apple.png", model.getIconImage());
        assertEquals("Apples Sold", model.getTitle());
        assertEquals("150", model.getValue());
    }
}