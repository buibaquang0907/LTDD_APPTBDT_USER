package com.example.shoptbdt.Fragment;

import android.app.AlertDialog;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.shoptbdt.Models.Products;
import com.example.shoptbdt.R;

public class ProductDetailsFragment extends Fragment {
    private Products product;
    private TextView productNameTextView, productPriceTextView;
    private ImageView productImageView;

    public void setProduct(Products product) {
        this.product = product;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_details, container, false);
        productNameTextView = view.findViewById(R.id.textViewProductName);
        productPriceTextView = view.findViewById(R.id.textViewProductPrice);
        productImageView = view.findViewById(R.id.imageViewProductDetail);

        productNameTextView.setText(product.getName());
        productPriceTextView.setText("$" + product.getPrice());
        Glide.with(this).load(product.getImage()).into(productImageView);

        Button changeNameButton = view.findViewById(R.id.buttonChangeProductName);
        changeNameButton.setOnClickListener(v -> showChangeNameDialog());

        return view;
    }

    private void showChangeNameDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setTitle("Change Product Name");

        final EditText input = new EditText(getContext());
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        builder.setView(input);

        builder.setPositiveButton("Save", (dialog, which) -> {
            String newName = input.getText().toString();
            product.setName(newName);
            productNameTextView.setText(newName);
        });

        builder.setNegativeButton("Cancel", (dialog, which) -> dialog.cancel());
        builder.show();
    }
}
