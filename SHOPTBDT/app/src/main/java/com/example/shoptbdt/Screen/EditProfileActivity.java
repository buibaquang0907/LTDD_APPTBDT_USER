package com.example.shoptbdt.Screen;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.shoptbdt.Models.User;
import com.example.shoptbdt.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class EditProfileActivity extends AppCompatActivity {

    EditText emailEditText, nameEditText, phoneEditText, addressEditText;
    Button btnSaveProfileUser, btnBack;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseAuth mAuth = FirebaseAuth.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        btnSaveProfileUser = findViewById(R.id.btnSaveProfileUser);
        emailEditText = findViewById(R.id.edit_emailedit);
        nameEditText = findViewById(R.id.edit_nameedit);
        phoneEditText = findViewById(R.id.edit_phoneedit);
        addressEditText = findViewById(R.id.edit_addressedit);

        btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            DocumentReference userRef = db.collection("users").document(currentUser.getUid());
            userRef.get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        User user = document.toObject(User.class);
                        updateUI(user);
                    } else {
                        Log.d("EditProfileActivity", "User data is null");
                    }
                } else {
                    Log.d("EditProfileActivity", "No such document");
                }
            });
        } else {
            Log.d("EditProfileActivity", "Unknown... -> No information user login");
        }

        btnSaveProfileUser.setOnClickListener(view -> saveUserProfile());
    }

    private void updateUI(@NonNull User user) {
        emailEditText = findViewById(R.id.edit_emailedit);
        nameEditText = findViewById(R.id.edit_nameedit);
        phoneEditText = findViewById(R.id.edit_phoneedit);
        addressEditText = findViewById(R.id.edit_addressedit);

        emailEditText.setText(user.getEmail());
        nameEditText.setText(user.getName());
        phoneEditText.setText(user.getPhone());
        if (user.getAddress() != null) {
            addressEditText.setText(user.getAddress());
        } else {
            addressEditText.setText("N/A");
        }
    }

    private void saveUserProfile() {
        String newEmail = emailEditText.getText().toString().trim();
        String newName = nameEditText.getText().toString().trim();
        String newPhone = phoneEditText.getText().toString().trim();
        String newAddress = addressEditText.getText().toString().trim();

        FirebaseUser currentUser = mAuth.getCurrentUser();
        if (currentUser != null) {
            DocumentReference userRef = db.collection("users").document(currentUser.getUid());
            userRef.update("email", newEmail, "name", newName, "phone", newPhone, "address", newAddress)
                    .addOnSuccessListener(aVoid -> {
                        Toast.makeText(EditProfileActivity.this, "Success", Toast.LENGTH_SHORT).show();
                        Log.d("EditProfileActivity", "User update successfully");
                        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(addressEditText.getWindowToken(), 0);
                    })
                    .addOnFailureListener(e -> {
                        Log.e("EditProfileActivity", "Error updating user", e);
                    });
        }
    }
}