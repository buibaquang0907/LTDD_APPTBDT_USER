package com.example.shoptbdt.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shoptbdt.Models.Orders;
import com.example.shoptbdt.R;

import java.util.List;

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

        public OrdersViewHolder(@NonNull View itemView) {
            super(itemView);
            paymentTextView = itemView.findViewById(R.id.paymentTextView);
            statusTextView = itemView.findViewById(R.id.statusTextView);
            productImageView = itemView.findViewById(R.id.productImageView);
            productNameTextView = itemView.findViewById(R.id.productNameTextView);
            priceTextView = itemView.findViewById(R.id.priceTextView);
            dateOrderTextView = itemView.findViewById(R.id.dateOrderTextView);
        }
    }


}

