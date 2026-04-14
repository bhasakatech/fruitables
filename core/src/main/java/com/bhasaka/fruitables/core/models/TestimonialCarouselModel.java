package com.bhasaka.fruitables.core.models;

import java.util.List;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

/**
 * Sling Model for the Testimonial Carousel component.
 *
 * <p>This model retrieves the subtitle, title, and a list of testimonial items
 * authored under the "testimonials" child node in the component dialog.</p>
 */
@Model(
        adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class TestimonialCarouselModel {

    /**
     * Subtitle text displayed above the testimonial carousel.
     */
    @ValueMapValue
    private String subtitle;

    /**
     * Main title of the testimonial carousel section.
     */
    @ValueMapValue
    private String title;

    /**
     * List of testimonial items authored under the "testimonials" node.
     */
    @ChildResource(name = "testimonials")
    private List<TestimonialItemsModel> testimonials;

    /**
     * Returns the subtitle of the carousel.
     *
     * @return subtitle text
     */
    public String getSubtitle() {
        return subtitle;
    }

    /**
     * Returns the title of the carousel.
     *
     * @return title text
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns the list of testimonial items.
     *
     * @return list of TestimonialItemsModel objects
     */
    public List<TestimonialItemsModel> getTestimonials() {
        return testimonials;
    }
}