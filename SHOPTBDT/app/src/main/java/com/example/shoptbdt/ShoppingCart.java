package com.example.shoptbdt;

import com.example.shoptbdt.Models.Products;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ShoppingCart {
    private static ShoppingCart instance;
    private List<Products> cartItems;

    private ShoppingCart() {
        cartItems = new ArrayList<>();
    }



    public static synchronized ShoppingCart getInstance() {
        if (instance == null) {
            instance = new ShoppingCart();
        }
        return instance;
    }

    public void addToCart(Products product) {
        cartItems.add(product);
    }
    public void removeToCart(Products product) {
        cartItems.remove(product);
    }
    public void clearCart(){
        cartItems.clear();
    }

    public List<Products> getShoppingCart(){return cartItems;}
    public int getLenghtShoppingCart() {return cartItems.size();}

    public Map<Products, Integer> getCartItemQuantities() {
        Map<Products, Integer> itemQuantities = new HashMap<>();
        for (Products product : cartItems) {
            int quantity = itemQuantities.getOrDefault(product, 0);
            itemQuantities.put(product, quantity + 1);
        }
        return itemQuantities;
    }
}
