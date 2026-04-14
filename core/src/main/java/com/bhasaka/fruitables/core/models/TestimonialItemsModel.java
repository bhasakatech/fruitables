package com.bhasaka.fruitables.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

/**
 * Sling Model for an individual testimonial item.
 *
 * <p>This model represents a single testimonial entry authored under
 * the "testimonials" node of the Testimonial Carousel component.</p>
 *
 * <p>It includes client details such as image, name, profession,
 * review text, and rating.</p>
 */
@Model(
        adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class TestimonialItemsModel {

    /**
     * Path to the client's image.
     */
    @ValueMapValue
    private String clientImage;

    /**
     * Name of the client.
     */
    @ValueMapValue
    private String clientName;

    /**
     * Profession of the client.
     */
    @ValueMapValue
    private String profession;

    /**
     * Review text provided by the client.
     */
    @ValueMapValue
    private String reviewText;

    /**
     * Rating given by the client.
     */
    @ValueMapValue
    private Integer rating;

    /**
     * Returns the client image path.
     *
     * @return client image path
     */
    public String getClientImage() {
        return clientImage;
    }

    /**
     * Returns the client name.
     *
     * @return client name
     */
    public String getClientName() {
        return clientName;
    }

    /**
     * Returns the client's profession.
     *
     * @return profession
     */
    public String getProfession() {
        return profession;
    }

    /**
     * Returns the review text.
     *
     * @return review text
     */
    public String getReviewText() {
        return reviewText;
    }

    /**
     * Returns the rating value.
     *
     * @return rating
     */
    public Integer getRating() {
        return rating;
    }
}