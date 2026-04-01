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
public class ServiceHighlights {

    @ChildResource(name = "services")
    private List<ServiceItem> services;

    private List<ServiceItem> servicesList;

    @PostConstruct
    protected void init() {
        servicesList = services != null ? services : new ArrayList<>();
    }

    public List<ServiceItem> getServicesList() {
        return servicesList;
    }

    @Model(
            adaptables = Resource.class,
            defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
    )
    public static class ServiceItem {

        @ValueMapValue
        private String icon;

        @ValueMapValue
        private String title;

        @ValueMapValue
        private String subtitle;

        @ValueMapValue
        private String color;

        public String getIcon() {
            return icon;
        }

        public String getTitle() {
            return title;
        }

        public String getSubtitle() {
            return subtitle;
        }

        public String getColor() {
            return color;
        }
    }
}
