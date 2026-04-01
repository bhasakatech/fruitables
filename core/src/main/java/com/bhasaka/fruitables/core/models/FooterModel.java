package com.bhasaka.fruitables.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.*;
import org.apache.sling.models.annotations.injectorspecific.*;
import java.util.Collections;
import java.util.List;


@Model(
        adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class FooterModel {

    @ValueMapValue
    private String title;

    @ValueMapValue
    private String subTitle;

    @ValueMapValue
    private String searchTitle;

    @ValueMapValue
    private String buttonText;

    @ChildResource(name = "iconLinks")
    private List<IconLink> iconLinks;
        
    @ChildResource(name = "footerLinks")
    private List<FooterColumn> footerLinks;

    @ValueMapValue
    private String copyrightText;

    @ValueMapValue
    private String designedByText;

    @Model(adaptables = Resource.class,defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
    public static class IconLink {

        @ValueMapValue
        private String iconUrl;

        public String getIconUrl() {
            return iconUrl;
        }
    }

    @Model(adaptables = Resource.class,defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
    public static class FooterColumn {

        @ValueMapValue
        private String columnTitle;

        @ValueMapValue
        private String columnDescription;

        @ValueMapValue
        private String readMoreText;

        @ValueMapValue
        private String readMoreUrl;

        @ChildResource(name = "columnLinks")
        private List<ColumnLink> columnLinks;

        public List<ColumnLink> getColumnLinks() {
            if (columnLinks == null) {
                return Collections.emptyList();
            }
            return columnLinks;
        }

        public String getColumnTitle() {
            return columnTitle;
        }

        public String getColumnDescription() {
            return columnDescription;
        }

        public String getReadMoreText() {
            return readMoreText;
        }

        public String getReadMoreUrl() {
            return readMoreUrl;
        }
    }

    @Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
    public static class ColumnLink {

        @ValueMapValue
        private String columnText;

        @ValueMapValue
        private String columnUrl;

        @ValueMapValue
        private String paymentImage;

        public String getPaymentImage() {
            return paymentImage;
        }

        public String getColumnText() {
            return columnText;
        }

        public String getColumnUrl() {
            return columnUrl;
        }
    }

    public List<IconLink> getIconLinks() {
        if (iconLinks == null) {
            return Collections.emptyList();
        }
        return iconLinks;
    }

    public List<FooterColumn> getFooterLinks() {
        if (footerLinks == null) {
            return Collections.emptyList();
        }
        return footerLinks;
    }

    public String getTitle() {
        return title;
    }

    public String getSubTitle() {
        return subTitle;
    }

    public String getSearchTitle() {
        return searchTitle;
    }

    public String getButtonText() {
        return buttonText;
    }

    public String getCopyrightText() {
        return copyrightText;
    }

    public String getDesignedByText() {
        return designedByText;
    }
}
