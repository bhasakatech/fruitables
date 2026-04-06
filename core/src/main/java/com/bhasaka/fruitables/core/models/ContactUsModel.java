package com.bhasaka.fruitables.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = Resource.class,
    defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
    )
public class ContactUsModel {
    @ValueMapValue
    private String mapUrl;
    @ValueMapValue
    private String headingText;
    @ValueMapValue
    private String descriptionText;
    @ValueMapValue
    private String buttonText;

    @ValueMapValue
    private String namePlaceholder;
    @ValueMapValue
    private String emailPlaceholder;
    @ValueMapValue
    private String messagePlaceholder;
    @ValueMapValue
    private String submitButtonText;


    @ValueMapValue
    private String addressLabel;
    @ValueMapValue
    private String addressText;
    @ValueMapValue
    private String emailLabel;
    @ValueMapValue
    private String emailText;
    @ValueMapValue
    private String phoneLabel;
    @ValueMapValue
    private long phoneNumber;

    public String getMapUrl() {
        return mapUrl;
    }

    public String getHeadingText() {
        return headingText;
    }

    public String getDescriptionText() {
        return descriptionText;
    }

    public String getButtonText() {
        return buttonText;
    }

    public String getNamePlaceholder() {
        return namePlaceholder;
    }

    public String getEmailPlaceholder() {
        return emailPlaceholder;
    }

    public String getMessagePlaceholder() {
        return messagePlaceholder;
    }

    public String getSubmitButtonText() {
        return submitButtonText;
    }

    public String getAddressLabel() {
        return addressLabel;
    }

    public String getAddressText() {
        return addressText;
    }

    public String getEmailLabel() { return emailLabel; }

    public String getEmailText() {
        return emailText;
    }

    public String getPhoneLabel() {
        return phoneLabel;
    }

    public long getPhoneNumber() {
        return phoneNumber;
    }
}
