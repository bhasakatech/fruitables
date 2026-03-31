package com.bhasaka.fruitables.core.models;


import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;

import java.util.Collections;
import java.util.List;

@Model(
        adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class PromoBannerModel {

    @ChildResource
    private List<PromoCardModel> cards;

    public List<PromoCardModel> getCards() {
        return cards != null ? cards : Collections.emptyList();
    }
}
