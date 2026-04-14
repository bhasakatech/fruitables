package com.bhasaka.fruitables.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import java.util.List;

/**
 * Sling Model for representing the Bottom Header component.
 *
 * <p>
 * This model adapts from a {@link Resource} and is used to fetch and expose
 * header-related properties such as logo, navigation menu, and feature toggles
 * (search, cart, profile) configured in the AEM dialog.
 * </p>
 *
 * <p>
 * It also provides default values for certain properties when not explicitly configured.
 * </p>
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class BottomHeaderModel {

    @ValueMapValue
    private String logoText;

    @ValueMapValue
    private String logoImage;

    @ChildResource(name = "menu")
    private List<MenuItem> menu;


    @ValueMapValue
    @Default(booleanValues = true)
    private boolean enableSearch;

    @ValueMapValue
    @Default(booleanValues = true)
    private boolean enableCart;

    @ValueMapValue
    @Default(booleanValues = true)
    private boolean enableProfile;

    @ValueMapValue
    private String cartPagePath;

    @ValueMapValue
    @Default(intValues = 3)
    private int defaultCartCount;


    @ValueMapValue
    private String homeLink;

    /**
     * Returns the home page link.
     *
     * @return home page path
     */
    public String getHomeLink() {
        return homeLink;
    }

    /**
     * Returns the logo text.
     *
     * @return logo text
     */
    public String getLogoText() {
        return logoText;
    }

    /**
     * Returns the logo image path.
     *
     * @return logo image path
     */
    public String getLogoImage() {
        return logoImage;
    }

    /**
     * Returns the list of menu items.
     *
     * @return list of {@link MenuItem}
     */
    public List<MenuItem> getMenu() {
        return menu;
    }

    /**
     * Checks if search functionality is enabled.
     *
     * @return true if enabled, false otherwise
     */
    public boolean isEnableSearch() {
        return enableSearch;
    }

    /**
     * Checks if cart functionality is enabled.
     *
     * @return true if enabled, false otherwise
     */
    public boolean isEnableCart() {
        return enableCart;
    }

    /**
     * Checks if profile functionality is enabled.
     *
     * @return true if enabled, false otherwise
     */
    public boolean isEnableProfile() {
        return enableProfile;
    }

    /**
     * Returns the cart page path.
     *
     * @return cart page path
     */
    public String getCartPagePath() {
        return cartPagePath;
    }

    /**
     * Returns the default cart count.
     *
     * <p>
     * If the configured value is less than or equal to 0,
     * a fallback value of 3 is returned.
     * </p>
     *
     * @return default cart count
     */
    public int getDefaultCartCount() {
        return defaultCartCount > 0 ? defaultCartCount : 3;
    }
}