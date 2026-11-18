package com.example.gruz;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;

public class HomeActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        MaterialButton btnCreateOrder = findViewById(R.id.btnCreateOrder);
        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);

        // Кнопка Создать заявку (пока заглушка)
        btnCreateOrder.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, CreateOrderActivity.class);
            startActivity(intent);
        });
        TextView btnNotifications = findViewById(R.id.btnNotifications);
        btnNotifications.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, NotificationsActivity.class);
            startActivity(intent);
        });

        // Обработка нажатий на нижнее меню
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                return true;
            } else if (id == R.id.nav_orders) {
                Intent intent = new Intent(HomeActivity.this, OrdersActivity.class);
                startActivity(intent);
                return false; // Не выделяем иконку в меню, так как уходим на другой экран
            } else if (id == R.id.nav_templates) {
                Intent intent = new Intent(HomeActivity.this, TemplatesActivity.class);
                startActivity(intent);
                return false; // Возвращаем false, так как уходим на другой экра
            } else if (id == R.id.nav_profile) {
            // Открываем экран профиля
            Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
            startActivity(intent);
            return false; // Возвращаем false, чтобы не выделять иконку "Профиль" зеленым, так как мы уходим с экрана
        }
            return false;
        });
    }
}
