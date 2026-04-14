package com.bhasaka.fruitables.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

/**
 * Sling Model representing a child menu item.
 *
 * <p>This model adapts from a {@link Resource} and holds
 * details for submenu items such as label and link.</p>
 */
@Model(
        adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class ChildItem {

    @ValueMapValue
    private String childLabel;

    @ValueMapValue
    private String childLink;

    /**
     * Returns the label of the child menu item.
     *
     * @return child menu item label
     */
    public String getChildLabel() {
        return childLabel;
    }

    /**
     * Returns the link associated with the child menu item.
     *
     * @return child menu item link
     */
    public String getChildLink() {
        return childLink;
    }
}