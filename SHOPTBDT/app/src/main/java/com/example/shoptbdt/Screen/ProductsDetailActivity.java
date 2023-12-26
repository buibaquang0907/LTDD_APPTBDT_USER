package com.example.shoptbdt.Screen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.shoptbdt.Models.Products;
import com.example.shoptbdt.R;

public class ProductsDetailActivity extends AppCompatActivity {

    private TextView productNameTextView, productPriceTextView;
    private ImageView productImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_detail);
        Intent intent = getIntent();
        if (intent != null) {
            Products product = (Products) intent.getSerializableExtra("product");

            displayProductDetails(product);
        }
    }
    private void displayProductDetails(@NonNull Products product) {
        productNameTextView = findViewById(R.id.textViewProductDetailName);
        productPriceTextView = findViewById(R.id.textViewProductDetailPrice);
        productImageView = findViewById(R.id.imageViewProductDetail);

        productNameTextView.setText(product.getName());
        productPriceTextView.setText("$" + product.getPrice());
        Glide.with(this).load(product.getImage()).into(productImageView);
    }
}