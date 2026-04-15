package com.bhasaka.fruitables.core.models;
import com.bhasaka.fruitables.core.service.ProductService;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.resource.*;
import org.apache.sling.models.annotations.*;
import org.apache.sling.models.annotations.injectorspecific.SlingObject;
import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
@Model(
        adaptables = SlingHttpServletRequest.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class ProductDetailModel {
    @SlingObject
    private SlingHttpServletRequest request;
    @SlingObject
    private ResourceResolver resolver;
    private ProductCFModel product;
    public ProductCFModel getProduct() {
        return product;
    }
    protected ProductCFModel fetchProduct(String slug) {
        return ProductService.getProductBySlug(resolver, slug);
    }
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