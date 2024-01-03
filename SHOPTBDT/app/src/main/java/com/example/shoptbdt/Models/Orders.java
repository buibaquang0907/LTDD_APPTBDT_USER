package com.example.shoptbdt.Models;

import java.io.Serializable;
import java.util.List;

public class Orders implements Serializable {
    String orderId;
    String payment;
    String status;
    String statusReview;
    List<Products> products;
    double totalPrice;
    String userId;
    String dateOrder;
    String dateCompletedOrder;
    String dateCancelOrder;
    String shippingAddress;

    public Orders(String orderId, String payment, String status, String statusReview, List<Products> products, double totalPrice, String userId, String dateOrder, String dateCompletedOrder, String dateCancelOrder, String shippingAddress) {
        this.orderId = orderId;
        this.payment = payment;
        this.status = status;
        this.statusReview = statusReview;
        this.products = products;
        this.totalPrice = totalPrice;
        this.userId = userId;
        this.dateOrder = dateOrder;
        this.dateCompletedOrder = dateCompletedOrder;
        this.dateCancelOrder = dateCancelOrder;
        this.shippingAddress = shippingAddress;
    }

    public Orders() {
    }

    public String getOrderId() {
        return orderId;
    }

    public void setOrderId(String orderId) {
        this.orderId = orderId;
    }

    public String getPayment() {
        return payment;
    }

    public void setPayment(String payment) {
        this.payment = payment;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatusReview() {
        return statusReview;
    }

    public void setStatusReview(String statusReview) {
        this.statusReview = statusReview;
    }

    public List<Products> getProducts() {
        return products;
    }

    public void setProducts(List<Products> products) {
        this.products = products;
    }

    public double getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(double totalPrice) {
        this.totalPrice = totalPrice;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getDateOrder() {
        return dateOrder;
    }

    public void setDateOrder(String dateOrder) {
        this.dateOrder = dateOrder;
    }

    public String getDateCompletedOrder() {
        return dateCompletedOrder;
    }

    public void setDateCompletedOrder(String dateCompletedOrder) {
        this.dateCompletedOrder = dateCompletedOrder;
    }

    public String getDateCancelOrder() {
        return dateCancelOrder;
    }

    public void setDateCancelOrder(String dateCancelOrder) {
        this.dateCancelOrder = dateCancelOrder;
    }

    public String getShippingAddress() {
        return shippingAddress;
    }

    public void setShippingAddress(String shippingAddress) {
        this.shippingAddress = shippingAddress;
    }
}
