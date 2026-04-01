package com.bhasaka.fruitables.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = Resource.class , defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ProductResource {

    @ValueMapValue
    private String cfPath;

    @ValueMapValue
    private String cardStyle;

    public String getCfPath() {
        return cfPath;
    }

    public String getCardStyle() {
        return cardStyle;
    }
}
