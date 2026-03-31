package com.bhasaka.fruitables.core.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import com.bhasaka.fruitables.core.testcontext.AppAemContext;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

@ExtendWith(AemContextExtension.class)
class FooterModelTest {

    private static final String FOOTER_JSON = "/com/bhasaka/fruitables/core/models/FooterModelTest.json";

    private final AemContext context = AppAemContext.newAemContext();

    @BeforeEach
    void setUp() {
        context.addModelsForClasses(
                FooterModel.class,
                FooterModel.IconLink.class,
                FooterModel.FooterColumn.class,
                FooterModel.ColumnLink.class);
    }

    @Test
    void testFooterModelWithConfiguredContent() throws IOException {
        loadJsonFixture(FOOTER_JSON, "/content");
        Resource footerResource = context.resourceResolver().getResource("/content/footer");

        assertNotNull(footerResource);
        FooterModel model = footerResource.adaptTo(FooterModel.class);

        assertNotNull(model);
        assertEquals("Stay Connected", model.getTitle());
        assertEquals("Fresh fruit deals every day", model.getDescription());
        assertEquals("Search Products", model.getSearchTitle());
        assertEquals("Subscribe", model.getButtonText());
        assertEquals("Fruitables 2026", model.getCopyrightText());
        assertEquals("Designed by Team", model.getDesignedByText());

        List<FooterModel.IconLink> iconLinkModels = model.getIconLinks();
        assertEquals(2, iconLinkModels.size());
        assertEquals("/content/dam/facebook.png", iconLinkModels.get(0).getIconUrl());
        assertEquals("", iconLinkModels.get(1).getIconUrl());

        List<FooterModel.FooterColumn> footerColumns = model.getFooterLinks();
        assertEquals(1, footerColumns.size());
        assertEquals("Useful Links", footerColumns.get(0).getColumnTitle());

        List<FooterModel.ColumnLink> columnLinks = footerColumns.get(0).getColumnLinks();
        assertEquals(2, columnLinks.size());
        assertEquals("About Us", columnLinks.get(0).getColumnText());
        assertEquals("/about-us", columnLinks.get(0).getColumnUrl());
        assertEquals("/content/dam/payments/visa.png", columnLinks.get(0).getPaymentImage());
        assertEquals("", columnLinks.get(1).getColumnText());
        assertEquals("", columnLinks.get(1).getColumnUrl());
        assertEquals("", columnLinks.get(1).getPaymentImage());
    }

    @Test
    void testFooterModelWithMissingContentReturnsEmptyValues() {
        Resource footerResource = context.create().resource("/content/footer-empty");

        FooterModel model = footerResource.adaptTo(FooterModel.class);

        assertNotNull(model);
        assertEquals("", model.getTitle());
        assertEquals("", model.getDescription());
        assertEquals("", model.getSearchTitle());
        assertEquals("", model.getButtonText());
        assertEquals("", model.getCopyrightText());
        assertEquals("", model.getDesignedByText());
        assertTrue(model.getIconLinks().isEmpty());
        assertTrue(model.getFooterLinks().isEmpty());
    }

    @Test
    void testNestedModelsReturnEmptyValuesWhenFieldsAreUnset() throws Exception {
        FooterModel.IconLink iconLink = new FooterModel.IconLink();
        FooterModel.FooterColumn footerColumn = new FooterModel.FooterColumn();
        FooterModel.ColumnLink columnLink = new FooterModel.ColumnLink();

        assertEquals("", iconLink.getIconUrl());
        assertEquals("", footerColumn.getColumnTitle());
        assertTrue(footerColumn.getColumnLinks().isEmpty());
        assertEquals("", columnLink.getColumnText());
        assertEquals("", columnLink.getColumnUrl());
        assertEquals("", columnLink.getPaymentImage());

        setField(iconLink, "iconUrl", "/content/dam/instagram.png");
        setField(footerColumn, "columnTitle", "Account");
        setField(columnLink, "columnText", "Track Order");
        setField(columnLink, "columnUrl", "/track-order");
        setField(columnLink, "paymentImage", "/content/dam/payments/mastercard.png");

        assertEquals("/content/dam/instagram.png", iconLink.getIconUrl());
        assertEquals("Account", footerColumn.getColumnTitle());
        assertEquals("Track Order", columnLink.getColumnText());
        assertEquals("/track-order", columnLink.getColumnUrl());
        assertEquals("/content/dam/payments/mastercard.png", columnLink.getPaymentImage());
    }

    private static void setField(Object target, String fieldName, Object value) throws Exception {
        Field field = target.getClass().getDeclaredField(fieldName);
        field.setAccessible(true);
        field.set(target, value);
    }

    private void loadJsonFixture(String classpathResource, String targetPath) throws IOException {
        Resource parentResource = context.resourceResolver().getResource(targetPath);
        if (parentResource == null) {
            parentResource = context.create().resource(targetPath);
        }

        try (InputStream inputStream = FooterModelTest.class.getResourceAsStream(classpathResource)) {
            assertNotNull(inputStream);

            Map<String, Object> jsonData = new ObjectMapper().readValue(
                    inputStream,
                    new TypeReference<Map<String, Object>>() { });

            for (Map.Entry<String, Object> entry : jsonData.entrySet()) {
                createResourceTree(parentResource, entry.getKey(), castToMap(entry.getValue()));
            }
        }
    }

    @SuppressWarnings("unchecked")
    private Map<String, Object> castToMap(Object value) {
        return (Map<String, Object>) value;
    }

    private void createResourceTree(Resource parentResource, String resourceName, Map<String, Object> resourceData) {
        Map<String, Object> properties = new LinkedHashMap<>();
        Map<String, Map<String, Object>> childResources = new LinkedHashMap<>();

        for (Map.Entry<String, Object> entry : resourceData.entrySet()) {
            if (entry.getValue() instanceof Map) {
                childResources.put(entry.getKey(), castToMap(entry.getValue()));
            } else {
                properties.put(entry.getKey(), entry.getValue());
            }
        }

        Resource currentResource = context.create().resource(parentResource, resourceName, properties);
        for (Map.Entry<String, Map<String, Object>> childEntry : childResources.entrySet()) {
            createResourceTree(currentResource, childEntry.getKey(), childEntry.getValue());
        }
    }
}
