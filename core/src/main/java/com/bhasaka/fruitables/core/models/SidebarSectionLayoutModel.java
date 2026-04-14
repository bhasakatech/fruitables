package com.bhasaka.fruitables.core.models;

import com.day.cq.tagging.Tag;
import com.day.cq.tagging.TagManager;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ValueMap;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;

/**
 * Sling Model for the Sidebar Section Layout component.
 *
 * This model handles the dynamic rendering of sidebar content including:
 * featured products, sorting options, category/tag listings, pricing filters,
 * additional filter options, and promotional banner details.
 *
 * It supports authored values from component dialog and provides fallback
 * default values when authoring is unavailable.
 *
 * The model also resolves category tag titles and counts tagged resources
 * from the configured category search root path.
 */
@Model(
        adaptables = Resource.class,
        adapters = SidebarSectionLayoutModel.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class SidebarSectionLayoutModel {

    /** The maximum number of featured products to display in the preview. */
    private static final int FEATURED_PRODUCTS_PREVIEW_LIMIT = 3;

    /**
     * Sling object to access the current resource.
     */
    @SlingObject
    private Resource resource;

    /**
     * Sling object to access the resource resolver.
     */
    @SlingObject
    private ResourceResolver resourceResolver;

    /**
     * The title for the sidebar section.
     */
    @ValueMapValue
    private String sectionTitle;

    /**
     * The placeholder text for the search input field.
     */
    @ValueMapValue
    private String searchPlaceholder;
    /**
     * The image URL for the search input field.
     */
    @ValueMapValue
    private String searchImage;

    /**
     * The label for the sorting dropdown.
     */
    @ValueMapValue
    private String sortingLabel;
    /**
     * The array of tags for the sidebar section.
     */
    @ValueMapValue
    private String categoriesTitle;

    /**
     * The array of tags for the sidebar section.
     */
    @ValueMapValue
    private String[] tags;
    /**
     * The root path for searching categories.
     */
    @ValueMapValue
    private String categorySearchRoot;
    /**
     * The title for the price filter section.
     */
    @ValueMapValue
    private String priceTitle;
    /**
     * The minimum price for the price filter.
     */
    @ValueMapValue
    private Integer minPrice;
    /**
     * The maximum price for the price filter.
     */
    @ValueMapValue
    private Integer maxPrice;
    /**
     * The currently selected price for the price filter.
     */
    @ValueMapValue
    private Integer selectedPrice;
    /**
     * The title for the additional filter section.
     */
    @ValueMapValue
    private String additionalTitle;
    /**
     * The title for the featured products section.
     */
    @ValueMapValue
    private String featuredProductsTitle;
    /**
     * The label for the "view more" link.
     */
    @ValueMapValue
    private String viewMoreLabel;
    /**
     * The URL for the "view more" link.
     */
    @ValueMapValue
    private String viewMoreLink;
    /**
     * The image URL for the sidebar banner.
     */
    @ValueMapValue
    private String bannerImage;
    /**
     * The alt text for the sidebar banner image.
     */
    @ValueMapValue
    private String bannerAltText;
    /**
     * The title for the sidebar banner.
     */
    @ValueMapValue
    private String bannerTitle;
    
    /**
     * The list of authored sorting options.
     */
    @ChildResource(name = "sortingOptions")
    private List<SortingOption> authoredSortingOptions;
    /** The list of authored additional filter options. */
    @ChildResource(name = "additionalOptions")
    private List<AdditionalOption> authoredAdditionalOptions;
    /**
     * The list of product resources for the sidebar section.
     */
    @ChildResource(name = "products")
    private List<ProductResource> productResources;

    /**
     * Initializes sidebar component data after model injection.
     *
     * Populates authored or default sorting options, additional options,
     * featured products, and category items.
     */
    @PostConstruct
    protected void init() {
        sortingOptions = authoredSortingOptions != null && !authoredSortingOptions.isEmpty()
                ? authoredSortingOptions
                : buildDefaultSortingOptions();
        additionalOptions = authoredAdditionalOptions != null && !authoredAdditionalOptions.isEmpty()
                ? authoredAdditionalOptions
                : buildDefaultAdditionalOptions();
        featuredProducts = buildFeaturedProducts();
        categories = buildCategories();
    }

    /**
     * Builds default sorting options when no authored sorting options exist.
     * @return list of default sorting options
     */
    private List<SortingOption> sortingOptions = Collections.emptyList();
    /**
     * Builds default additional filter options when no authored options exist.
     *
     * @return list of default additional options
     */
    private List<AdditionalOption> additionalOptions = Collections.emptyList();
    /**
     * Builds featured product items from authored content fragment paths.
     * Resolves content fragment resources and adapts them into ProductItem objects.
     *
     * @return list of featured products
     */
    private List<ProductItem> featuredProducts = Collections.emptyList();
    /**
     * Builds category items based on authored tags and counts tagged resources.
     *
     * Each category item includes a resolved title and the count of resources tagged with it.
     *
     * @return list of category items
     */
    private List<CategoryItem> categories = Collections.emptyList();

    /**
     * Builds a list of default sorting options to use when no authored options are available.
     * @return list of default sorting options
     */
    private List<SortingOption> buildDefaultSortingOptions() {
        List<SortingOption> defaults = new ArrayList<>();
        defaults.add(new SortingOption("Nothing", "nothing", true));
        defaults.add(new SortingOption("Popularity", "popularity", false));
        defaults.add(new SortingOption("Organic", "organic", false));
        defaults.add(new SortingOption("Fantastic", "fantastic", false));
        return defaults;
    }

    /**
     * Builds default additional filter options when no authored options exist.
     *
     * @return list of default additional options
     */
    private List<AdditionalOption> buildDefaultAdditionalOptions() {
        List<AdditionalOption> defaults = new ArrayList<>();
        defaults.add(new AdditionalOption("Organic", "Organic", false));
        defaults.add(new AdditionalOption("Fresh", "Fresh", false));
        defaults.add(new AdditionalOption("Sales", "Sales", false));
        defaults.add(new AdditionalOption("Discount", "Discount", false));
        defaults.add(new AdditionalOption("Expired", "Expired", true));
        return defaults;
    }
    /**
     * Builds featured product items from authored content fragment paths.
     * Resolves content fragment resources and adapts them into ProductItem objects.
     *
     * @return list of featured products
     */
    private List<ProductItem> buildFeaturedProducts() {
        if (resourceResolver == null || productResources == null || productResources.isEmpty()) {
            return Collections.emptyList();
        }

        List<ProductItem> items = new ArrayList<>();
        for (ProductResource productResource : productResources) {

            Resource contentFragmentResource = resourceResolver.getResource(productResource.getCfPath());
            if (contentFragmentResource == null) {
                continue;
            }

            Resource masterResource = contentFragmentResource.getChild("jcr:content/data/master");
            if (masterResource != null) {
                addFeaturedProduct(items, masterResource, productResource.getCardStyle(), productResource.getCfPath());
                continue;
            }

            for (Resource child : contentFragmentResource.getChildren()) {
                Resource childMasterResource = child.getChild("jcr:content/data/master");
                if (childMasterResource != null) {
                    addFeaturedProduct(items, childMasterResource, productResource.getCardStyle(), productResource.getCfPath());
                }
            }
        }

        return items;
    }

    /**
     * Adds a featured product to the provided product list.
     *
     * @param items destination product items list
     * @param masterResource content fragment master resource
     * @param cardStyle authored card style
     * @param productPath content fragment path
     */
    private void addFeaturedProduct(List<ProductItem> items, Resource masterResource, String cardStyle,String productPath) {
        ProductCFModel product = masterResource.adaptTo(ProductCFModel.class);
        if (product != null) {
            items.add(new ProductItem(product, cardStyle,productPath));
        }
    }
    /**
     * Builds category items from authored tags.
     *
     * Each category includes resolved title and tagged resource count.
     *
     * @return list of category items
     */
    private List<CategoryItem> buildCategories() {
        if (tags == null || tags.length == 0) {
            return Collections.emptyList();
        }

        List<CategoryItem> categoryItems = new ArrayList<>();
        for (String tagValue : tags) {
            if (isBlank(tagValue)) {
                continue;
            }
            categoryItems.add(new CategoryItem(resolveCategoryTitle(tagValue), countTaggedResources(tagValue)));
        }
        return categoryItems;
    }
    /**
     * Resolves readable category title from tag value.
     *
     * Attempts to fetch title from TagManager. Falls back to formatted tag name.
     *
     * @param tagValue authored tag value
     * @return resolved category title
     */
    private String resolveCategoryTitle(String tagValue) {
        TagManager tagManager = resourceResolver.adaptTo(TagManager.class);
        if (tagManager != null) {
            Tag tag = tagManager.resolve(tagValue);
            if (tag != null) {
                if (!isBlank(tag.getTitle())) {
                    return tag.getTitle();
                }
                if (!isBlank(tag.getName())) {
                    return humanize(tag.getName());
                }
            }
        }
        return humanize(extractTagLeaf(tagValue));
    }

    /**
     * Counts resources matching the provided tag under configured search root.
     *
     * @param tagValue tag to search
     * @return total tagged resource count
     */
    private int countTaggedResources(String tagValue) {
        if (isBlank(categorySearchRoot)) {
            return 0;
        }

        Resource searchRootResource = resourceResolver.getResource(categorySearchRoot);
        if (searchRootResource == null) {
            return 0;
        }

        Set<String> expectedTags = buildTagCandidates(tagValue);
        int count = 0;
        Deque<Resource> queue = new ArrayDeque<>();
        queue.add(searchRootResource);

        while (!queue.isEmpty()) {
            Resource current = queue.removeFirst();
            if (hasMatchingTag(current.getValueMap(), expectedTags)) {
                count++;
            }
            for (Resource child : current.getChildren()) {
                queue.addLast(child);
            }
        }

        return count;
    }

    /**
     * Checks whether resource contains matching tag values.
     *
     * @param valueMap resource properties
     * @param expectedTags expected tag candidates
     * @return true if matching tag exists
     */
    private boolean hasMatchingTag(ValueMap valueMap, Set<String> expectedTags) {
        String[] multiValueTags = valueMap.get("cq:tags", String[].class);
        if (multiValueTags != null) {
            for (String resourceTag : multiValueTags) {
                if (matchesTag(resourceTag, expectedTags)) {
                    return true;
                }
            }
        }

        String singleValueTag = valueMap.get("cq:tags", String.class);
        return matchesTag(singleValueTag, expectedTags);
    }
    /**
     * Builds all possible tag candidate formats for comparison.
     *
     * Includes tag ID, content path, and legacy etc path formats.
     *
     * @param tagValue original tag value
     * @return normalized tag candidate set
     */
    private Set<String> buildTagCandidates(String tagValue) {
        Set<String> candidates = new HashSet<>();
        if (isBlank(tagValue)) {
            return candidates;
        }

        candidates.add(tagValue);

        String normalizedTagId = convertTagPathToId(tagValue);
        if (!isBlank(normalizedTagId)) {
            candidates.add(normalizedTagId);
            candidates.add(buildContentTagPath(normalizedTagId));
            candidates.add(buildEtcTagPath(normalizedTagId));
        }

        TagManager tagManager = resourceResolver.adaptTo(TagManager.class);
        if (tagManager != null) {
            Tag tag = tagManager.resolve(tagValue);
            if (tag != null) {
                if (!isBlank(tag.getTagID())) {
                    candidates.add(tag.getTagID());
                    candidates.add(buildContentTagPath(tag.getTagID()));
                    candidates.add(buildEtcTagPath(tag.getTagID()));
                }
                if (!isBlank(tag.getPath())) {
                    candidates.add(tag.getPath());
                }
            }
        }

        return candidates;
    }

    /**
     * Validates whether resource tag matches expected tags.
     *
     * @param resourceTag tag present on resource
     * @param expectedTags expected tags set
     * @return true if matched
     */
    private boolean matchesTag(String resourceTag, Set<String> expectedTags) {
        if (isBlank(resourceTag)) {
            return false;
        }

        if (expectedTags.contains(resourceTag)) {
            return true;
        }

        String normalizedTagId = convertTagPathToId(resourceTag);
        if (!isBlank(normalizedTagId) && expectedTags.contains(normalizedTagId)) {
            return true;
        }

        return expectedTags.contains(buildContentTagPath(resourceTag))
                || expectedTags.contains(buildEtcTagPath(resourceTag));
    }
    /**
     * Converts tag repository path into tag ID format.
     *
     * @param value tag path or ID
     * @return normalized tag ID
     */
    private String convertTagPathToId(String value) {
        if (isBlank(value)) {
            return value;
        }
        if (value.startsWith("/content/cq:tags/")) {
            return value.substring("/content/cq:tags/".length()).replaceFirst("/", ":");
        }
        if (value.startsWith("/etc/tags/")) {
            return value.substring("/etc/tags/".length()).replaceFirst("/", ":");
        }
        return value;
    }
    /**
     * Builds content repository path for tag based on tag ID.
     *
     * @param tagId tag ID to convert
     * @return content path format of tag
     */
    private String buildContentTagPath(String tagId) {
        if (isBlank(tagId) || tagId.startsWith("/")) {
            return tagId;
        }
        return "/content/cq:tags/" + tagId.replace(":", "/");
    }
    /**
     * Builds legacy etc repository path for tag based on tag ID.
     *
     * @param tagId tag ID to convert
     * @return etc path format of tag
     */
    private String buildEtcTagPath(String tagId) {
        if (isBlank(tagId) || tagId.startsWith("/")) {
            return tagId;
        }
        return "/etc/tags/" + tagId.replace(":", "/");
    }
    /**
     * Extracts leaf node from tag value.
     *
     * @param tagValue full tag value
     * @return leaf portion of tag
     */
    private String extractTagLeaf(String tagValue) {
        String normalizedTagId = convertTagPathToId(tagValue);
        if (normalizedTagId.contains(":")) {
            normalizedTagId = normalizedTagId.substring(normalizedTagId.indexOf(':') + 1);
        }
        int lastSlashIndex = normalizedTagId.lastIndexOf('/');
        return lastSlashIndex >= 0 ? normalizedTagId.substring(lastSlashIndex + 1) : normalizedTagId;
    }

    /**
     * Removes HTML markup from provided string.
     *
     * @param value input text
     * @return plain text without markup
     */
    private String stripMarkup(String value) {
        return value.replaceAll("<[^>]+>", "").trim();
    }
    /**
     * Converts raw text into human-readable formatted text.
     *
     * Replaces separators and capitalizes words.
     *
     * @param value raw text
     * @return formatted text
     */
    private String humanize(String value) {
        if (isBlank(value)) {
            return "";
        }

        String normalized = value.replace('-', ' ').replace('_', ' ').trim();
        String[] words = normalized.split("\\s+");
        StringBuilder builder = new StringBuilder();
        for (String word : words) {
            if (word.isEmpty()) {
                continue;
            }
            if (builder.length() > 0) {
                builder.append(' ');
            }
            builder.append(word.substring(0, 1).toUpperCase(Locale.ENGLISH));
            if (word.length() > 1) {
                builder.append(word.substring(1));
            }
        }
        return builder.toString();
    }

    /**
     * Represents a sorting option item in sidebar sorting dropdown.
     */
    @Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
    public static class SortingOption {

        /** The display text for the sorting option. */
        @ValueMapValue
        private String optionText;
        /** The value for the sorting option, used for sorting logic. */
        @ValueMapValue
        private String optionValue;
        /** Indicates whether this sorting option is currently selected. */
        @ValueMapValue
        private boolean selected;
        /**
         * Default constructor for Sling Model instantiation.
         */
        public SortingOption() {
        }
        /**
         * Parameterized constructor for manual instantiation of sorting options.
         * @param optionText
         * @param optionValue
         * @param selected
         */
        private SortingOption(String optionText, String optionValue, boolean selected) {
            this.optionText = optionText;
            this.optionValue = optionValue;
            this.selected = selected;
        }

        /**
         * Returns sorting option display text.
         *
         * @return option text
         */
        public String getOptionText() {
            return optionText;
        }

        /**
         * Returns sorting option value.
         *
         * Falls back to option text when value is unavailable.
         *
         * @return option value
         */
        public String getOptionValue() {
            return optionValue != null ? optionValue : optionText;
        }
        /**
         * Indicates whether sorting option is selected.
         *
         * @return true if selected
         */
        public boolean isSelected() {
            return selected;
        }
    }

    /**
     * Represents additional checkbox filter option.
     */
    @Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
    public static class AdditionalOption {

        /** The label for the additional option. */
        @ValueMapValue
        private String label;

        /** The value for the additional option, used for filtering logic. */
        @ValueMapValue
        private String value;

        /** Indicates whether the additional option is checked. */ 
        @ValueMapValue
        private boolean checked;
        /**
         * Default constructor for Sling Model instantiation.
         */
        public AdditionalOption() {
        }
        /**
         * Parameterized constructor for manual instantiation of additional options.
         *
         * @param label display label
         * @param value filter value
         * @param checked whether option is checked
         */
        private AdditionalOption(String label, String value, boolean checked) {
            this.label = label;
            this.value = value;
            this.checked = checked;
        }
        /**
        * Returns the label for the additional option.
        *
        * @return additional option label
        */
        public String getLabel() {
            return label;
        }
        /**
         * Returns the value for the additional option.
         *
         * Falls back to label when value is unavailable.
         *
         * @return additional option value
         */
        public String getValue() {
            return value != null ? value : label;
        }

        /**
         * Indicates whether the additional option is checked.
         *
         * @return true if checked
         */
        public boolean isChecked() {
            return checked;
        }
    }

    /**
     * Represents category item with title and tagged count.
     */
    public static class CategoryItem {
        private final String title;
        private final int count;
        /**
         * Constructs a CategoryItem with the given title and count.
         *
         * @param title the display title of the category
         * @param count the number of resources tagged with this category
         */
        public CategoryItem(String title, int count) {
            this.title = title;
            this.count = count;
        }
        /**
         * Returns the display title of the category.
         *
         * @return category title
         */
        public String getTitle() {
            return title;
        }
        /**
         * Returns the number of resources tagged with this category.
         *
         * @return tagged resource count
         */
        public int getCount() {
            return count;
        }
    }
    /**
     * Returns sidebar section title.
     *
     * @return section title
     */
    public String getSectionTitle() {
        return defaultIfBlank(sectionTitle, "Fresh fruits shop");
    }
    /**
     * Returns search input placeholder text.
     *
     * @return search placeholder
     */
    public String getSearchPlaceholder() {
        return defaultIfBlank(searchPlaceholder, "keywords");
    }
    /**
     * Returns search input image URL.
     *
     * @return search image URL/path
     */
    public String getSearchImage() {
        return searchImage;
    }
    /**
     * Returns sorting label for the sorting dropdown.
     *
     * @return sorting label
     */
    public String getSortingLabel() {
        return defaultIfBlank(sortingLabel, "Default Sorting:");
    }
    /**
     * Returns list of sorting options for the sorting dropdown.
     *
     * @return list of sorting options
     */
    public List<SortingOption> getSortingOptions() {
        return sortingOptions;
    }
    /**
     * Returns the title for the categories section.
     *
     * @return categories section title
     */
    public String getCategoriesTitle() {
        return defaultIfBlank(categoriesTitle, "Categories");
    }
    /**
     * Returns list of category items with title and tagged resource count.
     *
     * @return list of category items
     */
    public List<CategoryItem> getCategories() {
        return categories;
    }
    /**
     * Returns the title for the price filter section.
     *
     * @return price filter title
     */
    public String getPriceTitle() {
        return defaultIfBlank(priceTitle, "Price");
    }
    /**
     * Returns the minimum price for the price filter.
     * Ensures non-negative value with a default of 0.
     *
     * @return minimum price
     */
    public int getMinPrice() {
        return minPrice != null ? Math.max(minPrice, 0) : 0;
    }
    /**
     * Returns the maximum price for the price filter.
     * Ensures non-negative value with a default of 500, and not less than minimum price.
     *
     * @return maximum price
     */
    public int getMaxPrice() {
        int resolvedMaxPrice = maxPrice != null ? Math.max(maxPrice, 0) : 500;
        return resolvedMaxPrice < getMinPrice() ? getMinPrice() : resolvedMaxPrice;
    }
    /**
     * Returns the currently selected price for the price filter.
     * Ensures value is within the defined minimum and maximum price range.
     *
     * @return selected price
     */
    public int getSelectedPrice() {
        int resolvedSelectedPrice = selectedPrice != null ? selectedPrice : getMinPrice();
        if (resolvedSelectedPrice < getMinPrice()) {
            return getMinPrice();
        }
        if (resolvedSelectedPrice > getMaxPrice()) {
            return getMaxPrice();
        }
        return resolvedSelectedPrice;
    }
    /**
     * Returns the title for the additional filter section.
     *
     * @return additional filter section title
     */
    public String getAdditionalTitle() {
        return defaultIfBlank(additionalTitle, "Additional");
    }

    /**
     * Returns list of additional filter options.
     *
     * @return list of additional options
     */
    public List<AdditionalOption> getAdditionalOptions() {
        return additionalOptions;
    }
    /**
     * Returns the title for the featured products section.
     *
     * @return featured products section title
     */
    public String getFeaturedProductsTitle() {
        return defaultIfBlank(featuredProductsTitle, "Featured products");
    }

    /**
     * Returns featured products list.
     *
     * @return featured products
     */
    public List<ProductItem> getFeaturedProducts() {
        return featuredProducts;
    }
    /**
     * Returns a preview list of featured products limited to a defined number.
     *
     * @return list of featured products for preview
     */
    public List<ProductItem> getFeaturedProductsPreview() {
        if (featuredProducts.isEmpty()) {
            return Collections.emptyList();
        }
        return new ArrayList<>(featuredProducts.subList(0, Math.min(FEATURED_PRODUCTS_PREVIEW_LIMIT, featuredProducts.size())));
    }
    /**
     * Returns the list of remaining featured products beyond the preview limit.
     *
     * @return list of remaining featured products or empty list if not expandable
     */
    public List<ProductItem> getRemainingFeaturedProducts() {
        if (!isFeaturedProductsExpandable()) {
            return Collections.emptyList();
        }
        return new ArrayList<>(featuredProducts.subList(FEATURED_PRODUCTS_PREVIEW_LIMIT, featuredProducts.size()));
    }
    /**
     * Determines if the featured products list exceeds the preview limit, indicating expandability.
     *
     * @return true if there are more featured products than the preview limit
     */
    public boolean isFeaturedProductsExpandable() {
        return featuredProducts.size() > FEATURED_PRODUCTS_PREVIEW_LIMIT;
    }
    /**
     * Returns the DOM ID for the featured products list element.
     *
     * @return featured products list DOM ID
     */
    public String getFeaturedProductsListId() {
        return getDomId() + "-featured-products";
    }
    /**
     * Returns the label for the "view more" link.
     *
     * @return view more link label
     */
    public String getViewMoreLabel() {
        return defaultIfBlank(viewMoreLabel, "View More");
    }
    /**
     * Returns the URL for the "view more" link.
     *
     * @return view more link URL
     */
    public String getViewMoreLink() {
        return defaultIfBlank(viewMoreLink, "#");
    }
    /**
     * Returns the image URL for the sidebar banner.
     *
     * @return banner image URL/path
     */
    public String getBannerImage() {
        return bannerImage;
    }

    /**
     * Returns banner alt text.
     *
     * Falls back to banner title or default text if alt text is unavailable.
     *
     * @return banner alt text
     */
    public String getBannerAltText() {
        return defaultIfBlank(bannerAltText, stripMarkup(defaultIfBlank(bannerTitle, "Fresh Fruits Banner")));
    }
    /**
     * Returns banner title.
     *
     * @return banner title
     */
    public String getBannerTitle() {
        return defaultIfBlank(bannerTitle, "Fresh Fruits Banner");
    }
    /**
     * Generates a unique DOM ID for the sidebar section based on the resource path.
     *
     * @return unique DOM ID for the sidebar section
     */
    public String getDomId() {
        return "sidebar-section-" + Math.abs(resource.getPath().hashCode());
    }

    /**
     * Returns the DOM ID for the search input element.
     *
     * @return search input ID
     */
    public String getSearchIconId() {
        return getDomId() + "-search-icon";
    }

    /**
     * Returns the DOM ID for the sorting dropdown element.
     *
     * @return sorting dropdown ID
     */
    public String getSortingId() {
        return getDomId() + "-sorting";
    }

    /**
     * Returns the DOM ID for the price range input element.
     *
     * @return price range input ID
     */
    public String getRangeInputId() {
        return getDomId() + "-range";
    }

    /**
     * Returns the DOM ID for the price amount display element.
     *
     * @return price amount output ID
     */
    public String getAmountOutputId() {
        return getDomId() + "-amount";
    }

    /**
     * Returns the DOM ID prefix for additional filter options.
     *
     * @return additional options ID prefix
     */
    public String getAdditionalIdPrefix() {
        return getDomId() + "-additional-";
    }

    /**
     * Returns the DOM ID for the additional filter options group.
     *
     * @return additional options group ID
     */
    public String getAdditionalGroupName() {
        return getDomId() + "-additional-group";
    }

    /**
     * Returns fallback value when input is blank.
     *
     * @param value original value
     * @param fallback fallback value
     * @return resolved string
     */
    private String defaultIfBlank(String value, String fallback) {
        return isBlank(value) ? fallback : value;
    }

    /**
     * Checks whether string is null or empty.
     *
     * @param value input string
     * @return true if blank
     */
    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

}
