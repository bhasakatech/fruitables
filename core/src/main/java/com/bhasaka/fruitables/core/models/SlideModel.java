package com.bhasaka.fruitables.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.*;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(
        adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class SlideModel {

    @ValueMapValue
    private String image;

    @ValueMapValue
    private String label;

    public String getImage() {
        return image;
    }

    public String getLabel() {
        return label;
    }
}
