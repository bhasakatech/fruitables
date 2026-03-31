package com.bhasaka.fruitables.core.models;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.*;
import org.apache.sling.models.annotations.injectorspecific.*;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Model(adaptables = Resource.class)
public class ServiceHighlights {

    @ChildResource(name = "services")
    private Resource servicesResource;


    private List<ServiceItem> servicesList;

    @PostConstruct
    protected void init() {
        servicesList = new ArrayList<>();

        if (servicesResource == null) {
            return;
        }

        for (Resource child : servicesResource.getChildren()) {
            ServiceItem item = child.adaptTo(ServiceItem.class);
            if (item != null) {
                servicesList.add(item);
            }
        }
    }

    public List<ServiceItem> getServicesList() {
        return servicesList;
    }

    @Model(adaptables = Resource.class)
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
            return StringUtils.defaultString(icon);
        }

        public String getTitle() {
            return StringUtils.defaultString(title);
        }

        public String getSubtitle() {
            return StringUtils.defaultString(subtitle);
        }

        public String getColor() {
            return StringUtils.defaultString(color);
        }
    }
}