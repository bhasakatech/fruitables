package com.bhasaka.fruitables.core.models;
import com.bhasaka.fruitables.core.service.ProductService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.*;
import org.apache.sling.models.annotations.*;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
/**
 * Sling Model for handling Product Detail functionality.
 * <p>
 * This model is responsible for:
 * <ul>
 *     <li>Fetching product data based on the URL selector (slug).</li>
 *     <li>Providing product details to the HTL component.</li>
 *     <li>Generating star ratings (filled and empty) for UI display.</li>
 * </ul>
 * </p>
 */
@Model(
        adaptables = SlingHttpServletRequest.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class ProductDetailModel {

    /**
     * Sling HTTP request object used to extract request-related data.
     */
    @SlingObject
    private SlingHttpServletRequest request;

    /**
     * ResourceResolver used to access AEM resources.
     */
    @SlingObject
    private ResourceResolver resolver;

    /**
     * Holds the product Content Fragment model.
     */
    private ProductCFModel product;

    /**
     * Returns the fetched product details.
     *
     * @return {@link ProductCFModel} representing the product, or null if not found
     */
    public ProductCFModel getProduct() {
        return product;
    }
    /**
     * Fetches a product using the given slug.
     *
     * @param slug the product identifier derived from the URL
     * @return {@link ProductCFModel} if found, otherwise null
     */
    protected ProductCFModel fetchProduct(String slug) {
        return ProductService.getProductBySlug(resolver, slug);
    }
    /**
     * Generates a list of filled stars based on the product rating.
     * <p>
     * Used for rendering filled star icons in the UI.
     * </p>
     *
     * @return list of integers representing filled stars
     */
    public List<Integer> getReviewFilledStars() {
        List<Integer> stars = new ArrayList<>();
        if (product != null) {
            int rating = product.getRating();
            for (int i = 0; i < rating; i++) {
                stars.add(i);
            }
        }
        return stars;
    }

    /**
     * Generates a list of empty stars based on the product rating.
     * <p>
     * Used for rendering empty star icons in the UI.
     * </p>
     *
     * @return list of integers representing empty stars
     */
    public List<Integer> getReviewEmptyStars() {
        List<Integer> stars = new ArrayList<>();
        if (product != null) {
            int rating = product.getRating();
            for (int i = rating; i < 5; i++) {
                stars.add(i);
            }
        }
        return stars;
    }

    /**
     * Initializes the model after all injections are completed.
     * <p>
     * This method:
     * <ul>
     *     <li>Extracts the slug from the request selectors.</li>
     *     <li>Uses a default slug ("broccoli") if none is provided.</li>
     *     <li>Fetches the product based on the slug.</li>
     *     <li>Falls back to default product if no match is found.</li>
     * </ul>
     * </p>
     */
    @PostConstruct
    protected void init() {
        String[] selectors = request.getRequestPathInfo().getSelectors();
        String slug = null;
        if (selectors != null && selectors.length > 0) {
            slug = selectors[0];
        }
        if (slug == null || slug.isEmpty()) {
            slug = "broccoli";
        }
        slug = slug.toLowerCase().trim();
        product = fetchProduct(slug);
        if (product == null) {
            product = fetchProduct("broccoli");
        }
    }
}