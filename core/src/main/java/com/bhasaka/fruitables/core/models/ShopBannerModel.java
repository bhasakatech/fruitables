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

    @ValueMapValue
    private String shopBannerTitle;

    @ValueMapValue
    private String shopBannerImage;

    @SlingObject
    private Resource currentResource;

    @SlingObject
    private ResourceResolver resourceResolver;

    public String getShopBannerTitle() {
        if (!isBlank(shopBannerTitle)) {
            return shopBannerTitle;
        }
        return getContainingPageName();
    }

    public String getAuthoredShopBannerTitle() {
        return shopBannerTitle;
    }

    public String getShopBannerImage() {
        return shopBannerImage;
    }

    private String getContainingPageName() {
        PageManager pageManager = resourceResolver.adaptTo(PageManager.class);
        Page containingPage = pageManager.getContainingPage(currentResource);
        return containingPage != null ? containingPage.getName() : null;
    }

    private boolean isBlank(String value) {
        return value == null || value.trim().isEmpty();
    }
}
