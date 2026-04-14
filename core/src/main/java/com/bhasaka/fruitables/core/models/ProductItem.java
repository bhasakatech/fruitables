package com.bhasaka.fruitables.core.models;

/**
 * Represents a product item in the product list component.
 *
 * <p>
 * This class acts as a wrapper for product-related data and UI configuration.
 * It holds:
 * <ul>
 * <li>The product model (Content Fragment data)</li>
 * <li>The card style used for rendering</li>
 * <li>An optional product resource path</li>
 * </ul>
 * </p>
 *
 * <p>
 * Supports initialization using different product model types such as
 * {@link ProductCFModel} and {@link ProductCFModelTag}.
 * </p>
 */
public class ProductItem {

    /**
     * Product data model representing Content Fragment information.
     */
    private ProductCFModel product;

    /**
     * Style applied to the product card (e.g., compact, detailed).
     */
    private String cardStyle;

    /**
     * Path of the product resource in the repository.
     */
    private String productPath;

    /**
     * Constructs a ProductItem with product data and card style.
     *
     * @param product   the product model
     * @param cardStyle the style applied to the product card
     */
    public ProductItem(ProductCFModel product, String cardStyle) {
        this(product, cardStyle, null);
    }

    /**
     * Constructs a ProductItem with product data, card style, and product path.
     *
     * @param product     the product model
     * @param cardStyle   the style applied to the product card
     * @param productPath the path of the product resource
     */
    public ProductItem(ProductCFModel product, String cardStyle, String productPath) {
        this.product = product;
        this.cardStyle = cardStyle;
        this.productPath = productPath;
    }

    /**
     * Constructs a ProductItem using a tag-based product model and card style.
     *
     * @param product   the tag-based product model
     * @param cardStyle the style applied to the product card
     */
    public ProductItem(ProductCFModelTag product, String cardStyle) {
        this(product, cardStyle, null);
    }

    /**
     * Constructs a ProductItem using a tag-based product model with full details.
     *
     * @param product     the tag-based product model
     * @param cardStyle   the style applied to the product card
     * @param productPath the path of the product resource
     */
    public ProductItem(ProductCFModelTag product, String cardStyle, String productPath) {
        this.product = product;
        this.cardStyle = cardStyle;
        this.productPath = productPath;
    }

    /**
     * Returns the product model.
     *
     * @return the product data
     */
    public ProductCFModel getProduct() {
        return product;
    }

    /**
     * Returns the card style.
     *
     * @return the card style
     */
    public String getCardStyle() {
        return cardStyle;
    }

    /**
     * Returns the product resource path.
     *
     * @return the product path
     */
    public String getProductPath() {
        return productPath;
    }
}