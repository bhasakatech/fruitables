package com.bhasaka.fruitables.core.models;

/**
 * Model representing an item in the shopping cart.
 *
 * <p>This class holds product details such as name, price, quantity,
 * image, and calculates the total price based on quantity and price.</p>
 */
public class CartItem {

    private String name;
    private double price;
    private String image;
    private int qty;
    private double total;
    private String productPath;

    /**
     * Returns the product path.
     *
     * @return product path
     */
    public String getProductPath() {
        return productPath;
    }

    /**
     * Sets the product path.
     *
     * @param productPath path of the product
     */
    public void setProductPath(String productPath) {
        this.productPath = productPath;
    }

    /**
     * Returns the product name.
     *
     * @return product name
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the product price and recalculates total.
     *
     * @param price product price
     */
    public void setPrice(double price) {
        this.price = price;
        calculateTotal();
    }

    /**
     * Sets the product quantity and recalculates total.
     *
     * @param qty product quantity
     */
    public void setQty(int qty) {
        this.qty = qty;
        calculateTotal();
    }

    /**
     * Calculates total price based on price and quantity.
     */
    private void calculateTotal() {
        this.total = this.price * this.qty;
    }

    /**
     * Returns the total price.
     *
     * @return total price
     */
    public double getTotal() {
        return total;
    }

    /**
     * Returns the product price.
     *
     * @return product price
     */
    public double getPrice() {
        return price;
    }

    /**
     * Returns the product image path.
     *
     * @return image path
     */
    public String getImage() {
        return image;
    }

    /**
     * Returns the product quantity.
     *
     * @return quantity
     */
    public int getQty() {
        return qty;
    }

    /**
     * Sets the product name.
     *
     * @param name product name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * Sets the product image path.
     *
     * @param image image path
     */
    public void setImage(String image) {
        this.image = image;
    }
}