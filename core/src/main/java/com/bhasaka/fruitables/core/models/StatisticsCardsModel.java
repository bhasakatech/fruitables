package com.bhasaka.fruitables.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;

import java.util.Collections;
import java.util.List;

/**
 * Sling Model for Statistics Cards component.
 *
 * <p>This model adapts from {@link Resource} and provides a list
 * of statistic card items configured as child resources.</p>
 */
@Model(adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class StatisticsCardsModel {

    @ChildResource(name = "statsItems")
    private List<StatCard> statsItems;

    /**
     * Returns the list of statistic cards.
     *
     * <p>Returns an empty list if no items are configured.</p>
     *
     * @return list of {@link StatCard}
     */
    public List<StatCard> getStatsItems() {
        return statsItems != null ? statsItems : Collections.emptyList();
    }
}