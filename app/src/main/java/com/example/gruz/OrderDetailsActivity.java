package com.example.gruz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;

public class OrderDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        // Находим элементы
        ImageView btnBack = findViewById(R.id.btnBack);
        TextView tvTitle = findViewById(R.id.tvOrderTitle);
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        ScrollView layoutDetails = findViewById(R.id.layoutDetails);
        ScrollView layoutHistory = findViewById(R.id.layoutHistory);
        MaterialButton btnRepeat = findViewById(R.id.btnRepeat);

        // Получаем ID заказа из Intent
        String orderId = getIntent().getStringExtra("order_id");
        if (orderId != null) {
            tvTitle.setText("Заказ #" + orderId);
        }

        // Назад
        btnBack.setOnClickListener(v -> finish());

        // Логика переключения вкладок
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                if (tab.getPosition() == 0) {
                    layoutDetails.setVisibility(View.VISIBLE);
                    layoutHistory.setVisibility(View.GONE);
                } else {
                    layoutDetails.setVisibility(View.GONE);
                    layoutHistory.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onTabUnselected(TabLayout.Tab tab) {}
            @Override
            public void onTabReselected(TabLayout.Tab tab) {}
        });

        // Кнопка "Повторить заказ"
        btnRepeat.setOnClickListener(v -> {
            // Открываем экран создания заказа
            Intent intent = new Intent(OrderDetailsActivity.this, CreateOrderActivity.class);
            startActivity(intent);
        });
    }
}
