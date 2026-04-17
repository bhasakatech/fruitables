package com.bhasaka.fruitables.core.models;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

/**
 * Sling Model for representing an individual Promo Card.
 * <p>
 * This model maps properties from a resource (usually a child node
 * in a Promo Banner component) and provides data for rendering
 * promotional cards in the UI.
 * </p>
 */
@Model(
        adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class PromoCardModel {

    /**
     * Path or reference to the card image.
     */
    @ValueMapValue
    private String image;

    /**
     * Subtitle text displayed on the promo card.
     */
    @ValueMapValue
    private String subtitle;

    /**
     * Offer text (e.g., discount or promotion details).
     */
    @ValueMapValue
    private String offer;

    /**
     * Overlay style applied on the card (e.g., gradient, opacity).
     */
    @ValueMapValue
    private String overlayStyle;

    /**
     * Background color for the promo card.
     */
    @ValueMapValue
    private String bgColor;

    /**
     * Returns the image path of the promo card.
     *
     * @return image path as String
     */
    public String getImage() {
        return image;
    }

    /**
     * Returns the subtitle of the promo card.
     *
     * @return subtitle text
     */
    public String getSubtitle() {
        return subtitle;
    }

    /**
     * Returns the offer text of the promo card.
     *
     * @return offer details
     */
    public String getOffer() {
        return offer;
    }

    /**
     * Returns the overlay style of the promo card.
     *
     * @return overlay style as String
     */
    public String getOverlayStyle() {
        return overlayStyle;
    }

    /**
     * Returns the background color of the promo card.
     *
     * @return background color value
     */
    public String getBgColor() {
        return bgColor;
    }
}