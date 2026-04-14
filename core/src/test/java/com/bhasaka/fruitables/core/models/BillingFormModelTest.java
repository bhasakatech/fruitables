package com.bhasaka.fruitables.core.models;

import static org.junit.jupiter.api.Assertions.*;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(AemContextExtension.class)
class BillingFormModelTest {

    private final AemContext context = new AemContext();

    @BeforeEach
    void setUp() {
        context.addModelsForClasses(BillingFormModel.class);
        context.load().json("/BillingFormModel.json", "/content");
    }

    @Test
    void testAllFieldsInjected() {
        Resource resource = context.resourceResolver().getResource("/content/billingForm");
        assertNotNull(resource);

        BillingFormModel model = resource.adaptTo(BillingFormModel.class);
        assertNotNull(model);

        assertEquals("Billing Details", model.getSectionTitle());
        assertEquals("First Name *", model.getFirstNameLabel());
        assertEquals("Last Name *", model.getLastNameLabel());
        assertEquals("Company Name *", model.getCompanyNameLabel());
        assertEquals("Address *", model.getAddressLabel());
        assertEquals("House number and street", model.getAddressPlaceholder());
        assertEquals("Town / City *", model.getCityLabel());
        assertEquals("Country *", model.getCountryLabel());
        assertEquals("Postcode / Zip *", model.getZipLabel());
        assertEquals("Mobile *", model.getMobileLabel());
        assertEquals("Email Address *", model.getEmailLabel());
        assertEquals("Create an account?", model.getCreateAccountLabel());
        assertEquals("Ship to a different address?", model.getShipDifferentAddressLabel());
        assertEquals("Order Notes (Optional)", model.getNotesPlaceholder());
    }

    @Test
    void testEmptyValues() {
        Resource resource = context.resourceResolver().getResource("/content/billingFormEmpty");
        assertNotNull(resource);

        BillingFormModel model = resource.adaptTo(BillingFormModel.class);
        assertNotNull(model);

        assertNull(model.getSectionTitle());
        assertNull(model.getFirstNameLabel());
        assertNull(model.getLastNameLabel());
        assertNull(model.getCompanyNameLabel());
        assertNull(model.getAddressLabel());
        assertNull(model.getAddressPlaceholder());
        assertNull(model.getCityLabel());
        assertNull(model.getCountryLabel());
        assertNull(model.getZipLabel());
        assertNull(model.getMobileLabel());
        assertNull(model.getEmailLabel());
        assertNull(model.getCreateAccountLabel());
        assertNull(model.getShipDifferentAddressLabel());
        assertNull(model.getNotesPlaceholder());
    }

    @Test
    void testPostConstructExecution() {
        Resource resource = context.resourceResolver().getResource("/content/billingForm");
        BillingFormModel model = resource.adaptTo(BillingFormModel.class);

        assertNotNull(model); // ensures init() is executed
    }
}