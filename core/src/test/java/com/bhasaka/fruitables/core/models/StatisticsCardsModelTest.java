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
class StatisticsCardsModelTest {

    private final AemContext context = new AemContext();
    private StatisticsCardsModel model;

    @BeforeEach
    void setUp() {
        context.addModelsForClasses(StatisticsCardsModel.class, StatCard.class);
        context.load().json("/statisticscards.json", "/content/stats");
        Resource resource = context.resourceResolver().getResource("/content/stats");
        assertNotNull(resource);
        model = resource.adaptTo(StatisticsCardsModel.class);
        assertNotNull(model);
    }

    @Test
    void testStatsItems() {
        List<StatCard> items = model.getStatsItems();
        assertNotNull(items);
        assertEquals(2, items.size());
        StatCard first = items.get(0);
        assertEquals("/content/dam/icons/apple.png", first.getIconImage());
        assertEquals("Apples Sold", first.getTitle());
        assertEquals("150", first.getValue());
        StatCard second = items.get(1);
        assertEquals("/content/dam/icons/orange.png", second.getIconImage());
        assertEquals("Oranges Sold", second.getTitle());
        assertEquals("200", second.getValue());
    }

    @Test
    void testEmptyStatsItems() {
        Resource resource = context.create().resource("/content/empty");
        StatisticsCardsModel emptyModel = resource.adaptTo(StatisticsCardsModel.class);
        assertNotNull(emptyModel);
        assertNotNull(emptyModel.getStatsItems());
        assertTrue(emptyModel.getStatsItems().isEmpty());
    }
}