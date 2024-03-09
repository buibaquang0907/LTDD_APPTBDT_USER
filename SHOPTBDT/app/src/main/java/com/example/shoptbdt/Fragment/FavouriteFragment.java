package com.example.shoptbdt.Fragment;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shoptbdt.Adapter.FavouriteAdapter;
import com.example.shoptbdt.Models.Favourite;
import com.example.shoptbdt.Models.Products;
import com.example.shoptbdt.R;
import com.example.shoptbdt.Screen.ProductsDetailActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class FavouriteFragment extends Fragment implements FavouriteAdapter.OnItemClickListener {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public FavouriteFragment() {
    }

    @NonNull
    public static FavouriteFragment newInstance(String param1, String param2) {
        FavouriteFragment fragment = new FavouriteFragment();
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

    private RecyclerView recyclerViewFavourite;
    private FavouriteAdapter favouriteAdapter;
    private List<Favourite> favouriteList;
    private List<Products> productList;

    private FirebaseAuth auth = FirebaseAuth.getInstance();
    private FirebaseUser currentUser = auth.getCurrentUser();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_favourite, container, false);

        recyclerViewFavourite = view.findViewById(R.id.recyclerViewFavourite);
        recyclerViewFavourite.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));

        favouriteList = new ArrayList<>();
        favouriteAdapter = new FavouriteAdapter(favouriteList, this);
        recyclerViewFavourite.setAdapter(favouriteAdapter);
        fetchFavouriteData();
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new SwipeToDeleteCallback());
        itemTouchHelper.attachToRecyclerView(recyclerViewFavourite);
        return view;
    }

    private void fetchFavouriteData() {
        if (currentUser != null) {
            String userId = currentUser.getUid();

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("favourite")
                    .whereEqualTo("userId", userId)
                    .get()
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful()) {
                            favouriteList.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Favourite favourite = document.toObject(Favourite.class);
                                favouriteList.add(favourite);

                            }
                            favouriteAdapter.notifyDataSetChanged();
                        } else {
                            Log.e("FavouriteFragment", "Error fetching favourite data", task.getException());
                        }
                    });
        }
    }

    @Override
    public void onButtonClickListener(String productId) {
        Log.d("", "Product ID: " + productId);
        FirestoreQueryMethodProduct(productId);

    }

    private void FirestoreQueryMethodProduct(String productId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        productList = new ArrayList<>();
        db.collectionGroup("products")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                Map<String, Object> data = document.getData();
                                Log.d(TAG, "Data for document " + document.getId() + ": " + data);
                                try {
                                    Products product = convertMapToProducts(data);
                                    productList.add(product);

                                } catch (Exception e) {
                                    Log.e(TAG, "Error converting data to Products: " + e.getMessage());
                                }
                            }
                            for (Products product : productList) {
                                if (product.getId().equals(productId)) {
                                    Intent intent = new Intent(getContext(), ProductsDetailActivity.class);
                                    intent.putExtra("product", product);
                                    getContext().startActivity(intent);
                                }
                            }
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


    private class SwipeToDeleteCallback extends ItemTouchHelper.SimpleCallback {
        SwipeToDeleteCallback() {
            super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        }

        @Override
        public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            Favourite deletedFavourite = favouriteList.get(position);
            favouriteList.remove(position);
            favouriteAdapter.notifyItemRemoved(position);
            deleteFavouriteFromFirestore(deletedFavourite);
        }
    }

    private void deleteFavouriteFromFirestore(Favourite favourite) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        db.collection("favourite")
                .whereEqualTo("userId", favourite.getUserId())
                .whereEqualTo("productId", favourite.getProductId())
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            db.collection("favourite").document(document.getId()).delete()
                                    .addOnSuccessListener(aVoid -> Log.d("FavouriteFragment", "DocumentSnapshot successfully deleted"))
                                    .addOnFailureListener(e -> Log.e("FavouriteFragment", "Error deleting document", e));
                        }
                    } else {
                        Log.e("FavouriteFragment", "Error getting documents.", task.getException());
                    }
                });
    }
}