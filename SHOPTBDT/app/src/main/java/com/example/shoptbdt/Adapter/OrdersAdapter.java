package com.example.shoptbdt.Adapter;

import android.content.Context;
import android.content.Intent;
import android.nfc.Tag;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shoptbdt.Models.Orders;
import com.example.shoptbdt.R;
import com.example.shoptbdt.Screen.Rating.RatingActivity;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class OrdersAdapter extends RecyclerView.Adapter<OrdersAdapter.OrdersViewHolder> {

    private List<Orders> ordersList;
    private Context context;

    public OrdersAdapter(Context context, List<Orders> ordersList) {
        this.context = context;
        this.ordersList = ordersList;
    }

    @NonNull
    @Override
    public OrdersViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_order, parent, false);
        return new OrdersViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrdersViewHolder holder, int position) {
        Orders currentOrder = ordersList.get(position);
        Glide.with(holder.itemView.getContext())
                .load(currentOrder.getProducts().get(0).getImage())
                .into(holder.productImageView);
        holder.productNameTextView.setText("Product: " + currentOrder.getProducts().get(0).getName());
        holder.dateOrderTextView.setText("Date: " + currentOrder.getDateOrder());
        holder.priceTextView.setText("Price: " + currentOrder.getTotalPrice());
        holder.paymentTextView.setText("Payment: " + currentOrder.getPayment());
        holder.statusTextView.setText("Status: " + currentOrder.getStatus());
        if (currentOrder.getStatus().equals("delivering")) {
            holder.rateButton.setText("Delivered");
            holder.rateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    updateOrderStatusToCompleted(currentOrder);
                }
            });
        } else if (currentOrder.getStatus().equals("completed") && !currentOrder.getStatusReview().equals("reviewed")) {
            holder.rateButton.setText("Rate Product");
            holder.rateButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // Start the RatingActivity
                    Intent intent = new Intent(context, RatingActivity.class);
                    intent.putExtra("ordersData", currentOrder);
                    context.startActivity(intent);
                }
            });
        } else if (currentOrder.getStatusReview().equals("reviewed")) {
            holder.rateButton.setEnabled(false);
        }

        if (currentOrder.getStatus().equals("delivering")) {
            holder.cancelButton.setVisibility(View.VISIBLE);
            holder.cancelButton.setOnClickListener(v -> cancelOrder(currentOrder));
        } else {
            holder.cancelButton.setVisibility(View.GONE);
        }
        updateButtonStateAndClickListener(holder, currentOrder);
    }

    private void cancelOrder(Orders order) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String currentTime = dateFormat.format(new Date());

        db.collection("orders").document(order.getOrderId()).update("status", "cancelled")
                .addOnSuccessListener(aVoid -> {
                    order.setStatus("cancelled");
                    Toast.makeText(context, "Your orders is cancelled", Toast.LENGTH_SHORT).show();
                    notifyDataSetChanged();
                });
        db.collection("orders").document(order.getOrderId()).update("dateCancelOrder", currentTime)
                .addOnSuccessListener(aVoid -> {
                    order.setDateCancelOrder(currentTime);
                    notifyDataSetChanged();
                });
    }

    private void updateButtonStateAndClickListener(OrdersViewHolder holder, Orders currentOrder) {
        // Hide all buttons initially
        holder.rateButton.setVisibility(View.GONE);
        holder.cancelButton.setVisibility(View.GONE);

        switch (currentOrder.getStatus()) {
            case "pending":
                holder.cancelButton.setVisibility(View.VISIBLE);
                holder.cancelButton.setText("Cancel Order");
                holder.cancelButton.setOnClickListener(v -> cancelOrder(currentOrder));

                break;
            case "delivering":
                holder.rateButton.setVisibility(View.VISIBLE);
                holder.rateButton.setText("Delivered");
                holder.rateButton.setOnClickListener(v -> updateOrderStatusToCompleted(currentOrder));
                break;
            case "completed":
                if (!currentOrder.getStatusReview().equals("reviewed")) {
                    holder.rateButton.setVisibility(View.VISIBLE);
                    holder.rateButton.setText("Rate Product");
                    holder.rateButton.setOnClickListener(v -> {
                        Intent intent = new Intent(context, RatingActivity.class);
                        intent.putExtra("ordersData", currentOrder);
                        context.startActivity(intent);
                    });
                }
                break;
            case "cancelled":
                // No buttons should be visible if the order is cancelled
                break;
        }
    }



    private void updateOrderStatusToCompleted(Orders order) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        String currentTime = dateFormat.format(new Date());
        db.collection("orders").document(order.getOrderId()).update("status", "completed")
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        order.setStatus("completed");
                        notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
        db.collection("orders").document(order.getOrderId()).update("dateCompletedOrder", currentTime)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        order.setDateCompletedOrder(currentTime);
                        notifyDataSetChanged();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }



    @Override
    public int getItemCount() {
        return ordersList.size();
    }

    public static class OrdersViewHolder extends RecyclerView.ViewHolder {
        TextView paymentTextView;
        TextView statusTextView;
        ImageView productImageView;
        TextView productNameTextView;
        TextView priceTextView;
        TextView dateOrderTextView;
        Button rateButton;
        Button cancelButton;
        public OrdersViewHolder(@NonNull View itemView) {
            super(itemView);
            paymentTextView = itemView.findViewById(R.id.paymentTextView);
            statusTextView = itemView.findViewById(R.id.statusTextView);
            productImageView = itemView.findViewById(R.id.productImageView);
            productNameTextView = itemView.findViewById(R.id.productNameTextView);
            priceTextView = itemView.findViewById(R.id.priceTextView);
            dateOrderTextView = itemView.findViewById(R.id.dateOrderTextView);
            rateButton = itemView.findViewById(R.id.rateButton);
            cancelButton = itemView.findViewById(R.id.cancelButton);
        }
    }


}

