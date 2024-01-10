package com.example.shoptbdt.Screen;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.shoptbdt.R;

public class AboutUsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about_us);
        Button btnBack = findViewById(R.id.btnBack);

        // Đặt lắng nghe sự kiện khi nút Back được nhấn
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onBackPressed(); // Gọi phương thức onBackPressed để quay trở lại màn hình trước đó
            }
        });
    }
}