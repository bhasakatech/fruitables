package com.bhasaka.fruitables.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(
        adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class ChildItem {

    @ValueMapValue
    private String childLabel;

    @ValueMapValue
    private String childLink;

    public String getChildLabel() {
        return childLabel;
    }

    public String getChildLink() {
        return childLink;
    }
}