package com.example.shoptbdt.Screen.Rating;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.example.shoptbdt.Adapter.ProductRatingAdapter;
import com.example.shoptbdt.Models.Orders;
import com.example.shoptbdt.Models.Products;
import com.example.shoptbdt.Models.RatingModel;
import com.example.shoptbdt.R;

import java.util.List;

public class RatingActivity extends AppCompatActivity {
    private EditText editTextContent;
    private RatingBar ratingBar;
    private Button submitButton;
    private Orders currentOrder;
    private RecyclerView recyclerView;
    private ProductRatingAdapter adapter;
    List<Products> productsList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rating);


        Intent intent = getIntent();
        if (intent != null) {
            currentOrder = (Orders) intent.getSerializableExtra("ordersData");
        }
        recyclerView = findViewById(R.id.recyclerViewProductRating);
        if(currentOrder!=null){
            productsList = (List<Products>) currentOrder.getProducts();
            recyclerView.setLayoutManager(new LinearLayoutManager(this));
            adapter = new ProductRatingAdapter(productsList, currentOrder.getOrderId());

            recyclerView.setAdapter(adapter);
        }


        submitButton = findViewById(R.id.submitButtonRating);
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(),"Rating coming soon", Toast.LENGTH_SHORT).show();
//                submitRating();
            }
        });
    }

    private void saveRating(String productId, float rating) {
//        RatingModel ratingModel = new RatingModel(userId, productId, rating);
//        DatabaseReference databaseReference = FirebaseDatabase.getInstance().getReference();
//        databaseReference.child("userRating").child(userId)
//                .child("userOrders").child(orderId)
//                .child("ratings").child(productId)
//                .setValue(ratingModel);
    }
}