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
    private List<Products> originalList;
    private OnProductClickListener productClickListener;

    private OnFavouriteClickListener favouriteClickListener;
    public interface OnProductClickListener {
        void onProductClick(Products product);
    }

    public interface OnFavouriteClickListener {
        void onFavouriteClick(Products product);
    }
    public ProductAdapter(List<Products> listProducts, OnProductClickListener productClickListener, OnFavouriteClickListener favouriteClickListener) {
        this.listProducts = listProducts;
        this.originalList = listProducts;
        this.productClickListener = productClickListener;
        this.favouriteClickListener = favouriteClickListener;
    }

    public void filterList(List<Products> filteredList) {
        listProducts = filteredList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ProductAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View productView = inflater.inflate(R.layout.item_product, parent, false);
        ViewHolder viewHolder = new ProductAdapter.ViewHolder(productView,productClickListener,favouriteClickListener);
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
        holder.buttonFavourite.setOnClickListener(view -> holder.favouriteClickListener.onFavouriteClick(temp));
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
        Button buttonFavourite;
        OnFavouriteClickListener favouriteClickListener;
        public ViewHolder(@NonNull View itemView, OnProductClickListener productClickListener, OnFavouriteClickListener favouriteClickListener) {
            super(itemView);

            imageViewProduct = (ImageView) itemView.findViewById(R.id.imageViewProduct);
            textViewNameProduct = (TextView) itemView.findViewById(R.id.textViewNameProduct);
            textViewPrice = (TextView) itemView.findViewById(R.id.textViewPrice);
            buttonView = itemView.findViewById(R.id.btnViewProductDetail);
            buttonFavourite = itemView.findViewById(R.id.btnProductFavourites);
            this.favouriteClickListener = favouriteClickListener;
            this.productClickListener = productClickListener;
        }

    }


}