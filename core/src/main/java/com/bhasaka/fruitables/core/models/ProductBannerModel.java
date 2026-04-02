package com.bhasaka.fruitables.core.models;

import lombok.Getter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.*;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(
        adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)

@Getter
public class ProductBannerModel {

    @ValueMapValue
    private String heading;

    @ValueMapValue
    private String subheading;

    @ValueMapValue
    private String description;

    @ValueMapValue
    private String buttonLabel;

    @ValueMapValue
    private String buttonLink;

    @ValueMapValue
    private String productImage;

    @ValueMapValue
    private String mainPrice;

    @ValueMapValue
    private String decimalPrice;

    @ValueMapValue
    private String currency;

    @ValueMapValue
    private String unit;

}