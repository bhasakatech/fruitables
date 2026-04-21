package com.bhasaka.fruitables.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

/**
 * Sling Model for the Service Highlights component.
 *
 * <p>This model retrieves a list of service items authored under the
 * "services" child node in the component dialog.</p>
 *
 * <p>Each service item contains icon, title, subtitle, and color properties.</p>
 */
@Model(
        adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class ServiceHighlights {

    /**
     * Injects the child resources under "services" node.
     * Each child represents a ServiceItem.
     */
    @ChildResource(name = "services")
    private List<ServiceItem> services;

    /**
     * Processed list of service items.
     * Ensures a non-null list is always returned.
     */
    private List<ServiceItem> servicesList;

    /**
     * Initializes the services list after injection.
     *
     * <p>If no services are authored, initializes an empty list
     * to avoid NullPointerException.</p>
     */
    @PostConstruct
    protected void init() {
        servicesList = services != null ? services : new ArrayList<>();
    }

    /**
     * Returns the list of service items.
     *
     * @return list of ServiceItem objects, never null
     */
    public List<ServiceItem> getServicesList() {
        return new ArrayList<>(servicesList);
    }

    /**
     * Inner Sling Model representing a single service item.
     *
     * <p>Each item corresponds to a child resource under "services".</p>
     */
    @Model(
            adaptables = Resource.class,
            defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
    )
    public static class ServiceItem {

        /**
         * Path to the icon image.
         */
        @ValueMapValue
        private String icon;

        /**
         * Title of the service.
         */
        @ValueMapValue
        private String title;

        /**
         * Subtitle or short description of the service.
         */
        @ValueMapValue
        private String subtitle;

        /**
         * Color associated with the service (e.g., hex code).
         */
        @ValueMapValue
        private String color;

        /**
         * Returns the icon path.
         *
         * @return icon path
         */
        public String getIcon() {
            return icon;
        }

        /**
         * Returns the service title.
         *
         * @return title of the service
         */
        public String getTitle() {
            return title;
        }

        /**
         * Returns the service subtitle.
         *
         * @return subtitle text
         */
        public String getSubtitle() {
            return subtitle;
        }

        /**
         * Returns the color value.
         *
         * @return color code
         */
        
        public String getColor() {
            return color;
        }
    }
}