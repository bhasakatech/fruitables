package com.bhasaka.fruitables.core.models;

import com.day.cq.wcm.api.Page;
import com.day.cq.wcm.api.PageManager;
import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;

@Model(adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ShopBannerModel {

    /**
     * Stores the shop banner title authored in the content.
     */
    @ValueMapValue
    private String shopBannerTitle;

    /**
     * Stores the shop banner image URL/path authored in the content.
     */
    @ValueMapValue
    private String shopBannerImage;

    /**
     * Sling objects to access current resource and resource resolver for page retrieval.
     */
    @SlingObject
    private Resource currentResource;

    /**
     * ResourceResolver is used to adapt to PageManager and retrieve the containing page for fallback title logic.
     */
    @SlingObject
    private ResourceResolver resourceResolver;

    /**
     * Returns the shop banner title.
     *
     * @return shop banner title
     */
    public String getShopBannerTitle() {
        if (!isBlank(shopBannerTitle)) {
            return shopBannerTitle;
        }
        return getContainingPageName();
    }

    /**
     * Returns the authored shop banner title.
     *
     * @return authored shop banner title
     */
    public String getAuthoredShopBannerTitle() {
        return shopBannerTitle;
    }

    /**
     * Returns the shop banner image URL.
     *
     * @return shop banner image URL/path
     */
    public String getShopBannerImage() {
        return shopBannerImage;
    }

    /**
     * Retrieves the name of the containing page to use as a fallback title.
     *
     * @return containing page name or null if not available
     */
    private String getContainingPageName() {
        PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
        Page containingPage = pageManager.getContainingPage(currentResource);
        return containingPage != null ? containingPage.getName() : null;
    }

    /**
     * Checks if the given string is null or empty.
     *
     * @param value the string to check
     * @return true if the string is null or empty, false otherwise
     */
    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
