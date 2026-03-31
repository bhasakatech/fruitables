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
class ServiceHighlightsTest {

    private final AemContext context = new AemContext();

    @BeforeEach
    void setUp() {
        context.addModelsForClasses(ServiceHighlights.class);
        context.create().resource("/content/test/servicesComp/services/item1",
                "icon", "/content/dam/icon1.png",
                "title", "Service 1",
                "subtitle", "Sub 1",
                "color", "#ff0000"
        );
        context.create().resource("/content/test/servicesComp/services/item2",
                "icon", "/content/dam/icon2.png",
                "title", "Service 2",
                "subtitle", "Sub 2",
                "color", "#00ff00"
        );
    }

    private ServiceHighlights adapt(String path) {
        Resource res = context.resourceResolver().getResource(path);
        assertNotNull(res);
        ServiceHighlights model = res.adaptTo(ServiceHighlights.class);
        assertNotNull(model);
        return model;
    }

    @Test
    void testServicesLoaded() {

        ServiceHighlights model = adapt("/content/test/servicesComp");
        List<ServiceHighlights.ServiceItem> list = model.getServicesList();
        assertEquals(2, list.size());
        assertEquals("Service 1", list.get(0).getTitle());
        assertEquals("Service 2", list.get(1).getTitle());
    }
}