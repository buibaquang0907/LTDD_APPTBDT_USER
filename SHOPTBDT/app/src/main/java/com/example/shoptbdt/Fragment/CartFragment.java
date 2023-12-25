package com.example.shoptbdt.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.shoptbdt.R;
import com.example.shoptbdt.Screen.CheckOut.CheckOutFromCartActivity;

public class CartFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public CartFragment() {
    }

    @NonNull
    public static CartFragment newInstance(String param1, String param2) {
        CartFragment fragment = new CartFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    TextView textViewEmpty, txtTotal;
    Button btnContinue;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cart, container, false);
        RecyclerView recyclerViewCart = view.findViewById(R.id.recyclerViewCart);
        textViewEmpty = view.findViewById(R.id.textViewEmpty);
        txtTotal = view.findViewById(R.id.txtTotal);
        btnContinue = view.findViewById(R.id.btnContinue);

//        if (recyclerViewCart.getAdapter() != null && recyclerViewCart.getAdapter().getItemCount() > 0) {
//            recyclerViewCart.setVisibility(View.VISIBLE);
//            textViewEmpty.setVisibility(View.GONE);
//            //
//
//        } else {
//            recyclerViewCart.setVisibility(View.GONE);
//            textViewEmpty.setVisibility(View.VISIBLE);
//        }

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), CheckOutFromCartActivity.class);
                startActivity(intent);
            }
        });
        return view;
    }

}