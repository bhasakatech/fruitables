package com.bhasaka.fruitables.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class PolicyLink {
    @ValueMapValue private String linkText;
    @ValueMapValue private String linkUrl;

    public String getLinkText() { return linkText; }
    public String getLinkUrl() { return linkUrl; }
}
