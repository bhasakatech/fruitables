package com.bhasaka.fruitables.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.*;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

@Model(
        adaptables = Resource.class,
        defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL
)
public class ProductCFModel {

    @ValueMapValue
    private String productName;
    @ValueMapValue
    private String productDescription;
    @ValueMapValue
    private String productCategory;
    @ValueMapValue
    private String productImage;
    @ValueMapValue
    private String productPrice;
    @ValueMapValue
    private String unit;
    @ValueMapValue
    private String productRating;
    private static final Logger LOG = LoggerFactory.getLogger(ProductCFModel.class);

    public String getProductName() {
        return productName;
    }
    public String getProductDescription() {
        return productDescription;
    }
    public String getProductCategory() {
        return productCategory;
    }
    public String getProductImage() {
        return productImage;
    }
    public String getProductPrice() {
        return productPrice;
    }
    public String getUnit() {
        return unit;
    }
    public String getProductRating() {
        return productRating;
    }
    public List<Integer> getFilledStars() {
        List<Integer> stars = new ArrayList<>();
        try {
            double rating = Double.parseDouble(productRating);
            int filled = (int) Math.round(rating);
            for (int i = 0; i < filled; i++) {
                stars.add(i);
            }
        } catch (NumberFormatException e) {
            LOG.error("Invalid product rating value: {}", productRating, e);
        }
        return stars;
    }
    public List<Integer> getEmptyStars() {
        List<Integer> stars = new ArrayList<>();
        int empty = 5 - getFilledStars().size();
        for (int i = 0; i < empty; i++) {
            stars.add(i);
        }
        return stars;
    }
}
