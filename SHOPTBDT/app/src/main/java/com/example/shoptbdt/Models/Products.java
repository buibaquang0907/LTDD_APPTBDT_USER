package com.example.shoptbdt.Models;

import com.google.firebase.firestore.DocumentReference;

import java.io.Serializable;

public class Products implements Serializable {
    private String id;
    private String name;
    private String image;
    private double price;
    private String description;
    private String status;

    private DocumentReference category;

    public Products(String id, String name, String image, double price, String description, String status, DocumentReference category) {
        this.id = id;
        this.name = name;
        this.image = image;
        this.price = price;
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

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
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
