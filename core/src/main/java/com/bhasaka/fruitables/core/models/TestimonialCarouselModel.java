package com.bhasaka.fruitables.core.models;

import java.util.List;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(
        adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class TestimonialCarouselModel {

    @ValueMapValue
    private String subtitle;

    @ValueMapValue
    private String title;

    @ChildResource(name = "testimonials")
    private List<TestimonialItemsModel> testimonials;

    public String getSubtitle() {
        return subtitle;
    }

    public String getTitle() {
        return title;
    }

    public List<TestimonialItemsModel> getTestimonials() {
        return testimonials;
    }
}
