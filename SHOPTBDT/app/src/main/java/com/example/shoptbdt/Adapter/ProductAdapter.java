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

public class ProductAdapter extends RecyclerView.Adapter<ProductAdapter.ViewHolder>{
    private List<Products> listProducts;
    private OnProductClickListener productClickListener;
    public interface OnProductClickListener {
        void onProductClick(Products product);
    }
    public ProductAdapter(List<Products> listProducts, OnProductClickListener productClickListener) {
        this.listProducts = listProducts;
        this.productClickListener = productClickListener;
    }


    @NonNull
    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View productView = inflater.inflate(R.layout.item_product, parent, false);
        ViewHolder viewHolder = new ProductAdapter.ViewHolder(productView,productClickListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProductAdapter.ViewHolder holder, int position) {
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
        public ViewHolder(@NonNull View itemView,OnProductClickListener productClickListener) {
            super(itemView);

            imageViewProduct = (ImageView) itemView.findViewById(R.id.imageViewProduct);
            textViewNameProduct = (TextView) itemView.findViewById(R.id.textViewNameProduct);
            textViewPrice = (TextView) itemView.findViewById(R.id.textViewPrice);
            buttonView = itemView.findViewById(R.id.btnViewProductDetail);

            this.productClickListener = productClickListener;
        }
    }
}