package com.example.shoptbdt.Screen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.shoptbdt.Models.Products;
import com.example.shoptbdt.R;
import com.example.shoptbdt.Screen.Rating.RatingActivity;
import com.example.shoptbdt.ShoppingCart;

import java.util.List;
import java.util.Map;

public class ProductsDetailActivity extends AppCompatActivity {
    private ShoppingCart shoppingCart;
    private TextView productNameTextView, productPriceTextView, productContentTextView;
    private ImageView productImageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_products_detail);
        shoppingCart = ShoppingCart.getInstance();
        Button btnAddTooCart = findViewById(R.id.btnAddtoCart);
        Intent intent = getIntent();
        if (intent != null) {
            Products product = (Products) intent.getSerializableExtra("product");

            displayProductDetails(product);
            btnAddTooCart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Thêm sản phẩm vào giỏ hàng từ OtherActivity
                    // Thay bằng cách lấy sản phẩm được chọn
                    shoppingCart.addToCart(product);

                    // Hiển thị thông báo hoặc cập nhật giao diện nếu cần thiết
                    Toast.makeText(ProductsDetailActivity.this, "Product added to cart", Toast.LENGTH_SHORT).show();
                }
            });
        }

        Button btnBuyNow = findViewById(R.id.btnBuyNow);
        btnBuyNow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Products product = (Products) intent.getSerializableExtra("product");

                displayProductDetails(product);
                shoppingCart.addToCart(product);
                Intent intent = new Intent(ProductsDetailActivity.this, YourOrdersActivity.class);
                intent.putExtra("product", product);
                startActivity(intent);
//                Map<Products, Integer> itemQuantities = shoppingCart.getCartItemQuantities();
//
//                for (Map.Entry<Products, Integer> entry : itemQuantities.entrySet()) {
//                    Products product = entry.getKey();
//                    int quantity = entry.getValue();
//
//                    // Sử dụng product và quantity theo ý muốn
//                    Log.d("ProductInfo", product.getName() + ": " + quantity);
//                }
            }
        });


    }
    private void displayProductDetails(@NonNull Products product) {
        productNameTextView = findViewById(R.id.textViewProductDetailName);
        productPriceTextView = findViewById(R.id.textViewProductDetailPrice);
        productImageView = findViewById(R.id.imageViewProductDetail);
        productContentTextView = findViewById(R.id.textViewProductDetailContent);
        productNameTextView.setText(product.getName());
        productContentTextView.setText(product.getDescription());
        productPriceTextView.setText("$" + product.getPrice());
        Glide.with(this).load(product.getImage()).into(productImageView);
    }


}