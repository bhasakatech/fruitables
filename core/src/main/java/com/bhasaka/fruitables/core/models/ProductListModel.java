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

/**
 * Sling Model for handling a list of products in the Fruitables application.
 *
 * <p>
 * This model adapts from a {@link Resource} and is responsible for:
 * <ul>
 * <li>Reading component properties such as section title, description, and cart
 * button text</li>
 * <li>Fetching product resources from child nodes</li>
 * <li>Resolving Content Fragment (CF) paths</li>
 * <li>Adapting CF data into {@link ProductCFModel}</li>
 * <li>Building a list of {@link ProductItem} objects</li>
 * <li>Extracting unique product categories</li>
 * </ul>
 * </p>
 *
 * <p>
 * The model initializes data in the {@link #init()} method using
 * {@link PostConstruct}.
 * </p>
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ProductListModel {

    /**
     * Title of the product section.
     */
    @ValueMapValue
    private String sectionTitle;

    /**
     * Description of the product section.
     */
    @ValueMapValue
    private String sectionDescription;

    /**
     * Text displayed on the "Add to Cart" button.
     */
    @ValueMapValue
    private String cartButtonText;

    /**
     * Child resources representing configured products.
     */
    @ChildResource(name = "products")
    private List<ProductResource> productResources;

    /**
     * ResourceResolver used to resolve Content Fragment paths.
     */
    @SlingObject
    private ResourceResolver resolver;

    /**
     * List of processed product items.
     */
    private List<ProductItem> items = new ArrayList<>();

    /**
     * Initializes the model after all injections are completed.
     *
     * <p>
     * This method:
     * <ul>
     * <li>Validates required injections</li>
     * <li>Iterates through configured product resources</li>
     * <li>Resolves Content Fragment paths</li>
     * <li>Fetches master nodes</li>
     * <li>Populates the {@link #items} list</li>
     * </ul>
     * </p>
     */
    @PostConstruct
    protected void init() {
        if (productResources == null || resolver == null) {
            return;
        }

        for (ProductResource item : productResources) {
            String cfPath = item.getCfPath();
            String cardStyle = item.getCardStyle();

            Resource resource = resolver.getResource(cfPath);
            if (resource == null)
                continue;

            Resource master = resource.getChild("jcr:content/data/master");
            if (master != null) {
                addProduct(master, cardStyle, cfPath);
                continue;
            }

            for (Resource child : resource.getChildren()) {
                Resource masterNode = child.getChild("jcr:content/data/master");
                if (masterNode != null) {
                    addProduct(masterNode, cardStyle, cfPath);
                }
            }
        }
    }

    /**
     * Adds a product to the items list by adapting the resource to
     * {@link ProductCFModel}.
     *
     * @param masterNode  the Content Fragment master node
     * @param cardStyle   the style to be applied to the product card
     * @param productPath the path of the product resource
     */
    private void addProduct(Resource masterNode, String cardStyle, String productPath) {
        ProductCFModel product = masterNode.adaptTo(ProductCFModel.class);
        if (product != null) {
            items.add(new ProductItem(product, cardStyle, productPath));
        }
    }

    /**
     * Retrieves a list of unique product categories.
     *
     * <p>
     * This method:
     * <ul>
     * <li>Iterates through all product items</li>
     * <li>Extracts category values</li>
     * <li>Removes duplicates</li>
     * </ul>
     * </p>
     *
     * @return list of unique product categories
     */
    public List<String> getCategories() {
        List<String> categories = new ArrayList<>();

        for (ProductItem item : items) {
            if (item.getProduct() != null &&
                    item.getProduct().getProductCategory() != null) {

                String category = item.getProduct().getProductCategory().trim();

                if (!categories.contains(category)) {
                    categories.add(category);
                }
            }
        }
        return categories;
    }

    /**
     * Gets the section title.
     *
     * @return section title
     */
    public String getSectionTitle() {
        return sectionTitle;
    }

    /**
     * Gets the section description.
     *
     * @return section description
     */
    public String getSectionDescription() {
        return sectionDescription;
    }

    /**
     * Gets the list of product items.
     *
     * @return list of {@link ProductItem}
     */
    public List<ProductItem> getItems() {
        return new ArrayList<>(items);
    }

    /**
     * Gets the cart button text.
     *
     * @return cart button label
     */
    public String getCartButtonText() {
        return cartButtonText;
    }
}