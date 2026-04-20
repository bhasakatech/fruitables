package com.bhasaka.fruitables.core.models;


import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;

import java.util.Collections;
import java.util.List;
/**
 * Sling Model for handling Promo Banner component.
 * <p>
 * This model is responsible for retrieving a list of promo cards
 * configured as child resources under the banner component.
 * </p>
 */
@Model(
        adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class PromoBannerModel {
    /**
     * List of promo card child resources mapped to {@link PromoCardModel}.
     */
    @ChildResource
    private List<PromoCardModel> cards;
    /**
     * Returns the list of promo cards.
     * <p>
     * If no cards are configured, it returns an empty list
     * to avoid null pointer exceptions in HTL.
     * </p>
     *
     * @return list of {@link PromoCardModel}, or empty list if none exist
     */
    public List<PromoCardModel> getCards() {
        return cards != null ? cards : Collections.emptyList();
    }
}
