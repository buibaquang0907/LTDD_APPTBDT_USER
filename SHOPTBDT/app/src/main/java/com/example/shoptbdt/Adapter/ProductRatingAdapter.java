package com.example.shoptbdt.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shoptbdt.Models.Products;
import com.example.shoptbdt.Models.RatingModel;
import com.example.shoptbdt.R;
import com.example.shoptbdt.Screen.Rating.RatingActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.List;

public class ProductRatingAdapter extends RecyclerView.Adapter<ProductRatingAdapter.ProductViewHolder> {
    private EditText editTextContent;
    private RatingBar ratingBar;
    private String orderId;
    private List<Products> productList; // Giả định rằng Orders có một danh sách các sản phẩm

    public ProductRatingAdapter(List<Products> productList, String orderId) {
        this.productList = productList;
        this.orderId = orderId;;
    }

    @NonNull
    @Override
    public ProductViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.rating_product_item, parent, false);
        return new ProductViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ProductViewHolder holder, int position) {
        Products product = productList.get(position);
        holder.productNameTextView.setText(product.getName());
        Glide.with(holder.itemView.getContext())
                .load(product.getImage())
                .into(holder.productImageView);
        holder.ratingBar.setOnRatingBarChangeListener((ratingBar, rating, fromUser) -> {
            if (fromUser) {
                submitRating(product.getId(),holder);
            }
        });
    }

    FirebaseAuth mAuth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = mAuth.getCurrentUser();
    private void submitRating(String product, @NonNull ProductViewHolder holder) {
        // Lấy nội dung đánh giá từ EditText
        String content = holder.editTextContent.getText().toString();
        String rating = String.valueOf(holder.ratingBar.getRating());

        RatingModel ratingModel = new RatingModel(currentUser.getUid(), product, content.isEmpty() ? "null" : content, rating);

        // Lấy tham chiếu đến Firestore Database
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Tạo cấu trúc dữ liệu và ghi vào Firestore
        // Structure: userRating/{userId}/userOrders/{orderId}/ratings/{productId}
        db.collection("userRating").document(currentUser.getUid())
                .collection("userOrders").document(orderId)
                .collection("ratings").document(product)
                .set(ratingModel)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        // Xử lý khi lưu thành công
                        Toast.makeText(holder.itemView.getContext(), "Rating submitted!", Toast.LENGTH_SHORT).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        // Xử lý khi có lỗi
                        Toast.makeText(holder.itemView.getContext(), "Failed to submit rating: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
    }




    @Override
    public int getItemCount() {
        return productList.size();
    }

    static class ProductViewHolder extends RecyclerView.ViewHolder {
        TextView productNameTextView;
        RatingBar ratingBar;
        EditText editTextContent;
        ImageView productImageView;// Thêm EditText

        public ProductViewHolder(View itemView) {
            super(itemView);
            productNameTextView = itemView.findViewById(R.id.productNameTextView);
            ratingBar = itemView.findViewById(R.id.productRatingBar);
            editTextContent = itemView.findViewById(R.id.contentProduct);
            productImageView = itemView.findViewById(R.id.productImageViewRating);
        }
    }
}

