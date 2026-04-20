package com.bhasaka.fruitables.core.models;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

/**
 * Unit test class for {@link ContactUsModel}.
 * <p>
 * This class uses AEM Mocks to simulate repository content and verify
 * that the ContactUsModel correctly maps resource properties and handles
 * different scenarios like populated and empty resources.
 * </p>
 */
@ExtendWith(AemContextExtension.class)
class ContactUsModelTest {

    /**
     * AEM mock context used for simulating Sling and JCR environment.
     */
    private final AemContext context = new AemContext();

    /**
     * Sets up the test environment before each test case.
     * <p>
     * This includes:
     * <ul>
     *     <li>Registering the model class.</li>
     *     <li>Loading JSON test data into the mock repository.</li>
     *     <li>Setting the current resource for testing.</li>
     * </ul>
     * </p>
     */
    @BeforeEach
    void setUp() {
        context.addModelsForClasses(ContactUsModel.class);
        context.load().json("/ContactUsModelTest.json", "/content");
        context.currentResource("/content/contact");
    }

    /**
     * Tests model behavior when all fields are populated.
     * <p>
     * Verifies that all getter methods return expected values
     * from the JSON configuration.
     * </p>
     */
    @Test
    void testAllFields() {
        ContactUsModel model = context.currentResource().adaptTo(ContactUsModel.class);

        assertNotNull(model);
        assertEquals("Contact Us", model.getHeadingText());
        assertEquals("This is description", model.getDescriptionText());
        assertEquals("Download", model.getButtonText());
        assertEquals("https://maps.google.com/test", model.getMapUrl());

        assertEquals("Enter Name", model.getNamePlaceholder());
        assertEquals("Enter Email", model.getEmailPlaceholder());
        assertEquals("Enter Message", model.getMessagePlaceholder());
        assertEquals("Submit", model.getSubmitButtonText());

        assertEquals("Address", model.getAddressLabel());
        assertEquals("Hyderabad", model.getAddressText());
        assertEquals("Email", model.getEmailLabel());
        assertEquals("test@gmail.com", model.getEmailText());
        assertEquals("Phone", model.getPhoneLabel());
        assertEquals(9876543210L, model.getPhoneNumber());
    }

    /**
     * Tests model behavior when resource has no properties.
     * <p>
     * Ensures that the model adapts successfully but returns null
     * for all string fields and default value for primitive types.
     * </p>
     */
    @Test
    void testEmptyResource() {
        Resource resource = context.create().resource("/content/empty");
        ContactUsModel model = resource.adaptTo(ContactUsModel.class);

        assertNotNull(model);

        assertNull(model.getHeadingText());
        assertNull(model.getDescriptionText());
        assertNull(model.getButtonText());
        assertNull(model.getMapUrl());

        assertNull(model.getNamePlaceholder());
        assertNull(model.getEmailPlaceholder());
        assertNull(model.getMessagePlaceholder());
        assertNull(model.getSubmitButtonText());

        assertNull(model.getAddressLabel());
        assertNull(model.getAddressText());
        assertNull(model.getEmailLabel());
        assertNull(model.getEmailText());
        assertNull(model.getPhoneLabel());
        assertEquals(0, model.getPhoneNumber());
    }

    /**
     * Tests basic resource-to-model adaptation.
     * <p>
     * Verifies that the resource is successfully adapted
     * to ContactUsModel.
     * </p>
     */
    @Test
    void testResourceAdaptation() {
        Resource resource = context.resourceResolver().getResource("/content/contact");

        assertNotNull(resource);

        ContactUsModel model = resource.adaptTo(ContactUsModel.class);

        assertNotNull(model);
    }
}