package com.bhasaka.fruitables.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

/**
 * Sling Model representing the 404 Error Page component.
 * <p>
 * This model retrieves author-configured properties such as image,
 * title, subtitle, description, and call-to-action (CTA) details
 * to render a custom error page in the UI.
 * </p>
 */
@Model(
        adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class Error404PageModel {

    /** The image for the 404 error page. */
    @ValueMapValue
    private String image;

    /** The title for the 404 error page. */
    @ValueMapValue
    private String title;

    /** The subtitle for the 404 error page. */
    @ValueMapValue
    private String subtitle;

    /** The description for the 404 error page. */
    @ValueMapValue
    private String description;

    /** The text for the call-to-action (CTA) button. */
    @ValueMapValue
    private String ctaText;

    /** The link for the call-to-action (CTA) button. */
    @ValueMapValue
    private String ctaLink;

    /* Getters for the model properties. */
    public String getImage() {
        return image;
    }

    /* Getters for the model properties. */
    public String getTitle() {
        return title;
    }

    /* Getters for the model properties. */
    public String getSubtitle() {
        return subtitle;
    }

    /* Getters for the model properties. */
    public String getDescription() {
        return description;
    }

    /* Getters for the model properties. */
    public String getCtaText() {
        return ctaText;
    }

    /* Getters for the model properties. */
    public String getCtaLink() {
        return ctaLink;
    }
}