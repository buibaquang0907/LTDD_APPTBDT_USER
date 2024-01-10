package com.example.shoptbdt.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shoptbdt.Models.Products;
import com.example.shoptbdt.R;

import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder>{
    private List<Products> listProducts;
    private OnProductClickListener productClickListener;
    public interface OnProductClickListener {
        void onProductClick(Products product);
    }

    public CartAdapter(List<Products> listProducts, OnProductClickListener productClickListener) {
        this.listProducts = listProducts;
        this.productClickListener = productClickListener;
    }


    @NonNull
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View cartView = inflater.inflate(R.layout.cart_item, parent, false);
        ViewHolder viewHolder = new CartAdapter.ViewHolder(cartView,productClickListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CartAdapter.ViewHolder holder, int position) {
        Products  temp = listProducts.get(position);
        holder.textViewNameProduct.setText(temp.getName());
        holder.textViewPrice.setText("$" + String.valueOf(temp.getPrice()));
        Glide.with(holder.itemView.getContext())
                .load(temp.getImage())
                .into(holder.imageViewProduct);

        holder.buttonView.setOnClickListener(view -> holder.productClickListener.onProductClick(temp));

    }

    @Override
    public int getItemCount() {
        return listProducts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageViewProduct;
        TextView textViewNameProduct, textViewPrice;
        Button buttonView;
        OnProductClickListener productClickListener;
        public ViewHolder(@NonNull View itemView, OnProductClickListener productClickListener) {
            super(itemView);

            imageViewProduct = (ImageView) itemView.findViewById(R.id.imageViewProductCart);
            textViewNameProduct = (TextView) itemView.findViewById(R.id.textViewNameProductCart);
            textViewPrice = (TextView) itemView.findViewById(R.id.textViewPriceCart);
            buttonView = itemView.findViewById(R.id.btnRemove);
            this.productClickListener = productClickListener;
        }
    }


}
