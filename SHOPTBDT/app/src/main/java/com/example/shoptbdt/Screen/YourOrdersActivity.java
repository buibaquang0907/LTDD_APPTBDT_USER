package com.example.shoptbdt.Screen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.shoptbdt.Adapter.ProductAdapter;
import com.example.shoptbdt.Models.Orders;
import com.example.shoptbdt.Models.Products;
import com.example.shoptbdt.Models.User;
import com.example.shoptbdt.R;
import com.example.shoptbdt.ShoppingCart;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicReference;

public class YourOrdersActivity extends AppCompatActivity implements ProductAdapter.OnProductClickListener {
    private RecyclerView recyclerViewProducts;
    private List<Products> productList;
    private ProductAdapter productAdapter;
    private ShoppingCart shoppingCart;
    Button btnPayment;
    private User userFirebase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_orders);
        shoppingCart = ShoppingCart.getInstance();
        recyclerViewProducts = findViewById(R.id.rcvViewOrders);
        btnPayment = findViewById(R.id.btnPayment);

        // Initialize productList (if necessary)
        productList = new ArrayList<>(); // or fetch data from somewhere

        recyclerViewProducts.setLayoutManager(new LinearLayoutManager(this));

        if(shoppingCart.getLenghtShoppingCart()!=0){
            productList = shoppingCart.getShoppingCart();
            productAdapter = new ProductAdapter(productList, this);

            productAdapter.notifyDataSetChanged();
            recyclerViewProducts.setAdapter(productAdapter);
        }
        btnPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveOrders();

            }
        });

    }

    @Override
    public void onProductClick(Products product) {
        shoppingCart.removeToCart(product);
        productAdapter.notifyDataSetChanged();
        Toast.makeText(YourOrdersActivity.this, "Product removd to cart", Toast.LENGTH_SHORT).show();
    }
    public synchronized void saveOrders() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser currentUser = mAuth.getCurrentUser();

        if (currentUser == null) {
            // Xử lý trường hợp không có người dùng hiện tại
            return;
        }

        db.collection("users").document(currentUser.getUid()).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document != null && document.exists()) {
                    User userFirebase = document.toObject(User.class);
                    if (userFirebase != null) {
                        // Tạo và lưu đơn hàng sau khi có dữ liệu người dùng
                        createAndSaveOrder(userFirebase, currentUser.getUid());
                    }
                } else {
                    // Xử lý không tìm thấy người dùng
                    Toast.makeText(this, "User not found", Toast.LENGTH_SHORT).show();
                }
            } else {
                // Xử lý lỗi
                Toast.makeText(this, "Error loading user data", Toast.LENGTH_SHORT).show();
            }
        });
}

    private double getTotalPrice(){
        Map<Products, Integer> itemQuantities = shoppingCart.getCartItemQuantities();
        double totalPrice = 0.0;
        for (Map.Entry<Products, Integer> entry : itemQuantities.entrySet()) {
            Products product = entry.getKey();
            int quantity = entry.getValue();

            totalPrice+= product.getPrice()*quantity;
        }
        return totalPrice ;
    }
    // Phương thức để tạo và lưu đơn hàng
    private void createAndSaveOrder(User userFirebase, String userId) {
        Orders order = new Orders();
        order.setShippingAddress(userFirebase.getAddress());

        // Định dạng thời gian hiện tại
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String currentTime = dateFormat.format(new Date());
        order.setDateOrder(currentTime);

        // Thiết lập các thông tin khác của đơn hàng
        order.setProducts(productList); // Giả sử productList đã được khai báo và khởi tạo
        order.setDateCancelOrder("");
        order.setDateCompletedOrder("");
        order.setStatus("pending");
        order.setUserId(userId);
        order.setPayment("Online");
        order.setStatusReview("Review");
        order.setTotalPrice(getTotalPrice());

        // Lưu đơn hàng vào Firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("orders").add(order)
                .addOnSuccessListener(documentReference -> {
                    // Xử lý thành công
                    String orderId = documentReference.getId();

                    // Cập nhật đơn hàng với orderId
                    order.setOrderId(orderId);

                    // Lưu lại thông tin cập nhật vào Firestore
                    db.collection("orders").document(orderId).set(order)
                            .addOnSuccessListener(aVoid -> {
                                Toast.makeText(this, "Order successfully placed with ID: " + orderId, Toast.LENGTH_SHORT).show();
                            })
                            .addOnFailureListener(e -> {
                                Toast.makeText(this, "Error updating order with ID", Toast.LENGTH_SHORT).show();
                            });
                    Toast.makeText(this, "Order successfully placed", Toast.LENGTH_SHORT).show();
                })
                .addOnFailureListener(e -> {
                    // Xử lý lỗi
                    Toast.makeText(this, "Error placing order", Toast.LENGTH_SHORT).show();
                });
    }


}
