package com.example.shoptbdt.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
    private OnItemClickListener listener;


    public interface OnItemClickListener {
        void onButtonClickListener(String productId);
    }


    public FavouriteAdapter(List<Favourite> favouriteList, OnItemClickListener listener) {
        this.favouriteList = favouriteList;
        this.listener = listener;
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
        holder.bind(favourite, listener);
    }

    @Override
    public int getItemCount() {
        return favouriteList.size();
    }

    public static class FavouriteViewHolder extends RecyclerView.ViewHolder {

        private ImageView imageViewProductFavourite;
        private TextView textViewProductNameFavourite;
        private TextView textViewProductPriceFavourite;
        private Button btnViewProductFavourite;
        private Favourite favourite;

        public FavouriteViewHolder(@NonNull View itemView) {
            super(itemView);
            imageViewProductFavourite = itemView.findViewById(R.id.imageViewProductFavourite);
            textViewProductNameFavourite = itemView.findViewById(R.id.textViewProductNameFavourite);
            textViewProductPriceFavourite = itemView.findViewById(R.id.textViewProductPriceFavourite);
            btnViewProductFavourite = itemView.findViewById(R.id.btnViewProductFavourite);
        }

        public void bind(Favourite favourite, OnItemClickListener listener) {
            Glide.with(itemView.getContext())
                    .load(favourite.getProductImage())
                    .centerCrop()
                    .into(imageViewProductFavourite);

            textViewProductNameFavourite.setText("Name: " + favourite.getProductName());
            textViewProductPriceFavourite.setText("Price: $" + String.valueOf(favourite.getProductPrice()));
            this.favourite = favourite;

            btnViewProductFavourite.setOnClickListener(v -> {
                if (listener != null) {
                    listener.onButtonClickListener(favourite.getProductId());
                }
            });
        }
    }
}

