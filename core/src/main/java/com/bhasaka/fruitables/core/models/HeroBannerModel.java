package com.bhasaka.fruitables.core.models;

import lombok.Getter;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.*;
import org.apache.sling.models.annotations.injectorspecific.*;
import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Model(
        adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL,
        resourceType = "fruitables/components/hero-banner"
)
@Getter
public class HeroBannerModel {

    @ValueMapValue
    private String subtitle;

    @ValueMapValue
    private String title;

    @ChildResource(name = "slides")
    private Resource slidesResource;

    private List<Slide> slides = new ArrayList<>();

    @PostConstruct
    protected void init() {
        if (slidesResource != null) {
            for (Resource res : slidesResource.getChildren()) {
                Slide slide = new Slide();
                slide.image = res.getValueMap().get("image", String.class);
                slide.label = res.getValueMap().get("label", String.class);
                slides.add(slide);
            }
        }
    }

    public String getSubtitle() {
        return subtitle != null ? subtitle : "100% Organic Foods";
    }

    public String getTitle() {
        return title != null ? title : "Organic Veggies & Fruits Foods";
    }

    @Getter
    public static class Slide {
        private String image;
        private String label;
    }
}
