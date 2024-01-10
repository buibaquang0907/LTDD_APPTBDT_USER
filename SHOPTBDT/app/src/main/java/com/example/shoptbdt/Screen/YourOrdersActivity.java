package com.example.shoptbdt.Screen;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.shoptbdt.Adapter.ProductAdapter;
import com.example.shoptbdt.EmailSender;
import com.example.shoptbdt.Models.Orders;
import com.example.shoptbdt.Models.Products;
import com.example.shoptbdt.Models.User;
import com.example.shoptbdt.R;
import com.example.shoptbdt.ShoppingCart;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.stripe.android.PaymentConfiguration;
import com.stripe.android.paymentsheet.PaymentSheet;
import com.stripe.android.paymentsheet.PaymentSheetResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class YourOrdersActivity extends AppCompatActivity implements ProductAdapter.OnProductClickListener, ProductAdapter.OnFavouriteClickListener {
    private RecyclerView recyclerViewProducts;
    private List<Products> productList;
    private ProductAdapter productAdapter;
    private ShoppingCart shoppingCart;
    FloatingActionButton btnPayment;
    String Publishablekey = "pk_test_51NcQH0CizuobP5vV9ZC0fDWT25Or9yeykFi2i5JXqARUstruauJWUMJqSDUIz2OxQj8vV1fa0Ytmolnmltx1xl1s00bihWFCpt";
    String Secretkey = "sk_test_51NcQH0CizuobP5vVKDY72s3RjPXJ6c0uyHGYZehGXOx3wzrWbKkoMGrbVB0ZC0kW7Oxe9EibT00T03GPlzIkDi6N00LjkfH6eP";
    String CustomerId;
    String EnphericalKey;
    String ClientSecret;
    PaymentSheet paymentSheet;
    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_your_orders);
        shoppingCart = ShoppingCart.getInstance();
        Button btnBack = findViewById(R.id.btnBack);

        // Đặt lắng nghe sự kiện khi nút Back được nhấn
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed(); // Gọi phương thức onBackPressed để quay trở lại màn hình trước đó
            }
        });
        recyclerViewProducts = findViewById(R.id.rcvViewOrders);
        btnPayment = findViewById(R.id.fabChoosePayment);

        btnPayment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showPaymentMethodDialog();
            }
        });

        // Initialize productList (if necessary)
        productList = new ArrayList<>(); // or fetch data from somewhere

        recyclerViewProducts.setLayoutManager(new LinearLayoutManager(this));

        if(shoppingCart.getLenghtShoppingCart()!=0){
            productList = shoppingCart.getShoppingCart();
            productAdapter = new ProductAdapter(productList, this, this);

            productAdapter.notifyDataSetChanged();
            recyclerViewProducts.setAdapter(productAdapter);
        }

        PaymentConfiguration.init(this, Publishablekey);
        paymentSheet = new PaymentSheet(this,paymentSheetResult -> {
            onPaymentResult(paymentSheetResult);
        });


        createCustomerAndGetId();
    }

    public void showPaymentMethodDialog() {
        String[] paymentMethods = {"Online Payment", "Cash On Delivery"};
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Choose Payment Method");
        builder.setSingleChoiceItems(paymentMethods, -1, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == 0) {
                    // User chose Online Payment
                    paymentFlow();
                } else {
                    // User chose Cash On Delivery
                    saveOrders("COD");
                }
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("Cancel", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }

    private void paymentFlow() {
        if (ClientSecret == null || EnphericalKey == null) {
            Log.e("PaymentFlow", "ClientSecret or EnphericalKey is null");
            return;
        }

        paymentSheet.presentWithPaymentIntent(
                ClientSecret,
                new PaymentSheet.Configuration(
                        "TBDT-Shop", // Replace with your actual business name
                        new PaymentSheet.CustomerConfiguration(CustomerId, EnphericalKey)
                )
        );
    }

    private void onPaymentResult(PaymentSheetResult paymentSheetResult) {
        if (paymentSheetResult instanceof PaymentSheetResult.Completed) {
            saveOrders("Online");

            Toast.makeText(YourOrdersActivity.this, "Payment succeeded", Toast.LENGTH_SHORT).show();
        }
        // Handle other cases (Cancelled, Failed, etc.)
    }
    private void getClientSecret(String customerId, String enphericalKey) {
        StringRequest request = new StringRequest(Request.Method.POST, "https://api.stripe.com/v1/payment_intents", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    ClientSecret = object.getString("client_secret");
                    Toast.makeText(YourOrdersActivity.this,CustomerId,Toast.LENGTH_SHORT).show();


                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(YourOrdersActivity.this,error.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> header = new HashMap<>();
                header.put("Authorization","Bearer " + Secretkey);



                return header;
            }

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("customer",CustomerId);
                params.put("amount","100"+"00");
                params.put("currency","USD");
                params.put("automatic_payment_methods[enabled]","true");
                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }
    private void getEnphericalKey() {
        StringRequest request = new StringRequest(Request.Method.POST, "https://api.stripe.com/v1/ephemeral_keys", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    EnphericalKey = object.getString("id");
                    Toast.makeText(YourOrdersActivity.this,CustomerId,Toast.LENGTH_SHORT).show();
                    getClientSecret(CustomerId,EnphericalKey);

                }catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(YourOrdersActivity.this,error.getLocalizedMessage(),Toast.LENGTH_SHORT).show();
            }
        }){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {

                Map<String, String> header = new HashMap<>();
                header.put("Authorization","Bearer " + Secretkey);

                header.put("Stripe-Version", "2022-11-15");

                return header;
            }

            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("customer",CustomerId);

                return params;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }

    private void createCustomerAndGetId() {
        StringRequest request = new StringRequest(Request.Method.POST, "https://api.stripe.com/v1/customers", new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    JSONObject object = new JSONObject(response);
                    CustomerId = object.getString("id");
                    Toast.makeText(YourOrdersActivity.this, CustomerId, Toast.LENGTH_SHORT).show();
                    getEnphericalKey();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(YourOrdersActivity.this, error.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> header = new HashMap<>();
                header.put("Authorization", "Bearer " + Secretkey);
                return header;
            }
        };

        RequestQueue requestQueue = Volley.newRequestQueue(this);
        requestQueue.add(request);
    }


    @Override
    public void onProductClick(Products product) {
        shoppingCart.removeToCart(product);
        productAdapter.notifyDataSetChanged();
        Toast.makeText(YourOrdersActivity.this, "Product removd to cart", Toast.LENGTH_SHORT).show();
    }

    public synchronized void saveOrders(String paymentMethod) {
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
                        createAndSaveOrder(userFirebase, currentUser.getUid(),paymentMethod);
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
    private void createAndSaveOrder(User userFirebase, String userId,String paymentMethod) {
        Orders order = new Orders();
        order.setShippingAddress(userFirebase.getAddress());

        // Định dạng thời gian hiện tại
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String currentTime = dateFormat.format(new Date());
        order.setDateOrder(currentTime);

        // Thiết lập các thông tin khác của đơn hàng
        order.setProducts(shoppingCart.getShoppingCart()); // Giả sử productList đã được khai báo và khởi tạo
        order.setDateCancelOrder("");
        order.setDateCompletedOrder("");
        order.setStatus("pending");
        order.setUserId(userId);
        order.setPayment(paymentMethod);
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
                    sendEmailOnOrderSuccess(order);
                    // Lưu lại thông tin cập nhật vào Firestore
                    db.collection("orders").document(orderId).set(order)
                            .addOnSuccessListener(aVoid -> {
                                shoppingCart.clearCart();
                                productAdapter.notifyDataSetChanged();
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

    private void sendEmailOnOrderSuccess(Orders order) {
        if (currentUser != null) {
            String userEmail = currentUser.getEmail();
            String userName = currentUser.getDisplayName();
            String subject = "Order Confirmation - TBDT Shop";
            StringBuilder messageBuilder = new StringBuilder("Thank you for your order!\n");
            for (Products product : order.getProducts()) {
                messageBuilder.append("Product Name: ").append(product.getName()).append("\n");
            }
            messageBuilder.append("Total Price: ").append(order.getTotalPrice()).append("\n");
            messageBuilder.append("Order Date: ").append(order.getDateOrder()).append("\n");
            messageBuilder.append("Shipping Address: ").append(order.getShippingAddress());

            String message = messageBuilder.toString();
            EmailSender.sendEmail(userName, userEmail, subject, message);
        }
    }

    @Override
    public void onFavouriteClick(Products product) {

    }
}
