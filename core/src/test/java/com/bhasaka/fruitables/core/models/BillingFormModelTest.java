package com.bhasaka.fruitables.core.models;

import static org.junit.jupiter.api.Assertions.*;
import io.wcm.testing.mock.aem.junit5.AemContext;
import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

@ExtendWith(AemContextExtension.class)
class BillingFormModelTest {

    private final AemContext context = new AemContext();

    @BeforeEach
    void setUp() {
        context.addModelsForClasses(BillingFormModel.class);
        context.load().json("/BillingFormModel.json", "/content");
    }

    @Test
    void testModelWithValues() {
        Resource resource = context.resourceResolver().getResource("/content/billingForm");
        assertNotNull(resource);
        BillingFormModel model = resource.adaptTo(BillingFormModel.class);
        assertNotNull(model);
        assertEquals("Billing Details", model.getSectionTitle());
        assertEquals("Enter your name", model.getPlaceholder());
        assertEquals("Submit", model.getButtonLabel());
    }

    @Test
    void testModelWithEmptyValues() {
        Resource resource = context.resourceResolver().getResource("/content/billingFormEmpty");
        assertNotNull(resource);
        BillingFormModel model = resource.adaptTo(BillingFormModel.class);
        assertNotNull(model);
        assertNull(model.getSectionTitle());
        assertNull(model.getPlaceholder());
        assertNull(model.getButtonLabel());
    }

    @Test
    void testInitMethodCoverage() {
        Resource resource = context.resourceResolver().getResource("/content/billingForm");
        BillingFormModel model = resource.adaptTo(BillingFormModel.class);
        assertNotNull(model); // ensures @PostConstruct is executed
    }
}