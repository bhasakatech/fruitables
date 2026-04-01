package com.bhasaka.fruitables.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(
        adaptables = Resource.class,
        adapters = SearchBarModel.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class SearchBarModel {

    @ValueMapValue
    private String placeholder;

    @ValueMapValue
    private String buttonLabel;

    public String getPlaceholder() {
        return placeholder;
    }

    public String getButtonLabel() {
        return buttonLabel;
    }
}