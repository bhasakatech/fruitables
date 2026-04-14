package com.bhasaka.fruitables.core.models;

import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.Self;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

import javax.annotation.PostConstruct;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

/**
 * Sling Model for the Categories Tags component.
 *
 * This model retrieves authored category tags from the content fragment, resolves product-category associations,
 * and provides structured data for rendering category filters and associated products in the frontend.
 */
@Model(
        adaptables = SlingHttpServletRequest.class,
        adapters = CategorysTagsModel.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class CategorysTagsModel {
    /** The request parameter for the selected category. */
    private static final String CATEGORY_REQUEST_PARAMETER = "category";
    /** Default card style if not specified in the content. */
    private static final String DEFAULT_CARD_STYLE = "standard";
    /** Map of authored category tag IDs to their display titles. */
    @Self
    private SlingHttpServletRequest request;
    /** Resource representing the current component instance. */
    @SlingObject
    private Resource resource;
    /** ResourceResolver for accessing resources and resolving category information. */
    @SlingObject
    private ResourceResolver resourceResolver;
    /** Stores the category title authored in the content. */
    @ValueMapValue
    private String categoryTitle;
    /** Stores the root path of the product content fragment for querying products. */
    @ValueMapValue
    private String fragmentRootPath;
    /** Stores the list of category tags authored in the content. */
    @ValueMapValue
    private String[] tags;
    /** Map of authored category tag IDs to their display titles, populated during initialization. */
    @ValueMapValue
    private String cartButtonText;
    /** Stores the card style for product display, authored in the content. */
    @ValueMapValue
    private String cardStyle;
    /** Stores the price unit (e.g., currency symbol) to be displayed with product prices. */
    @ValueMapValue
    private String priceUnit;
    /** Flag indicating whether the component is properly configured with necessary data. */
    @ValueMapValue
    private Boolean showDescription;
    /** Flag indicating whether to show product ratings in the frontend. */
    @ValueMapValue
    private Boolean showRating;
    /** Flag indicating whether to show product categories in the frontend. */
    @ValueMapValue
    private Boolean showCategory;
    /** Flag indicating whether to show borders around product cards. */
    @ValueMapValue
    private Boolean showBorder;
    /** Stores the category color to be used in the frontend, authored in the content. */
    @ValueMapValue
    private String categoryColor;
    /** Stores the position for any badges displayed on product cards, authored in the content. */
    @ValueMapValue
    private String badgePosition;

    /** Stores the message to be displayed when no category is selected. */ 
    @ValueMapValue
    private String emptySelectionMessage;
    /** Stores the message to be displayed when no products are found for the selected category. */
    @ValueMapValue
    private String noResultsMessage;
    /** List of products matching the selected category, populated during initialization. */
    @PostConstruct
    protected void init() {
        authoredCategories = getAuthoredCategories(tags);
        configured = !ProductCategorySupport.isBlank(fragmentRootPath) && !authoredCategories.isEmpty();

        selectedCategoryTagId = resolveSelectedCategory(authoredCategories.keySet());
        selectedCategoryTitle = authoredCategories.get(selectedCategoryTagId);

        if (!configured) {
            categories = buildCategories(Collections.emptyList());
            return;
        }

        List<Resource> masterResources = ProductCategorySupport.queryProductMasterResources(resourceResolver, fragmentRootPath);
        categories = buildCategories(masterResources);

        if (ProductCategorySupport.isBlank(selectedCategoryTagId)) {
            return;
        }

        List<ProductItem> matchedProducts = new ArrayList<>();
        for (Resource masterResource : masterResources) {
            ProductCFModelTag product = masterResource.adaptTo(ProductCFModelTag.class);
            if (product == null) {
                continue;
            }

            Set<String> productTagIds = ProductCategorySupport.extractProductTagIds(resourceResolver, masterResource.getValueMap(), product);
            if (productTagIds.contains(selectedCategoryTagId)) {
                matchedProducts.add(
                        new ProductItem(product, getCardStyle(), masterResource.getPath())
                );
            }
        }

        matchedProducts.sort(Comparator.comparing(item -> safeLowerCase(item.getProduct().getProductName())));
        products = matchedProducts;
    }
    /** List of products matching the selected category. */
    private List<ProductItem> products = Collections.emptyList();
    /** Flag indicating whether the component is properly configured with necessary data. */
    private boolean configured;
    /** Stores the selected category tag ID based on the request parameter. */
    private String selectedCategoryTagId;
    /** Stores the title of the selected category. */
    private String selectedCategoryTitle;
    private Map<String, String> authoredCategories = Collections.emptyMap();
    private List<CategoryItem> categories = Collections.emptyList();
    /** Retrieves the authored category tags from the content and resolves their display titles.
     *
     * @param sidebarTags array of category tag values authored in the content
     * @return map of normalized category tag IDs to their display titles
     */
    private Map<String, String> getAuthoredCategories(String[] sidebarTags) {
        if (sidebarTags == null || sidebarTags.length == 0) {
            return Collections.emptyMap();
        }

        Map<String, String> categories = new LinkedHashMap<>();
        for (String tagValue : sidebarTags) {
            String normalizedTagId = ProductCategorySupport.normalizeTagId(resourceResolver, tagValue);
            if (isBlank(normalizedTagId) || categories.containsKey(normalizedTagId)) {
                continue;
            }
            categories.put(normalizedTagId, ProductCategorySupport.resolveCategoryTitle(resourceResolver, tagValue));
        }
        return categories;
    }
    /** Builds the list of category items for rendering in the frontend, including product counts and links.
     *
     * @param masterResources list of product master resources to analyze for category associations
     * @return list of CategoryItem objects representing each category for frontend rendering
     */
    private List<CategoryItem> buildCategories(List<Resource> masterResources) {
        if (authoredCategories.isEmpty()) {
            return Collections.emptyList();
        }

        Map<String, Integer> categoryCounts = new LinkedHashMap<>();
        for (String authoredCategoryTagId : authoredCategories.keySet()) {
            categoryCounts.put(authoredCategoryTagId, 0);
        }

        for (Resource masterResource : masterResources) {
            ProductCFModelTag product = masterResource.adaptTo(ProductCFModelTag.class);
            if (product == null) {
                continue;
            }

            Set<String> productTagIds = ProductCategorySupport.extractProductTagIds(resourceResolver, masterResource.getValueMap(), product);
            for (String productTagId : new LinkedHashSet<>(productTagIds)) {
                if (categoryCounts.containsKey(productTagId)) {
                    categoryCounts.put(productTagId, categoryCounts.get(productTagId) + 1);
                }
            }
        }

        List<CategoryItem> items = new ArrayList<>();
        for (Map.Entry<String, String> entry : authoredCategories.entrySet()) {
            String tagId = entry.getKey();
            items.add(new CategoryItem(
                    tagId,
                    entry.getValue(),
                    categoryCounts.getOrDefault(tagId, 0),
                    buildCategoryLink(tagId),
                    Objects.equals(tagId, selectedCategoryTagId)
            ));
        }
        return items;
    }

    /** Resolves the selected category tag ID from the request parameter and validates it against the authored categories.
     *
     * @param authoredCategoryTagIds set of normalized category tag IDs that are authored in the content
     * @return the valid selected category tag ID if it exists in the authored categories, otherwise null
     */
    private String resolveSelectedCategory(Set<String> authoredCategoryTagIds) {
        String requestedCategory = request.getParameter(CATEGORY_REQUEST_PARAMETER);
        String normalizedRequestedCategory = ProductCategorySupport.normalizeTagId(resourceResolver, requestedCategory);
        if (authoredCategoryTagIds.contains(normalizedRequestedCategory)) {
            return normalizedRequestedCategory;
        }
        return null;
    }

    /** Builds the link for a given category tag ID by preserving existing request parameters and adding/updating the category parameter.
     *
     * @param tagId the category tag ID for which to build the link
     * @return the URL query string with the appropriate category parameter for filtering products
     */
    private String buildCategoryLink(String tagId) {
        StringBuilder builder = new StringBuilder("?");
        boolean firstParameter = true;

        for (Map.Entry<String, String[]> entry : request.getParameterMap().entrySet()) {
            if (CATEGORY_REQUEST_PARAMETER.equals(entry.getKey())) {
                continue;
            }
            for (String value : entry.getValue()) {
                if (!firstParameter) {
                    builder.append('&');
                }
                builder.append(encode(entry.getKey()));
                if (value != null) {
                    builder.append('=').append(encode(value));
                }
                firstParameter = false;
            }
        }

        if (!firstParameter) {
            builder.append('&');
        }
        builder.append(CATEGORY_REQUEST_PARAMETER).append('=').append(encode(tagId));
        return builder.toString();
    }

    /** Encodes a string value for safe inclusion in URL query parameters.
     *
     * @param value the string value to encode
     * @return the URL-encoded string, or the original value if encoding fails
     */
    private String encode(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            return value;
        }
    }

    /** Converts a string to lower case in a locale-insensitive manner, treating null as an empty string.
     *
     * @param value the string value to convert
     * @return the lower-case version of the string, or an empty string if the input is null
     */
    private String safeLowerCase(String value) {
        return value != null ? value.toLowerCase(Locale.ENGLISH) : "";
    }

    /** Checks if a string value is blank (null, empty, or only whitespace).
     *
     * @param value the string value to check
     * @return true if the value is blank, false otherwise
     */
    private boolean isBlank(String value) {
        return ProductCategorySupport.isBlank(value);
    }
    /** Checks if the component is properly configured with necessary data to function.
     *
     * @return true if the component is configured, false otherwise
     */
    public boolean isConfigured() {
        return configured;
    }
    /** Checks if a category has been selected based on the request parameter and authored categories.
     *
     * @return true if a valid category is selected, false otherwise
     */
    public boolean isHasSelection() {
        return !isBlank(selectedCategoryTagId);
    }
    /** Checks if there are products available for the selected category.
     *
     * @return true if there are products to display, false otherwise
     */
    public boolean isHasProducts() {
        return !products.isEmpty();
    }
    /** Retrieves the list of products matching the selected category for rendering in the frontend.
     *
     * @return list of ProductItem objects representing the products to display
     */
    public List<ProductItem> getProducts() {
        return products;
    }
    /** Retrieves the title for the category filter section, defaulting to "Categories" if not authored.
     *
     * @return the category title for display in the frontend
     */
    public String getCategoryTitle() {
        return !isBlank(categoryTitle) ? categoryTitle : "Categories";
    }
    /** Retrieves the list of category items for rendering in the frontend, including their titles, counts, and links.
     *
     * @return list of CategoryItem objects representing each category for frontend rendering
     */
    public List<CategoryItem> getCategories() {
        return categories;
    }
    /** Retrieves the title of the currently selected category for display in the frontend, defaulting to "Products" if no category is selected.
     *
     * @return the title of the selected category or a default title if no category is selected
     */
    public String getSelectedCategoryTitle() {
        return !isBlank(selectedCategoryTitle) ? selectedCategoryTitle : "Products";
    }
    /** Retrieves the text for the "Add to cart" button, defaulting to "Add to cart" if not authored in the content.
     *
     * @return the text to display on the cart button in the frontend
     */
    public String getCartButtonText() {
        return isBlank(cartButtonText) ? "Add to cart" : cartButtonText;
    }
    /** Retrieves the card style for product display, defaulting to "standard" if not authored in the content.
     *
     * @return the card style to use for rendering product cards in the frontend
     */
    public String getCardStyle() {
        return isBlank(cardStyle) ? DEFAULT_CARD_STYLE : cardStyle;
    }
    /** Retrieves the price unit (e.g., currency symbol) to be displayed with product prices, defaulting to an empty string if not authored.
     *
     * @return the price unit for display in the frontend
     */
    public String getPriceUnit() {
        return priceUnit;
    }
    /** Checks whether to show product descriptions in the frontend, defaulting to true if not explicitly set to false.
     *
     * @return true if product descriptions should be shown, false otherwise
     */
    public boolean isShowDescription() {
        return showDescription == null || showDescription;
    }
    /** Checks whether to show product ratings in the frontend, defaulting to true if not explicitly set to false.
     *
     * @return true if product ratings should be shown, false otherwise
     */
    public boolean isShowRating() {
        return showRating == null || showRating;
    }
    /** Checks whether to show product categories in the frontend, defaulting to true if not explicitly set to false.
     *
     * @return true if product categories should be shown, false otherwise
     */
    public boolean isShowCategory() {
        return showCategory == null || showCategory;
    }
    /** Checks whether to show borders around product cards in the frontend, defaulting to true if not explicitly set to false.
     *
     * @return true if borders should be shown around product cards, false otherwise
     */
    public boolean isShowBorder() {
        return showBorder != null && showBorder;
    }
    /** Retrieves the category color to be used in the frontend, defaulting to "orange" if not authored in the content.
     *
     * @return the category color for display in the frontend
     */
    public String getCategoryColor() {
        return isBlank(categoryColor) ? "orange" : categoryColor;
    }
    /** Retrieves the position for badges displayed on product cards, defaulting to "top-left" if not authored in the content.
     *
     * @return the badge position for display in the frontend
     */
    public String getBadgePosition() {
        return isBlank(badgePosition) ? "top-left" : badgePosition;
    }
    /** Retrieves the message to be displayed when no category is selected, defaulting to a standard message if not authored in the content.
     *
     * @return the message to display when no category is selected
     */
    public String getEmptySelectionMessage() {
        return isBlank(emptySelectionMessage)
                ? "Select a category to load the matching products."
                : emptySelectionMessage;
    }
    /** Retrieves the message to be displayed when no products are found for the selected category, defaulting to a standard message if not authored in the content.
     *
     * @return the message to display when no products are found for the selected category
     */
    public String getNoResultsMessage() {
        return isBlank(noResultsMessage)
                ? "No products were found for the selected category."
                : noResultsMessage;
    }
    /** Retrieves the unique component ID for this instance, which can be used in the frontend for targeting with JavaScript or CSS.
     *
     * @return the unique component ID for this instance
     */
    public String getComponentId() {
        return ProductCategorySupport.buildComponentId(resource, "categorys-tags-");
    }
    /** Inner class representing a category item for frontend rendering, including its tag ID, title, product count, link, and selection state. */
    public static class CategoryItem {
        /** The normalized tag ID for this category, used for filtering products and building links. */
        private final String tagId;
        private final String title;
        private final int count;
        private final String link;
        private final boolean selected;
        /* Constructs a CategoryItem with the specified properties.
         *
         * @param tagId the normalized tag ID for this category
         * @param title the display title for this category
         * @param count the count of products associated with this category
         * @param link the URL link for this category filter
         * @param selected the selection state indicating whether this category is currently selected
         */
        public CategoryItem(String tagId, String title, int count, String link, boolean selected) {
            this.tagId = tagId;
            this.title = title;
            this.count = count;
            this.link = link;
            this.selected = selected;
        }
        /* Retrieves the normalized tag ID for this category, which is used for filtering products and building links.
         *
         * @return the normalized tag ID for this category
         */
        public String getTagId() {
            return tagId;
        }
        /* Retrieves the display title for this category, which is shown in the frontend category filter.
         *
         * @return the display title for this category
         */
        public String getTitle() {
            return title;
        }
        /* Retrieves the count of products associated with this category, which can be displayed in the frontend to indicate how many products match this category.
         *
         * @return the count of products for this category
         */
        public int getCount() {
            return count;
        }
        /* Retrieves the link for this category, which includes the appropriate query parameters to filter products by this category when clicked in the frontend.
         *
         * @return the URL link for this category filter
         */
        public String getLink() {
            return link;
        }

        /* Retrieves the selection state for this category, indicating whether it is currently selected in the frontend.
         *
         * @return true if the category is selected, false otherwise
         */
        public boolean isSelected() {
            return selected;
        }
    }
}
