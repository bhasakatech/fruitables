package com.bhasaka.fruitables.core.models;

import java.util.Collections;
import java.util.List;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(
        adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL,
        resourceType = "fruitables/components/hero-banner"
)
public class HeroBannerModel {

    @ChildResource
    private List<SlideModel> slides;

    @ValueMapValue
    private String subtitle;

    @ValueMapValue
    private String title;

    @ValueMapValue
    private String backgroundImage;

    public String getBackgroundImage() {
        return backgroundImage;
    }

    public String getSubtitle() {
        return subtitle;
    }

    public String getTitle() {
        return title;
    }

    public List<SlideModel> getSlides() {
        return slides != null ? slides : Collections.emptyList();
    }
}