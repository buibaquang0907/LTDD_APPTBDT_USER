package com.example.shoptbdt.Models;

import com.google.firebase.firestore.PropertyName;

public class Favourite {
    private String userId;
    private String productId;
    private String productName;
    private String productImage;
    private String productPrice;

    public Favourite(String userId, String productId, String productName, String productImage, String productPrice) {
        this.userId = userId;
        this.productId = productId;
        this.productName = productName;
        this.productImage = productImage;
        this.productPrice = productPrice;
    }

    public Favourite() {
    }

    @PropertyName("userId")
    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    @PropertyName("productId")
    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    @PropertyName("productName")
    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    @PropertyName("productImage")
    public String getProductImage() {
        return productImage;
    }

    public void setProductImage(String productImage) {
        this.productImage = productImage;
    }

    @PropertyName("productPrice")
    public String getProductPrice() {
        return productPrice;
    }

    public void setProductPrice(String productPrice) {
        this.productPrice = productPrice;
    }
}
