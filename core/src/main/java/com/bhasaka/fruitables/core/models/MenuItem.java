package com.bhasaka.fruitables.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import java.util.List;

/**
 * Sling Model representing a menu item in the header component.
 *
 * <p>This model adapts from a {@link Resource} and provides
 * details such as label, link, and nested child menu items.</p>
 */
@Model(
        adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class MenuItem {

    @ValueMapValue
    private String label;

    @ValueMapValue
    private String link;

    @ChildResource(name = "children")
    private List<ChildItem> children;

    /**
     * Returns the label of the menu item.
     *
     * @return menu item label
     */
    public String getLabel() { return label; }

    /**
     * Returns the link associated with the menu item.
     *
     * @return menu item link
     */
    public String getLink() { return link; }

    /**
     * Returns the list of child menu items.
     *
     * @return list of {@link ChildItem}
     */
    public List<ChildItem> getChildren() { return children; }
}