package com.bhasaka.fruitables.core.models;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(AemContextExtension.class)
class ContactUsModelTest {
    private final AemContext context = new AemContext();

    @BeforeEach
    void setUp() {
        context.addModelsForClasses(ContactUsModel.class);
        context.load().json("/ContactUsModelTest.json", "/content");
        context.currentResource("/content/contact");
    }

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

    @Test
    void testResourceAdaptation() {
        Resource resource = context.resourceResolver().getResource("/content/contact");
        assertNotNull(resource);
        ContactUsModel model = resource.adaptTo(ContactUsModel.class);
        assertNotNull(model);
    }
}