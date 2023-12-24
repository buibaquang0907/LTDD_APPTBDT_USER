package com.example.shoptbdt.Fragment;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.shoptbdt.Auth.LoginActivity;
import com.example.shoptbdt.R;
import com.example.shoptbdt.Screen.AboutUsActivity;
import com.example.shoptbdt.Screen.ChangePasswordActivity;
import com.example.shoptbdt.Screen.EditProfileActivity;
import com.example.shoptbdt.Screen.FavouritesActivity;
import com.example.shoptbdt.Screen.SupportActivity;
import com.example.shoptbdt.Screen.YourOrdersActivity;
import com.example.shoptbdt.Screen.YourReviewsActivity;
import com.google.firebase.auth.FirebaseAuth;

public class ProfileFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public ProfileFragment() {
    }

    @NonNull
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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

    Button btnEditProfile, btnSupport, btnYourOrders, btnFavourites, btnYourReviews, btnAboutUs, btnChangePassword, btnLogOut;
    ImageView imgImageUser;
    TextView txtNameUser, txtEmailUser;

    @Override

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        imgImageUser = view.findViewById(R.id.imgImageUser);
        txtEmailUser = view.findViewById(R.id.txtEmailUser);
        txtNameUser = view.findViewById(R.id.txtNameUser);

        btnEditProfile = view.findViewById(R.id.btnEditProfile);
        btnSupport = view.findViewById(R.id.btnSupport);
        btnYourOrders = view.findViewById(R.id.btnYourOrders);
        btnFavourites = view.findViewById(R.id.btnFavourites);
        btnYourReviews = view.findViewById(R.id.btnYourReviews);
        btnAboutUs = view.findViewById(R.id.btnAboutUs);
        btnLogOut = view.findViewById(R.id.btnLogOut);
        btnChangePassword = view.findViewById(R.id.btnChangePassword);


        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), EditProfileActivity.class);
                startActivity(intent);
            }
        });

        btnSupport.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SupportActivity.class);
                startActivity(intent);
            }
        });
        btnYourOrders.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), YourOrdersActivity.class);
                startActivity(intent);
            }
        });
        btnFavourites.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), FavouritesActivity.class);
                startActivity(intent);
            }
        });
        btnYourReviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), YourReviewsActivity.class);
                startActivity(intent);
            }
        });
        btnAboutUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AboutUsActivity.class);
                startActivity(intent);
            }
        });
        btnChangePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChangePasswordActivity.class);
                startActivity(intent);
            }
        });
        btnLogOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                Toast.makeText(getActivity(), "Bạn đã đăng xuất!", Toast.LENGTH_SHORT).show();
                startActivity(intent);
                if (getActivity() != null) {
                    getActivity().finish();
                }
            }
        });
        return view;
    }
}