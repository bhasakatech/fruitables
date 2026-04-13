package com.bhasaka.fruitables.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.Default;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import java.util.List;

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

    public String getHomeLink() {
        return homeLink;
    }

    public String getLogoText() { return logoText; }
    public String getLogoImage() { return logoImage; }
    public List<MenuItem> getMenu() { return menu; }

    public boolean isEnableSearch() { return enableSearch; }
    public boolean isEnableCart() { return enableCart; }
    public boolean isEnableProfile() { return enableProfile; }
    public String getCartPagePath() { return cartPagePath; }
    public int getDefaultCartCount() {
        return defaultCartCount > 0 ? defaultCartCount : 3;
    }
}