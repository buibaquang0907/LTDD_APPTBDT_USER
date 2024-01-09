package com.example.shoptbdt.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.shoptbdt.Models.Favourite;
import com.example.shoptbdt.R;

import java.util.List;
public class FavouriteAdapter extends RecyclerView.Adapter<FavouriteAdapter.FavouriteViewHolder> {

    private List<Favourite> favouriteList;

    public FavouriteAdapter(List<Favourite> favouriteList) {
        this.favouriteList = favouriteList;
    }

    @NonNull
    @Override
    public FavouriteViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favourite, parent, false);
        return new FavouriteViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FavouriteViewHolder holder, int position) {
        Favourite favourite = favouriteList.get(position);
        holder.bind(favourite);
    }

    @Override
    public int getItemCount() {
        return favouriteList.size();
    }

    public static class FavouriteViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageViewProductFavourite;
        private TextView textViewProductNameFavourite;
        private TextView textViewProductPriceFavourite;
        public FavouriteViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewProductFavourite = itemView.findViewById(R.id.imageViewProductFavourite);
            textViewProductNameFavourite = itemView.findViewById(R.id.textViewProductNameFavourite);
            textViewProductPriceFavourite = itemView.findViewById(R.id.textViewProductPriceFavourite);
        }
        public void bind(Favourite favourite) {
            Glide.with(itemView.getContext())
                    .load(favourite.getProductImage())
                    .centerCrop()
                    .into(imageViewProductFavourite);

            textViewProductNameFavourite.setText("Price Name: "+favourite.getProductName());
            textViewProductPriceFavourite.setText("Price Price: "+String.valueOf(favourite.getProductPrice()));

        }
    }
}

