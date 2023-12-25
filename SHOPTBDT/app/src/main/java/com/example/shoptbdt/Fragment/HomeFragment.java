package com.example.shoptbdt.Fragment;

import static android.content.ContentValues.TAG;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.shoptbdt.Adapter.CategoriesAdapter;
import com.example.shoptbdt.Adapter.ProductAdapter;
import com.example.shoptbdt.Models.Categories;
import com.example.shoptbdt.Models.Products;
import com.example.shoptbdt.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;


public class HomeFragment extends Fragment {

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

    private EditText editTextSearch;
    private RecyclerView recyclerViewCategories;
    private CategoriesAdapter categoriesAdapter;
    private List<Categories> categoriesList;

    private RecyclerView recyclerViewProducts;
    private List<Products> productList;
    private ProductAdapter productAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        editTextSearch = view.findViewById(R.id.editTextSearch);

        //Categories
        recyclerViewCategories = view.findViewById(R.id.recyclerViewCategories);
        recyclerViewCategories.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));

        categoriesList = new ArrayList<>();
        categoriesAdapter = new CategoriesAdapter(categoriesList);
        recyclerViewCategories.setAdapter(categoriesAdapter);

        getCategoriesFromFireStore();

        // Products
        recyclerViewProducts = view.findViewById(R.id.recyclerViewProducts);
        recyclerViewProducts.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        productList = new ArrayList<>();
        productAdapter = new ProductAdapter(productList);
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
}
