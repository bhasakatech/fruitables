package com.bhasaka.fruitables.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.*;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;


@Model(
        adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class ProductCFModelTag extends ProductCFModel {

    @ValueMapValue
    private String[] productTags;
    public String[] getProductTags() {
        return productTags != null ? productTags.clone() : new String[0];
    }
}
