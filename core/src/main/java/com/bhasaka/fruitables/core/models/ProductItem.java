package com.bhasaka.fruitables.core.models;

public class ProductItem {

    private ProductCFModel product;
    private String cardStyle;
    private String productPath; 

    public ProductItem(ProductCFModel product, String cardStyle, String productPath) {
        this.product = product;
        this.cardStyle = cardStyle;
        this.productPath = productPath;
    }

    public ProductCFModel getProduct() {
        return product;
    }

    public String getCardStyle() {
        return cardStyle;
    }

    public String getProductPath() {  
        return productPath;
    }
}