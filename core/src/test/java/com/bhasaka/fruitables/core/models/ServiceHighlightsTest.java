package com.bhasaka.fruitables.core.models;

import java.util.List;

import org.apache.sling.api.resource.Resource;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

@ExtendWith(AemContextExtension.class)
class ServiceHighlightsTest {

    private final AemContext context = new AemContext();

    @BeforeEach
    void setUp() {
        context.addModelsForClasses(ServiceHighlights.class);
        context.load().json("/service-highlights.json", "/content/test");
    }

    private ServiceHighlights adapt(String path) {
        Resource res = context.resourceResolver().getResource(path);
        assertNotNull(res);
        ServiceHighlights model = res.adaptTo(ServiceHighlights.class);
        assertNotNull(model);
        return model;
    }

    @Test
    void testValidServices() {
        ServiceHighlights model = adapt("/content/test/validComponent");
        List<ServiceHighlights.ServiceItem> list = model.getServicesList();
        assertEquals(2, list.size());
        assertEquals("Service 1", list.get(0).getTitle());
        assertEquals("/content/dam/icon1.png", list.get(0).getIcon());
        assertEquals("Subtitle 1", list.get(0).getSubtitle());
        assertEquals("#ff0000", list.get(0).getColor());
    }


}
