package com.example.shoptbdt.Models;

import com.google.firebase.firestore.DocumentReference;

public class Products {
    private String id;
    private String name;
    private String image;
    private boolean isFavourite;
    private double price;
    private int quantity;
    private String description;
    private String status;

    private DocumentReference category;

    public Products(String id, String name, String image, boolean isFavourite, double price, int quantity, String description, String status, DocumentReference category) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.isFavourite = isFavourite;
        this.price = price;
        this.quantity = quantity;
        this.description = description;
        this.status = status;
        this.category = category;
    }

    public Products() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public boolean isFavourite() {
        return isFavourite;
    }

    public void setFavourite(boolean favourite) {
        isFavourite = favourite;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public DocumentReference getCategory() {
        return category;
    }

    public void setCategory(DocumentReference category) {
        this.category = category;
    }
}
