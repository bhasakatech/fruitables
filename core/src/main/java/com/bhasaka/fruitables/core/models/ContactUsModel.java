package com.bhasaka.fruitables.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

/**
 * Sling Model for handling Contact Us component.
 * <p>
 * This model maps all configurable fields required for rendering
 * a Contact Us section including map, form fields, and contact details.
 * </p>
 */
@Model(adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class ContactUsModel {

    /**
     * URL for embedding map (e.g., Google Maps iframe).
     */
    @ValueMapValue
    private String mapUrl;

    /**
     * Main heading text of the Contact Us section.
     */
    @ValueMapValue
    private String headingText;

    /**
     * Description text displayed below the heading.
     */
    @ValueMapValue
    private String descriptionText;

    /**
     * Text for the primary button.
     */
    @ValueMapValue
    private String buttonText;

    /**
     * Placeholder text for the name input field.
     */
    @ValueMapValue
    private String namePlaceholder;

    /**
     * Placeholder text for the email input field.
     */
    @ValueMapValue
    private String emailPlaceholder;

    /**
     * Placeholder text for the message textarea.
     */
    @ValueMapValue
    private String messagePlaceholder;

    /**
     * Text for the submit button.
     */
    @ValueMapValue
    private String submitButtonText;

    /**
     * Label for the address section.
     */
    @ValueMapValue
    private String addressLabel;

    /**
     * Address details text.
     */
    @ValueMapValue
    private String addressText;

    /**
     * Label for the email section.
     */
    @ValueMapValue
    private String emailLabel;

    /**
     * Email address text.
     */
    @ValueMapValue
    private String emailText;

    /**
     * Label for the phone section.
     */
    @ValueMapValue
    private String phoneLabel;

    /**
     * Phone number value.
     */
    @ValueMapValue
    private long phoneNumber;

    /**
     * Returns the map URL.
     *
     * @return map URL string
     */
    public String getMapUrl() {
        return mapUrl;
    }

    /**
     * Returns the heading text.
     *
     * @return heading text
     */
    public String getHeadingText() {
        return headingText;
    }

    /**
     * Returns the description text.
     *
     * @return description text
     */
    public String getDescriptionText() {
        return descriptionText;
    }

    /**
     * Returns the button text.
     *
     * @return button text
     */
    public String getButtonText() {
        return buttonText;
    }

    /**
     * Returns the name placeholder text.
     *
     * @return name placeholder
     */
    public String getNamePlaceholder() {
        return namePlaceholder;
    }

    /**
     * Returns the email placeholder text.
     *
     * @return email placeholder
     */
    public String getEmailPlaceholder() {
        return emailPlaceholder;
    }

    /**
     * Returns the message placeholder text.
     *
     * @return message placeholder
     */
    public String getMessagePlaceholder() {
        return messagePlaceholder;
    }

    /**
     * Returns the submit button text.
     *
     * @return submit button text
     */
    public String getSubmitButtonText() {
        return submitButtonText;
    }

    /**
     * Returns the address label.
     *
     * @return address label
     */
    public String getAddressLabel() {
        return addressLabel;
    }

    /**
     * Returns the address text.
     *
     * @return address details
     */
    public String getAddressText() {
        return addressText;
    }

    /**
     * Returns the email label.
     *
     * @return email label
     */
    public String getEmailLabel() { return emailLabel; }

    /**
     * Returns the email text.
     *
     * @return email address
     */
    public String getEmailText() {
        return emailText;
    }

    /**
     * Returns the phone label.
     *
     * @return phone label
     */
    public String getPhoneLabel() {
        return phoneLabel;
    }

    /**
     * Returns the phone number.
     *
     * @return phone number
     */
    public long getPhoneNumber() {
        return phoneNumber;
    }
}