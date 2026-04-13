package com.bhasaka.fruitables.core.models;

import org.apache.sling.api.resource.Resource;
import org.apache.sling.models.annotations.*;
import org.apache.sling.models.annotations.injectorspecific.ValueMapValue;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * Sling Model representing a Product Content Fragment.
 *
 * This model maps product-related properties from the JCR resource
 * and provides helper methods to process product rating into
 * filled and empty stars for UI display.
 */
@Model(adaptables = Resource.class, defaultInjectionStrategy = DefaultInjectionStrategy.OPTIONAL)
public class ProductCFModel {

    /** Product name */
    @ValueMapValue
    private String productName;

    /** Product description */
    @ValueMapValue
    private String productDescription;

    /** Product category */
    @ValueMapValue
    private String productCategory;

    /** Product image path */
    @ValueMapValue
    private String productImage;

    /** Product price */
    @ValueMapValue
    private String productPrice;

    /** Unit of the product (e.g., kg, piece) */
    @ValueMapValue
    private String unit;

    /** Product rating (stored as String, e.g., "4.5") */
    @ValueMapValue
    private String productRating;

    /** Maximum rating value (used for star calculation) */
    private static final int MAX_RATING = 5;

    /** Logger for error tracking */
    private static final Logger LOG = LoggerFactory.getLogger(ProductCFModel.class);

    /**
     * @return product name
     */
    public String getProductName() {
        return productName;
    }

    /**
     * @return product description
     */
    public String getProductDescription() {
        return productDescription;
    }

    /**
     * @return product category
     */
    public String getProductCategory() {
        return productCategory;
    }

    /**
     * @return product image path
     */
    public String getProductImage() {
        return productImage;
    }

    /**
     * @return product price
     */
    public String getProductPrice() {
        return productPrice;
    }

    /**
     * @return unit of the product
     */
    public String getUnit() {
        return unit;
    }

    /**
     * @return product rating as String
     */
    public String getProductRating() {
        return productRating;
    }

    /**
     * Calculates the number of filled stars based on product rating.
     *
     * Example:
     * If rating = 4.2 → rounded to 4 → returns list of size 4
     *
     * @return list representing filled stars
     */
    public List<Integer> getFilledStars() {
        List<Integer> stars = new ArrayList<>();
        try {
            double rating = Double.parseDouble(productRating);
            int filled = (int) Math.round(rating);

            for (int i = 0; i < filled; i++) {
                stars.add(i);
            }
        } catch (NumberFormatException e) {
            LOG.error("Invalid product rating value: {}", productRating, e.getMessage());
        }
        return stars;
    }

    /**
     * Calculates the number of empty stars based on remaining rating.
     *
     * Example:
     * MAX_RATING = 5, filled = 4 → empty = 1
     *
     * @return list representing empty stars
     */
    public List<Integer> getEmptyStars() {
        List<Integer> stars = new ArrayList<>();
        int empty = MAX_RATING - getFilledStars().size();

        for (int i = 0; i < empty; i++) {
            stars.add(i);
        }
        return stars;
    }
}
