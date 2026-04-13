package com.bhasaka.fruitables.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(
        adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class ProductReviewFeedbackFormModel {

    @ValueMapValue
    private String sectionTitle;

    @ValueMapValue
    private String namePlaceholder;

    @ValueMapValue
    private String emailPlaceholder;

    @ValueMapValue
    private String reviewPlaceholder;

    @ValueMapValue
    private String ratingLabel;

    @ValueMapValue
    private String buttonText;

    public String getSectionTitle() {
        return sectionTitle;
    }

    public String getNamePlaceholder() {
        return namePlaceholder;
    }

    public String getEmailPlaceholder() {
        return emailPlaceholder;
    }

    public String getReviewPlaceholder() {
        return reviewPlaceholder;
    }

    public String getRatingLabel() {
        return ratingLabel;
    }

    public String getButtonText() {
        return buttonText;
    }
}
