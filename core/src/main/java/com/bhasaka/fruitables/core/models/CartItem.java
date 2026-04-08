package com.bhasaka.fruitables.core.models;

public class CartItem {


    private String name;
    private double price;
    private String image;
    private int qty;
    private double total;
    private String productPath;

    public String getProductPath() {
        return productPath;
    }

    public void setProductPath(String productPath) {
        this.productPath = productPath;
    }

    public String getName() {
        return name;
    }
    public void setPrice(double price) {
        this.price = price;
        calculateTotal();
    }

    public void setQty(int qty) {
        this.qty = qty;
        calculateTotal();
    }

    private void calculateTotal() {
        this.total = this.price * this.qty;
    }

    public double getTotal() {
        return total;
    }

    public double getPrice() {
        return price;
    }

    public String getImage() {
        return image;
    }

    public int getQty() {
        return qty;
    }

    public void setName(String name) {
        this.name = name;
    }



    public void setImage(String image) {
        this.image = image;
    }


}
