package com.bhasaka.fruitables.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

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

    private List<TestimonialItemsModel> testimonialsList;

    @PostConstruct
    protected void init() {
        if (testimonials != null) {
            testimonialsList = testimonials;
        } else {
            testimonialsList = new ArrayList<>();
        }
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getTitle() {
        return title;
    }

    public List<TestimonialItemsModel> getTestimonials() {
        return testimonialsList;
    }
}
