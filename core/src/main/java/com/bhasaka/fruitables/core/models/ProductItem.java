package com.bhasaka.fruitables.core.models;

public class ProductItem {

    private ProductCFModel product;
    private String cardStyle;

    public ProductItem(ProductCFModel product, String cardStyle) {
        this.product = product;
        this.cardStyle = cardStyle;
    }

    public ProductCFModel getProduct() {
        return product;
    }

    public String getCardStyle() {
        return cardStyle;
    }
}
