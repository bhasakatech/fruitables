package com.bhasaka.fruitables.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.*;
import org.apache.sling.models.annotations.injectorspecific.*;
import java.util.Collections;
import java.util.List;

/**
 * Sling Model for the Footer component.
 *
 * This model provides authored footer content including titles, search section,
 * social/media icon links, footer navigation columns, and copyright details.
 */
@Model(
        adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class FooterModel {
    /**
     * Stores the main title of the footer section.
     */
    @ValueMapValue
    private String title;
    /**
     * Stores the subtitle of the footer section.
     */
    @ValueMapValue
    private String subTitle;
    /**
     * Stores the title for the search section in the footer.
     */
    @ValueMapValue
    private String searchTitle;
    /**
     * Stores the button text for the search section in the footer.
     */
    @ValueMapValue
    private String buttonText;
    /**
     * Child resource list for social/media icon links in the footer.
     */
    @ChildResource(name = "iconLinks")
    private List<IconLink> iconLinks;
    /**
     * Child resource list for footer navigation columns.
     */ 
    @ChildResource(name = "footerLinks")
    private List<FooterColumn> footerLinks;
    /**
     * Stores the copyright text to be displayed in the footer.
     */
    @ValueMapValue
    private String copyrightText;
    /**
     * Stores the "Designed by" text to be displayed in the footer.
     */
    @ValueMapValue
    private String designedByText;

    /**
     * Represents an icon link item in the footer.
     */
    @Model(adaptables = Resource.class,defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
    public static class IconLink {
        /**
         * Stores the URL/path for the social/media icon.
         */
        @ValueMapValue
        private String iconUrl;

        /**
         * Returns icon URL.
         *
         * @return icon URL/path
         */
        public String getIconUrl() {
            return iconUrl;
        }
    }
    
    /**
     * Represents a footer content column.
     */
    @Model(adaptables = Resource.class,defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
    public static class FooterColumn {
        /**
         * Stores the title for the footer column.
         */
        @ValueMapValue
        private String columnTitle;
        /**
         * Stores the description for the footer column.
         */
        @ValueMapValue
        private String columnDescription;
        /**
         * Stores the "read more" text for the footer column.
         */
        @ValueMapValue
        private String readMoreText;
        /**
         * Stores the URL for the "read more" link in the footer column.
         */
        @ValueMapValue
        private String readMoreUrl;
        /**
         * Child resource list for links within the footer column.
         */
        @ChildResource(name = "columnLinks")
        private List<ColumnLink> columnLinks;

        /**
         * Returns footer column links.
         *
         * @return list of column links or empty list if unavailable
         */
        public List<ColumnLink> getColumnLinks() {
            if (columnLinks == null) {
                return Collections.emptyList();
            }
            return columnLinks;
        }
        /**
         * Returns column title.
         *
         * @return footer column title
         */
        public String getColumnTitle() {
            return columnTitle;
        }
        /**
         * Returns column description.
         *
         * @return footer column description
         */
        public String getColumnDescription() {
            return columnDescription;
        }
        /**
         * Returns read more text.
         *
         * @return read more label
         */
        public String getReadMoreText() {
            return readMoreText;
        }
        /**
         * Returns read more URL.
         *
         * @return navigation URL
         */
        public String getReadMoreUrl() {
            return readMoreUrl;
        }
    }

    /**
     * Represents a link item within a footer column.
     */
    @Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
    public static class ColumnLink {

        /**
         * Stores the text for the column link.
         */
        @ValueMapValue
        private String columnText;
        /**
         * Stores the URL for the column link.
         */
        @ValueMapValue
        private String columnUrl;
        /**
         * Stores the image URL for the payment method icon associated with the column link.
         */
        @ValueMapValue
        private String paymentImage;

        /**
         * Returns payment image URL.
         *
         * @return payment image URL/path
         */
        public String getPaymentImage() {
            return paymentImage;
        }

        /**
         * Returns column link text.
         *
         * @return column link label
         */
        public String getColumnText() {
            return columnText;
        }

        /**
         * Returns column link URL.
         *
         * @return navigation URL
         */ 
        public String getColumnUrl() {
            return columnUrl;
        }
    }
    /**
     * Returns list of social/media icon links for the footer.
     *
     * @return list of icon links or empty list if unavailable
     */
    public List<IconLink> getIconLinks() {
        if (iconLinks == null) {
            return Collections.emptyList();
        }
        return iconLinks;
    }
    /**
     * Returns footer navigation columns.
     *
     * @return list of footer columns or empty list if unavailable
     */
    public List<FooterColumn> getFooterLinks() {
        if (footerLinks == null) {
            return Collections.emptyList();
        }
        return footerLinks;
    }
    /**
     * Returns the main title of the footer.
     *
     * @return footer title
     */
    public String getTitle() {
        return title;
    }
    /**
     * Returns the subtitle of the footer.
     *
     * @return footer subtitle
     */
    public String getSubTitle() {
        return subTitle;
    }
    /**
     * Returns the title for the search section in the footer.
     *
     * @return search section title
     */
    public String getSearchTitle() {
        return searchTitle;
    }
    /**
     * Returns the button text for the search section in the footer.
     *
     * @return search button text
     */
    public String getButtonText() {
        return buttonText;
    }
    /**
     * Returns the copyright text to be displayed in the footer.
     *
     * @return copyright information
     */
    public String getCopyrightText() {
        return copyrightText;
    }
    /**
     * Returns the "Designed by" text to be displayed in the footer.
     *
     * @return designed by information
     */
    public String getDesignedByText() {
        return designedByText;
    }
}
