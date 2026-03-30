package com.bhasaka.fruitables.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import java.util.List;

@Model(
        adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class MenuItem {

    @ValueMapValue
    private String label;

    @ValueMapValue
    private String link;

    // IMPORTANT: must match "./children"
    @ChildResource(name = "children")
    private List<ChildItem> children;

    public String getLabel() { return label; }
    public String getLink() { return link; }
    public List<ChildItem> getChildren() { return children; }
}