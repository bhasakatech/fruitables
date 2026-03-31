package com.bhasaka.fruitables.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(
        adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class PromoCardModel {

    @ValueMapValue
    private String image;

    @ValueMapValue
    private String subtitle;

    @ValueMapValue
    private String offer;

    @ValueMapValue
    private String overlayStyle;

    @ValueMapValue
    private String bgColor;

    // Getters
    public String getImage() {
        return image;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getOffer() {
        return offer;
    }

    public String getOverlayStyle() {
        return overlayStyle;
    }

    public String getBgColor() {
        return bgColor;
    }
}
