package com.bhasaka.fruitables.core.models;

import org.apache.commons.lang3.StringUtils;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.*;
import org.apache.sling.models.annotations.injectorspecific.*;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Model(
        adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class FooterModel {

    /* ================= BASIC FIELDS ================= */
    @ValueMapValue
    private String title;

    @ValueMapValue
    private String description;

    @ValueMapValue
    private String searchTitle;

    @ValueMapValue
    private String buttonText;

    public String getTitle() {
        return StringUtils.defaultString(title);
    }

    public String getDescription() {
        return StringUtils.defaultString(description);
    }

    public String getSearchTitle() {
        return StringUtils.defaultString(searchTitle);
    }

    public String getButtonText() {
        return StringUtils.defaultString(buttonText);
    }


    /* ================= ICON LINKS ================= */
    @ChildResource(name = "iconLinks")
    private List<Resource> iconLinks;

    public List<IconLink> getIconLinks() {
        if (iconLinks == null) {
            return Collections.emptyList();
        }
        return iconLinks.stream()
                .map(res -> res.adaptTo(IconLink.class))
                .collect(Collectors.toList());
    }

    @Model(adaptables = Resource.class,defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
    public static class IconLink {

        @ValueMapValue
        private String iconUrl;

        public String getIconUrl() {
            return StringUtils.defaultString(iconUrl);
        }
    }


    /* ================= FOOTER COLUMNS ================= */
    @ChildResource(name = "footerLinks")
    private List<Resource> footerLinks;

    public List<FooterColumn> getFooterLinks() {
        if (footerLinks == null) {
            return Collections.emptyList();
        }
        return footerLinks.stream()
                .map(res -> res.adaptTo(FooterColumn.class))
                .collect(Collectors.toList());
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
        private List<Resource> columnLinks;

        public String getColumnTitle() {
            return StringUtils.defaultString(columnTitle);
        }

        public String getColumnDescription() {
            return StringUtils.defaultString(columnDescription);
        }

        public String getReadMoreText() {
            return StringUtils.defaultString(readMoreText);
        }

        public String getReadMoreUrl() {
            return StringUtils.defaultString(readMoreUrl);
        }

        public List<ColumnLink> getColumnLinks() {
            if (columnLinks == null) {
                return Collections.emptyList();
            }
            return columnLinks.stream()
                    .map(res -> res.adaptTo(ColumnLink.class))
                    .collect(Collectors.toList());
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
            return StringUtils.defaultString(paymentImage);
        }

        public String getColumnText() {
            return StringUtils.defaultString(columnText);
        }

        public String getColumnUrl() {
            return StringUtils.defaultString(columnUrl);
        }
    }


    /* ================= COPYRIGHT ================= */
    @ValueMapValue
    private String copyrightText;

    @ValueMapValue
    private String designedByText;

    public String getCopyrightText() {
        return StringUtils.defaultString(copyrightText);
    }

    public String getDesignedByText() {
        return StringUtils.defaultString(designedByText);
    }
}
