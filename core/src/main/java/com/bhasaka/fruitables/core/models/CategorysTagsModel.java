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

@Model(
        adaptables = SlingHttpServletRequest.class,
        adapters = CategorysTagsModel.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class CategorysTagsModel {

    private static final String CATEGORY_REQUEST_PARAMETER = "category";
    private static final String DEFAULT_CARD_STYLE = "standard";

    @Self
    private SlingHttpServletRequest request;

    @SlingObject
    private Resource resource;

    @SlingObject
    private ResourceResolver resourceResolver;

    @ValueMapValue
    private String categoryTitle;

    @ValueMapValue
    private String fragmentRootPath;

    @ValueMapValue
    private String[] tags;

    @ValueMapValue
    private String cartButtonText;

    @ValueMapValue
    private String cardStyle;

    @ValueMapValue
    private String priceUnit;

    @ValueMapValue
    private Boolean showDescription;

    @ValueMapValue
    private Boolean showRating;

    @ValueMapValue
    private Boolean showCategory;

    @ValueMapValue
    private Boolean showBorder;

    @ValueMapValue
    private String categoryColor;

    @ValueMapValue
    private String badgePosition;

    @ValueMapValue
    private String emptySelectionMessage;

    @ValueMapValue
    private String noResultsMessage;

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
                matchedProducts.add(new ProductItem(product, getCardStyle()));
            }
        }

        matchedProducts.sort(Comparator.comparing(item -> safeLowerCase(item.getProduct().getProductName())));
        products = matchedProducts;
    }
    
    private List<ProductItem> products = Collections.emptyList();
    private boolean configured;
    private String selectedCategoryTagId;
    private String selectedCategoryTitle;
    private Map<String, String> authoredCategories = Collections.emptyMap();
    private List<CategoryItem> categories = Collections.emptyList();

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

    private String resolveSelectedCategory(Set<String> authoredCategoryTagIds) {
        String requestedCategory = request.getParameter(CATEGORY_REQUEST_PARAMETER);
        String normalizedRequestedCategory = ProductCategorySupport.normalizeTagId(resourceResolver, requestedCategory);
        if (authoredCategoryTagIds.contains(normalizedRequestedCategory)) {
            return normalizedRequestedCategory;
        }
        return null;
    }

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

    private String encode(String value) {
        try {
            return URLEncoder.encode(value, StandardCharsets.UTF_8.name());
        } catch (UnsupportedEncodingException e) {
            return value;
        }
    }

    private String safeLowerCase(String value) {
        return value != null ? value.toLowerCase(Locale.ENGLISH) : "";
    }

    private boolean isBlank(String value) {
        return ProductCategorySupport.isBlank(value);
    }

    public boolean isConfigured() {
        return configured;
    }

    public boolean isHasSelection() {
        return !isBlank(selectedCategoryTagId);
    }

    public boolean isHasProducts() {
        return !products.isEmpty();
    }

    public List<ProductItem> getProducts() {
        return products;
    }

    public String getCategoryTitle() {
        return !isBlank(categoryTitle) ? categoryTitle : "Categories";
    }

    public List<CategoryItem> getCategories() {
        return categories;
    }

    public String getSelectedCategoryTitle() {
        return !isBlank(selectedCategoryTitle) ? selectedCategoryTitle : "Products";
    }

    public String getCartButtonText() {
        return isBlank(cartButtonText) ? "Add to cart" : cartButtonText;
    }

    public String getCardStyle() {
        return isBlank(cardStyle) ? DEFAULT_CARD_STYLE : cardStyle;
    }

    public String getPriceUnit() {
        return priceUnit;
    }

    public boolean isShowDescription() {
        return showDescription == null || showDescription;
    }

    public boolean isShowRating() {
        return showRating == null || showRating;
    }

    public boolean isShowCategory() {
        return showCategory == null || showCategory;
    }

    public boolean isShowBorder() {
        return showBorder != null && showBorder;
    }

    public String getCategoryColor() {
        return isBlank(categoryColor) ? "orange" : categoryColor;
    }

    public String getBadgePosition() {
        return isBlank(badgePosition) ? "top-left" : badgePosition;
    }

    public String getEmptySelectionMessage() {
        return isBlank(emptySelectionMessage)
                ? "Select a category to load the matching products."
                : emptySelectionMessage;
    }

    public String getNoResultsMessage() {
        return isBlank(noResultsMessage)
                ? "No products were found for the selected category."
                : noResultsMessage;
    }

    public String getComponentId() {
        return ProductCategorySupport.buildComponentId(resource, "categorys-tags-");
    }

    public static class CategoryItem {

        private final String tagId;
        private final String title;
        private final int count;
        private final String link;
        private final boolean selected;

        public CategoryItem(String tagId, String title, int count, String link, boolean selected) {
            this.tagId = tagId;
            this.title = title;
            this.count = count;
            this.link = link;
            this.selected = selected;
        }

        public String getTagId() {
            return tagId;
        }

        public String getTitle() {
            return title;
        }

        public int getCount() {
            return count;
        }

        public String getLink() {
            return link;
        }

        public boolean isSelected() {
            return selected;
        }
    }
}
