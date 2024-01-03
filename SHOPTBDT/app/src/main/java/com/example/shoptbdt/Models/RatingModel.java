package com.example.shoptbdt.Models;

public class RatingModel {
    public RatingModel(String userId, String productId, String content, String ranting) {
        this.userId = userId;
        this.productId = productId;
        this.content = content;
        this.ranting = ranting;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getRanting() {
        return ranting;
    }

    public void setRanting(String ranting) {
        this.ranting = ranting;
    }

    private String userId;
    private String productId;
    private String content;
    private String ranting;
}
