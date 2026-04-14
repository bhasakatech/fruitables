package com.bhasaka.fruitables.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

/**
 * Sling Model representing a single statistics card item.
 *
 * <p>This model adapts from {@link Resource} and contains
 * properties such as icon, title, and value for display.</p>
 */
@Model(adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class StatCard {

    @ValueMapValue
    private String iconImage;

    @ValueMapValue
    private String title;

    @ValueMapValue
    private String value;

    /**
     * Returns the icon image path.
     *
     * @return icon image path
     */
    public String getIconImage() {
        return iconImage;
    }

    /**
     * Returns the title of the statistic card.
     *
     * @return card title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns the value displayed in the card.
     *
     * @return card value
     */
    public String getValue() {
        return value;
    }
}