package com.bhasaka.fruitables.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.models.annotations.DefaultInjectionStrategy;
import org.apache.sling.models.annotations.Model;
import org.apache.sling.models.annotations.injectorspecific.ChildResource;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;

@Model(adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ProductListModel {
    @ValueMapValue
    private String sectionTitle;
    @ValueMapValue
    private String sectionDescription;
    @ValueMapValue
    private String cartButtonText;
    @ChildResource(name = "products")
    private List<ProductResource> productResources;
    @SlingObject
    private ResourceResolver resolver;
    private List<ProductItem> items = new ArrayList<>();
    @PostConstruct
    protected void init() {
        if (productResources == null || resolver == null) {
            return;
        }

        for (ProductResource item : productResources) {
            String cfPath = item.getCfPath();
            String cardStyle = item.getCardStyle();

            Resource resource = resolver.getResource(cfPath);
            if (resource == null) continue;

            Resource master = resource.getChild("jcr:content/data/master");
            if (master != null) {
                addProduct(master, cardStyle);
                continue;
            }

            for (Resource child : resource.getChildren()) {
                Resource masterNode = child.getChild("jcr:content/data/master");
                if (masterNode != null) {
                    addProduct(masterNode, cardStyle);
                }
            }
        }
    }

    private void addProduct(Resource masterNode, String cardStyle) {
        ProductCFModel product = masterNode.adaptTo(ProductCFModel.class);
        if (product != null) {
            items.add(new ProductItem(product, cardStyle));
        }
    }

    public String getSectionTitle() {
        return sectionTitle;
    }

    public String getSectionDescription() {
        return sectionDescription;
    }

    public List<ProductItem> getItems() {
        return items;
    }
    public String getCartButtonText() {
        return cartButtonText;
    }

}