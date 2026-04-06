package com.bhasaka.fruitables.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class StatCard {

    @ValueMapValue
    private String iconImage;

    @ValueMapValue
    private String title;

    @ValueMapValue
    private String value;

    public String getIconImage() {
        return iconImage;
    }

    public String getTitle() {
        return title;
    }

    public String getValue() {
        return value;
    }
}
