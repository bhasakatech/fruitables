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

@Model(
        adaptables = Resource.class,
        adapters = SidebarSectionLayoutModel.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class SidebarSectionLayoutModel {

    private static final int FEATURED_PRODUCTS_PREVIEW_LIMIT = 3;

    @SlingObject
    private Resource resource;

    @SlingObject
    private ResourceResolver resourceResolver;

    @ValueMapValue
    private String sectionTitle;

    @ValueMapValue
    private String searchPlaceholder;

    @ValueMapValue
    private String searchImage;

    @ValueMapValue
    private String sortingLabel;

    @ValueMapValue
    private String categoriesTitle;

    @ValueMapValue
    private String[] tags;

    @ValueMapValue
    private String categorySearchRoot;

    @ValueMapValue
    private String priceTitle;

    @ValueMapValue
    private Integer minPrice;

    @ValueMapValue
    private Integer maxPrice;

    @ValueMapValue
    private Integer selectedPrice;

    @ValueMapValue
    private String additionalTitle;

    @ValueMapValue
    private String featuredProductsTitle;

    @ValueMapValue
    private String viewMoreLabel;

    @ValueMapValue
    private String viewMoreLink;

    @ValueMapValue
    private String bannerImage;

    @ValueMapValue
    private String bannerAltText;

    @ValueMapValue
    private String bannerTitle;
 
    @ChildResource(name = "sortingOptions")
    private List<SortingOption> authoredSortingOptions;
    
    @ChildResource(name = "additionalOptions")
    private List<AdditionalOption> authoredAdditionalOptions;

    @ChildResource(name = "products")
    private List<ProductResource> productResources;

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

    private List<SortingOption> sortingOptions = Collections.emptyList();
    private List<AdditionalOption> additionalOptions = Collections.emptyList();
    private List<ProductItem> featuredProducts = Collections.emptyList();
    private List<CategoryItem> categories = Collections.emptyList();

    private List<SortingOption> buildDefaultSortingOptions() {
        List<SortingOption> defaults = new ArrayList<>();
        defaults.add(new SortingOption("Nothing", "nothing", true));
        defaults.add(new SortingOption("Popularity", "popularity", false));
        defaults.add(new SortingOption("Organic", "organic", false));
        defaults.add(new SortingOption("Fantastic", "fantastic", false));
        return defaults;
    }

    private List<AdditionalOption> buildDefaultAdditionalOptions() {
        List<AdditionalOption> defaults = new ArrayList<>();
        defaults.add(new AdditionalOption("Organic", "Organic", false));
        defaults.add(new AdditionalOption("Fresh", "Fresh", false));
        defaults.add(new AdditionalOption("Sales", "Sales", false));
        defaults.add(new AdditionalOption("Discount", "Discount", false));
        defaults.add(new AdditionalOption("Expired", "Expired", true));
        return defaults;
    }

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

    private void addFeaturedProduct(List<ProductItem> items, Resource masterResource, String cardStyle,String productPath) {
        ProductCFModel product = masterResource.adaptTo(ProductCFModel.class);
        if (product != null) {
            items.add(new ProductItem(product, cardStyle,productPath));
        }
    }

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

    private String buildContentTagPath(String tagId) {
        if (isBlank(tagId) || tagId.startsWith("/")) {
            return tagId;
        }
        return "/content/cq:tags/" + tagId.replace(":", "/");
    }

    private String buildEtcTagPath(String tagId) {
        if (isBlank(tagId) || tagId.startsWith("/")) {
            return tagId;
        }
        return "/etc/tags/" + tagId.replace(":", "/");
    }

    private String extractTagLeaf(String tagValue) {
        String normalizedTagId = convertTagPathToId(tagValue);
        if (normalizedTagId.contains(":")) {
            normalizedTagId = normalizedTagId.substring(normalizedTagId.indexOf(':') + 1);
        }
        int lastSlashIndex = normalizedTagId.lastIndexOf('/');
        return lastSlashIndex >= 0 ? normalizedTagId.substring(lastSlashIndex + 1) : normalizedTagId;
    }

    private String stripMarkup(String value) {
        return value.replaceAll("<[^>]+>", "").trim();
    }

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


    @Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
    public static class SortingOption {

        @ValueMapValue
        private String optionText;

        @ValueMapValue
        private String optionValue;

        @ValueMapValue
        private boolean selected;

        public SortingOption() {
        }

        private SortingOption(String optionText, String optionValue, boolean selected) {
            this.optionText = optionText;
            this.optionValue = optionValue;
            this.selected = selected;
        }

        public String getOptionText() {
            return optionText;
        }

        public String getOptionValue() {
            return optionValue != null ? optionValue : optionText;
        }

        public boolean isSelected() {
            return selected;
        }
    }

    @Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
    public static class AdditionalOption {

        @ValueMapValue
        private String label;

        @ValueMapValue
        private String value;

        @ValueMapValue
        private boolean checked;

        public AdditionalOption() {
        }

        private AdditionalOption(String label, String value, boolean checked) {
            this.label = label;
            this.value = value;
            this.checked = checked;
        }

        public String getLabel() {
            return label;
        }

        public String getValue() {
            return value != null ? value : label;
        }

        public boolean isChecked() {
            return checked;
        }
    }

    public static class CategoryItem {
        private final String title;
        private final int count;

        public CategoryItem(String title, int count) {
            this.title = title;
            this.count = count;
        }

        public String getTitle() {
            return title;
        }

        public int getCount() {
            return count;
        }
    }
    public String getSectionTitle() {
        return defaultIfBlank(sectionTitle, "Fresh fruits shop");
    }

    public String getSearchPlaceholder() {
        return defaultIfBlank(searchPlaceholder, "keywords");
    }

    public String getSearchImage() {
        return searchImage;
    }

    public String getSortingLabel() {
        return defaultIfBlank(sortingLabel, "Default Sorting:");
    }

    public List<SortingOption> getSortingOptions() {
        return sortingOptions;
    }

    public String getCategoriesTitle() {
        return defaultIfBlank(categoriesTitle, "Categories");
    }

    public List<CategoryItem> getCategories() {
        return categories;
    }

    public String getPriceTitle() {
        return defaultIfBlank(priceTitle, "Price");
    }

    public int getMinPrice() {
        return minPrice != null ? Math.max(minPrice, 0) : 0;
    }

    public int getMaxPrice() {
        int resolvedMaxPrice = maxPrice != null ? Math.max(maxPrice, 0) : 500;
        return resolvedMaxPrice < getMinPrice() ? getMinPrice() : resolvedMaxPrice;
    }

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

    public String getAdditionalTitle() {
        return defaultIfBlank(additionalTitle, "Additional");
    }

    public List<AdditionalOption> getAdditionalOptions() {
        return additionalOptions;
    }

    public String getFeaturedProductsTitle() {
        return defaultIfBlank(featuredProductsTitle, "Featured products");
    }

    public List<ProductItem> getFeaturedProducts() {
        return featuredProducts;
    }

    public List<ProductItem> getFeaturedProductsPreview() {
        if (featuredProducts.isEmpty()) {
            return Collections.emptyList();
        }
        return new ArrayList<>(featuredProducts.subList(0, Math.min(FEATURED_PRODUCTS_PREVIEW_LIMIT, featuredProducts.size())));
    }

    public List<ProductItem> getRemainingFeaturedProducts() {
        if (!isFeaturedProductsExpandable()) {
            return Collections.emptyList();
        }
        return new ArrayList<>(featuredProducts.subList(FEATURED_PRODUCTS_PREVIEW_LIMIT, featuredProducts.size()));
    }

    public boolean isFeaturedProductsExpandable() {
        return featuredProducts.size() > FEATURED_PRODUCTS_PREVIEW_LIMIT;
    }

    public String getFeaturedProductsListId() {
        return getDomId() + "-featured-products";
    }

    public String getViewMoreLabel() {
        return defaultIfBlank(viewMoreLabel, "View More");
    }

    public String getViewMoreLink() {
        return defaultIfBlank(viewMoreLink, "#");
    }

    public String getBannerImage() {
        return bannerImage;
    }

    public String getBannerAltText() {
        return defaultIfBlank(bannerAltText, stripMarkup(defaultIfBlank(bannerTitle, "Fresh Fruits Banner")));
    }

    public String getBannerTitle() {
        return defaultIfBlank(bannerTitle, "Fresh Fruits Banner");
    }

    public String getDomId() {
        return "sidebar-section-" + Math.abs(resource.getPath().hashCode());
    }

    public String getSearchIconId() {
        return getDomId() + "-search-icon";
    }

    public String getSortingId() {
        return getDomId() + "-sorting";
    }

    public String getRangeInputId() {
        return getDomId() + "-range";
    }

    public String getAmountOutputId() {
        return getDomId() + "-amount";
    }

    public String getAdditionalIdPrefix() {
        return getDomId() + "-additional-";
    }

    public String getAdditionalGroupName() {
        return getDomId() + "-additional-group";
    }

    private String defaultIfBlank(String value, String fallback) {
        return isBlank(value) ? fallback : value;
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }

}
