package com.example.shoptbdt.Fragment;

import static android.content.ContentValues.TAG;

import android.Manifest;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoptbdt.Adapter.CategoriesAdapter;
import com.example.shoptbdt.Adapter.ProductAdapter;
import com.example.shoptbdt.Models.Categories;
import com.example.shoptbdt.Models.Favourite;
import com.example.shoptbdt.Models.Products;
import com.example.shoptbdt.R;
import com.example.shoptbdt.Screen.ProductsDetailActivity;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Map;


public class HomeFragment extends Fragment implements ProductAdapter.OnProductClickListener, ProductAdapter.OnFavouriteClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public HomeFragment() {
    }

    @NonNull
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private TextView txtLocation;
    private EditText editTextSearch;
    private RecyclerView recyclerViewCategories;
    private CategoriesAdapter categoriesAdapter;
    private List<Categories> categoriesList;
    private RecyclerView recyclerViewProducts;
    private List<Products> productList;
    private ProductAdapter productAdapter;
    FirebaseAuth auth = FirebaseAuth.getInstance();
    FirebaseUser currentUser = auth.getCurrentUser();
    private static final int MY_PERMISSIONS_REQUEST_LOCATION = 100;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Yêu cầu quyền từ người dùng
            ActivityCompat.requestPermissions(
                    getActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_LOCATION
            );
        } else {
            getCurrentLocation();
        }
        txtLocation = view.findViewById(R.id.txtLocation);

        editTextSearch = view.findViewById(R.id.editTextSearch);
        editTextSearch.clearFocus();
        editTextSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                if (charSequence.length() > 0) {
                    filterProducts(charSequence.toString());
                }
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        });

        //Categories
        recyclerViewCategories = view.findViewById(R.id.recyclerViewCategories);
        recyclerViewCategories.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        categoriesList = new ArrayList<>();
        categoriesAdapter = new CategoriesAdapter(categoriesList);
        recyclerViewCategories.setAdapter(categoriesAdapter);
        getCategoriesFromFireStore();

        // Products
        recyclerViewProducts = view.findViewById(R.id.recyclerViewProducts);
        recyclerViewProducts.setLayoutManager(new GridLayoutManager(getContext(), 2));

        productList = new ArrayList<>();
        productAdapter = new ProductAdapter(productList, this, this);
        recyclerViewProducts.setAdapter(productAdapter);
        fetchProductsFromFirestore();

        return view;
    }

    private void getCategoriesFromFireStore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("categories")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Categories category = document.toObject(Categories.class);
                                categoriesList.add(category);
                            }
                            categoriesAdapter.notifyDataSetChanged();
                        } else {
                            Log.w(TAG, "Error getting categories from Firestore.", task.getException());
                        }
                    }
                });
    }

    private void fetchProductsFromFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collectionGroup("products")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            for (QueryDocumentSnapshot document : task.getResult()) {
                                // Retrieve data as a Map
                                Map<String, Object> data = document.getData();

                                Log.d(TAG, "Data for document " + document.getId() + ": " + data);

                                try {
                                    Products product = convertMapToProducts(data);
                                    productList.add(product);
                                } catch (Exception e) {
                                    Log.e(TAG, "Error converting data to Products: " + e.getMessage());
                                }
                            }

                            productAdapter.notifyDataSetChanged();
                        } else {
                            Log.w(TAG, "Error getting products from Firestore.", task.getException());
                        }
                    }
                });
    }

    private Products convertMapToProducts(Map<String, Object> data) {
        Gson gson = new Gson();
        String jsonString = gson.toJson(data);
        return gson.fromJson(jsonString, Products.class);
    }

    @Override
    public void onProductClick(Products product) {
        Intent intent = new Intent(getActivity(), ProductsDetailActivity.class);
        intent.putExtra("product", product);
        startActivity(intent);
    }

    private void filterProducts(@NonNull String searchText) {
        List<Products> filteredList = new ArrayList<>();

        if (!searchText.isEmpty()) {
            for (Products product : productList) {
                if (product.getName().toLowerCase().contains(searchText.toLowerCase())) {
                    filteredList.add(product);
                }
            }
        } else {
            filteredList.addAll(productList);
        }

        productAdapter.filterList(filteredList);
    }

    private void getCurrentLocation() {
        FusedLocationProviderClient fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity());
        if (ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getActivity(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(requireActivity(), location -> {
                    if (location != null) {
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();
                        getAddressFromLocation(latitude, longitude);
                    }
                })
                .addOnFailureListener(requireActivity(), e -> {
                    Log.e(TAG, "Error getting location", e);
                });
    }

    private void getAddressFromLocation(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(requireContext(), Locale.getDefault());
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null && addresses.size() > 0) {
                Address address = addresses.get(0);
                txtLocation.setText("Address: " + address.getAddressLine(0));
            }
        } catch (IOException e) {
            Log.e(TAG, "Error getting address from location", e);
        }
    }

    @Override
    public void onFavouriteClick(Products product) {
        String userId = currentUser.getUid();
        String productId = product.getId();
        String productName = product.getName();
        String productImage = product.getImage();
        String productPrice = String.valueOf(product.getPrice());
        Log.d("Show Favourite", "Current user ID: " + userId);
        Log.d("Show Favourite", "Product ID: " + productId);
        Log.d("Show Favourite", "Product Name: " + productName);
        Log.d("Show Favourite", "Product Price: " + productPrice);
        Favourite favourite = new Favourite(userId, productId, productName, productImage, productPrice);
        checkIfFavouriteExists(userId, productId,favourite);
    }

    private void checkIfFavouriteExists(final String userId, final String productId, final Favourite favourite) {
        String collectionPath = "favourite";

        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection(collectionPath)
                .whereEqualTo("userId", userId)
                .whereEqualTo("productId", productId)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            if (task.getResult().isEmpty()) {
                                saveFavouriteToFirestore(favourite);
                            } else {
                                Log.d("", "Product đã tồn tại");
                            }
                        } else {
                            Log.e("", "Error checking", task.getException());
                        }
                    }
                });
    }


    private void saveFavouriteToFirestore(Favourite favourite) {
        String collectionPath = "favourite";
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        db.collection(collectionPath)
                .add(favourite)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("", "Đã lưu thành công. ID của tài liệu: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.e("", "Error saving ", e);
                    }
                });
    }
}
