package com.bhasaka.fruitables.core.models;

import static org.junit.jupiter.api.Assertions.*;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

/**
 * Unit test class for {@link BillingFormModel}.
 *
 * <p>This class uses AEM Mocks ({@link AemContext}) to simulate an AEM environment
 * and validate the behavior of the BillingFormModel.</p>
 *
 * <p>The test cases cover:
 * <ul>
 *     <li>Verification of all injected field values from authored content</li>
 *     <li>Handling of empty or missing properties</li>
 *     <li>Successful execution of the {@code @PostConstruct} lifecycle method</li>
 * </ul>
 * </p>
 */
@ExtendWith(AemContextExtension.class)
class BillingFormModelTest {

    /**
     * AEM mock context used to simulate repository, resources,
     * and Sling Model adaptation.
     */
    private final AemContext context = new AemContext();

    /**
     * Initializes the test context before each test.
     *
     * <p>Registers the model class and loads mock JSON content
     * into the in-memory repository under /content.</p>
     */
    @BeforeEach
    void setUp() {
        context.addModelsForClasses(BillingFormModel.class);
        context.load().json("/BillingFormModel.json", "/content");
    }

    /**
     * Tests that all fields are correctly injected from the resource.
     *
     * <p>Validates that each getter method returns the expected
     * authored value defined in the JSON file.</p>
     */
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

    /**
     * Tests the behavior when no properties are authored.
     *
     * <p>Ensures that all getters return {@code null}
     * when values are missing in the resource.</p>
     */
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

    /**
     * Tests that the model is successfully initialized and
     * the {@code @PostConstruct} method is executed.
     *
     * <p>Since the init() method currently contains no logic,
     * this test ensures that model adaptation does not fail.</p>
     */
    @Test
    void testPostConstructExecution() {
        Resource resource = context.resourceResolver().getResource("/content/billingForm");
        BillingFormModel model = resource.adaptTo(BillingFormModel.class);

        assertNotNull(model); // ensures init() is executed
    }
}