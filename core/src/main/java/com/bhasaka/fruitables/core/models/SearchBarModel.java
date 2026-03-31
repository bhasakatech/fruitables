package com.bhasaka.fruitables.core.models;

import org.apache.sling.models.annotations.*;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(
        adaptables = org.apache.sling.api.resource.Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class SearchBarModel {

    @ValueMapValue
    private String placeholder;

    @ValueMapValue
    private String buttonLabel;

    public String getPlaceholder() {
        return placeholder != null ? placeholder : "Search";
    }

    public String getButtonLabel() {
        return buttonLabel != null ? buttonLabel : "Submit Now";
    }
}
