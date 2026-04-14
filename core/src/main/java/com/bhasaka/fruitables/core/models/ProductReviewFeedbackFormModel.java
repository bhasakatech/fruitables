package com.bhasaka.fruitables.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
/**
 * Sling Model representing the Product Review Feedback Form component.
 * <p>
 * This model retrieves author-configured properties from the resource,
 * including section title, input placeholders, review content, rating label,
 * and submit button text for rendering in the HTL component.
 * </p>
 */
@Model(
        adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class ProductReviewFeedbackFormModel {

    /** Section title displayed at the top of the review form */
    @ValueMapValue
    private String sectionTitle;

    /** Placeholder text for the name input field */
    @ValueMapValue
    private String namePlaceholder;

    /** Placeholder text for the email input field */
    @ValueMapValue
    private String emailPlaceholder;

    /** Rich text or placeholder content displayed above the review textarea */
    @ValueMapValue
    private String reviewPlaceholder;

    /** Label text for the rating section */
    @ValueMapValue
    private String ratingLabel;

    /** Text displayed on the submit button */
    @ValueMapValue
    private String buttonText;

    /**
     * Returns the section title.
     *
     * @return section title of the review form
     */
    public String getSectionTitle() {
        return sectionTitle;
    }

    /**
     * Returns the placeholder for the name input field.
     *
     * @return name input placeholder text
     */
    public String getNamePlaceholder() {
        return namePlaceholder;
    }

    /**
     * Returns the placeholder for the email input field.
     *
     * @return email input placeholder text
     */
    public String getEmailPlaceholder() {
        return emailPlaceholder;
    }

    /**
     * Returns the review placeholder or description content.
     *
     * @return review placeholder text or HTML content
     */
    public String getReviewPlaceholder() {
        return reviewPlaceholder;
    }

    /**
     * Returns the rating label.
     *
     * @return rating section label
     */
    public String getRatingLabel() {
        return ratingLabel;
    }

    /**
     * Returns the submit button text.
     *
     * @return submit button label
     */
    public String getButtonText() {
        return buttonText;
    }
}