package com.bhasaka.fruitables.core.models;

import java.util.List;

import org.apache.sling.api.resource.Resource;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;
/**
 * Unit test class for {@link ServiceHighlights}.
 * <p>
 * This class verifies the behavior of the ServiceHighlights Sling Model
 * by testing different scenarios such as valid data, null values,
 * missing services, and invalid configurations using AEM mock context.
 * </p>
 */
@ExtendWith(AemContextExtension.class)
class ServiceHighlightsTest {

    /** AEM mock context used for setting up resources and models */
    private final AemContext context = new AemContext();

    /**
     * Sets up the test environment by registering the model
     * and loading test JSON data into the mock repository.
     */
    @BeforeEach
    void setUp() {
        context.addModelsForClasses(ServiceHighlights.class);
        context.load().json("/service-highlights.json", "/content/test");
    }

    /**
     * Adapts a resource at the given path to {@link ServiceHighlights} model.
     *
     * @param path resource path
     * @return adapted ServiceHighlights model
     */
    private ServiceHighlights adapt(String path) {
        Resource resource = context.resourceResolver().getResource(path);
        assertNotNull(resource);

        ServiceHighlights model = resource.adaptTo(ServiceHighlights.class);
        assertNotNull(model);

        return model;
    }

    /**
     * Tests the model with valid services data.
     * Verifies that all fields are correctly mapped.
     */
    @Test
    void testValidServices() {
        ServiceHighlights model = adapt("/content/test/validComponent");

        List<ServiceHighlights.ServiceItem> services = model.getServicesList();

        assertNotNull(services);
        assertEquals(2, services.size());

        ServiceHighlights.ServiceItem first = services.get(0);
        assertEquals("/content/dam/icon1.png", first.getIcon());
        assertEquals("Service 1", first.getTitle());
        assertEquals("Subtitle 1", first.getSubtitle());
        assertEquals("#ff0000", first.getColor());

        ServiceHighlights.ServiceItem second = services.get(1);
        assertEquals("/content/dam/icon2.png", second.getIcon());
        assertEquals("Service 2", second.getTitle());
        assertEquals("Subtitle 2", second.getSubtitle());
        assertEquals("#00ff00", second.getColor());
    }

    /**
     * Tests the model when some properties are null or empty.
     * Ensures default handling does not break the model.
     */
    @Test
    void testNullValuesComponent() {
        ServiceHighlights model = adapt("/content/test/nullValuesComponent");

        List<ServiceHighlights.ServiceItem> services = model.getServicesList();

        assertNotNull(services);
        assertEquals(1, services.size());

        ServiceHighlights.ServiceItem item = services.get(0);
        assertNull(item.getIcon());
        assertEquals("", item.getTitle());
        assertNull(item.getSubtitle());
        assertNull(item.getColor());
    }

    /**
     * Tests the model when no services node is present.
     * Verifies that an empty list is returned instead of null.
     */
    @Test
    void testNoServicesComponent() {
        ServiceHighlights model = adapt("/content/test/noServicesComponent");

        List<ServiceHighlights.ServiceItem> services = model.getServicesList();

        assertNotNull(services);
        assertEquals(0, services.size());
    }

    /**
     * Tests the model with invalid service configuration.
     * Ensures properties are safely handled as null values.
     */
    @Test
    void testBadComponent() {
        ServiceHighlights model = adapt("/content/test/badComponent");

        List<ServiceHighlights.ServiceItem> services = model.getServicesList();

        assertNotNull(services);
        assertEquals(1, services.size());

        ServiceHighlights.ServiceItem item = services.get(0);
        assertNull(item.getIcon());
        assertNull(item.getTitle());
        assertNull(item.getSubtitle());
        assertNull(item.getColor());
    }
}