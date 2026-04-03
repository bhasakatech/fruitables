package com.bhasaka.fruitables.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(
        adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class TestimonialItemsModel {

    @ValueMapValue
    private String clientImage;

    @ValueMapValue
    private String clientName;

    @ValueMapValue
    private String profession;

    @ValueMapValue
    private String reviewText;

    @ValueMapValue
    private Integer rating;

    public String getClientImage() {
        return clientImage;
    }

    public String getClientName() {
        return clientName;
    }

    public String getProfession() {
        return profession;
    }

    public String getReviewText() {
        return reviewText;
    }

    public Integer getRating() {
        return rating;
    }
}
