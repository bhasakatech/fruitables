package com.bhasaka.fruitables.core.models;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.List;
import org.apache.sling.api.resource.Resource;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import io.wcm.testing.mock.aem.junit5.AemContext;
import io.wcm.testing.mock.aem.junit5.AemContextExtension;

/**
 * Unit test class for {@link FooterModel}.
 *
 * Validates footer content rendering, nested child resources,
 * empty state handling, and footer column behavior.
 */
@ExtendWith(AemContextExtension.class)
class FooterModelTest {

    /**
     * AEM mock context for Sling model testing.
     */
    private final AemContext context = new AemContext();

    /**
     * Initializes mock context before each test.
     *
     * Registers footer models and loads JSON test content.
     */
    @BeforeEach
    void setUp() {
        context.addModelsForClasses(
                FooterModel.class,
                FooterModel.IconLink.class,
                FooterModel.FooterColumn.class,
                FooterModel.ColumnLink.class);
        context.load().json("/FooterModelTest.json", "/content");
    }

    /**
     * Tests successful adaptation of populated footer resource
     * into FooterModel and validates all authored values.
     */
    @Test
    void testAdaptToFooterModels() {
        Resource footerResource = context.resourceResolver().getResource("/content/footer");

        FooterModel model = footerResource.adaptTo(FooterModel.class);

        assertNotNull(model);
        assertEquals("Stay Connected", model.getTitle());
        assertEquals("Fresh fruit deals every day", model.getSubTitle());
        assertEquals("Search Products", model.getSearchTitle());
        assertEquals("Subscribe", model.getButtonText());
        assertEquals("Fruitables 2026", model.getCopyrightText());
        assertEquals("Designed by Team", model.getDesignedByText());

        List<FooterModel.IconLink> iconLinks = model.getIconLinks();
        assertEquals(2, iconLinks.size());
        assertEquals("/content/dam/facebook.png", iconLinks.get(0).getIconUrl());
        assertEquals("/content/dam/instagram.png", iconLinks.get(1).getIconUrl());

        List<FooterModel.FooterColumn> footerColumns = model.getFooterLinks();
        assertEquals(2, footerColumns.size());

        FooterModel.FooterColumn usefulLinksColumn = footerColumns.get(0);
        assertEquals("Useful Links", usefulLinksColumn.getColumnTitle());
        assertEquals("Everything you need for your next order.", usefulLinksColumn.getColumnDescription());
        assertEquals("Read more", usefulLinksColumn.getReadMoreText());
        assertEquals("/useful-links", usefulLinksColumn.getReadMoreUrl());

        List<FooterModel.ColumnLink> usefulLinks = usefulLinksColumn.getColumnLinks();
        assertEquals(2, usefulLinks.size());
        assertEquals("About Us", usefulLinks.get(0).getColumnText());
        assertEquals("/about-us", usefulLinks.get(0).getColumnUrl());
        assertEquals("/content/dam/payments/visa.png", usefulLinks.get(0).getPaymentImage());
        assertEquals("Contact", usefulLinks.get(1).getColumnText());
        assertEquals("/contact", usefulLinks.get(1).getColumnUrl());
        assertNull(usefulLinks.get(1).getPaymentImage());

        FooterModel.FooterColumn paymentColumn = footerColumns.get(1);
        assertEquals("Payment Methods", paymentColumn.getColumnTitle());
        assertNull(paymentColumn.getColumnDescription());
        assertNull(paymentColumn.getReadMoreText());
        assertNull(paymentColumn.getReadMoreUrl());
        assertEquals(1, paymentColumn.getColumnLinks().size());
        assertNull(paymentColumn.getColumnLinks().get(0).getColumnText());
        assertNull(paymentColumn.getColumnLinks().get(0).getColumnUrl());
        assertEquals("/content/dam/payments/mastercard.png",
                paymentColumn.getColumnLinks().get(0).getPaymentImage());
    }

    /**
     * Tests adaptation of empty footer resource
     * and validates null/empty default responses.
     */
    @Test
    void testAdaptToFooterModel() {
        Resource footerResource = context.resourceResolver().getResource("/content/footer-empty");

        FooterModel model = footerResource.adaptTo(FooterModel.class);

        assertNotNull(model);
        assertNull(model.getTitle());
        assertNull(model.getSubTitle());
        assertNull(model.getSearchTitle());
        assertNull(model.getButtonText());
        assertNull(model.getCopyrightText());
        assertNull(model.getDesignedByText());
        assertTrue(model.getIconLinks().isEmpty());
        assertTrue(model.getFooterLinks().isEmpty());
    }

    /**
     * Tests footer column behavior when no column links exist.
     */
    @Test
    void testFooterColumnReturns() {
        Resource columnResource = context.resourceResolver().getResource("/content/footer-column-without-links");

        FooterModel.FooterColumn footerColumn = columnResource.adaptTo(FooterModel.FooterColumn.class);

        assertNotNull(footerColumn);
        assertEquals("Customer Care", footerColumn.getColumnTitle());
        assertTrue(footerColumn.getColumnLinks().isEmpty());
    }
}