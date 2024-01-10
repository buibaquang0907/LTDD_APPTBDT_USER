package com.example.shoptbdt.Adapter;

import static androidx.core.content.ContextCompat.startActivity;

import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shoptbdt.Models.Categories;
import com.example.shoptbdt.Models.Products;
import com.example.shoptbdt.R;
import com.example.shoptbdt.Screen.ProductsDetailActivity;
import com.example.shoptbdt.Screen.YourOrdersActivity;

import java.util.List;

public class CategoriesAdapter extends RecyclerView.Adapter<CategoriesAdapter.CategoriesViewHolder> {
    private List<Categories> categoriesList;
    private OnCategoryClickListener categoryClickListener;

    public CategoriesAdapter(List<Categories> categoriesList, OnCategoryClickListener categoryClickListener) {
        this.categoriesList = categoriesList;
        this.categoryClickListener = categoryClickListener;
    }

    public interface OnCategoryClickListener {
        void onCategoryClick(Categories category);
    }

    @NonNull
    @Override
    public CategoriesViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_category, parent, false);
        return new CategoriesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CategoriesViewHolder holder, int position) {
        Categories category = categoriesList.get(position);
        Glide.with(holder.itemView.getContext())
                .load(category.getImage())
                .into(holder.categoryImageView);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Handle category item click
                categoryClickListener.onCategoryClick(category);
            }
        });
    }

    @Override
    public int getItemCount() {
        return categoriesList.size();
    }

    public static class CategoriesViewHolder extends RecyclerView.ViewHolder {
        ImageView categoryImageView;

        public CategoriesViewHolder(@NonNull View itemView) {
            super(itemView);
            categoryImageView = itemView.findViewById(R.id.categoryImageView);
        }
    }
}

