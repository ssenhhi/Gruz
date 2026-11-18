package com.example.gruz;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class OrderSuccessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_success);

        // Кнопка "Перейти к заказу" (открывает историю заказов)
        MaterialButton btnGoToOrder = findViewById(R.id.btnGoToOrder);
        btnGoToOrder.setOnClickListener(v -> {
            Intent intent = new Intent(OrderSuccessActivity.this, OrdersActivity.class);
            startActivity(intent);
            finish(); // Закрываем экран успеха, чтобы не висел в стеке
        });

        // Кнопка "На главную" (возвращает на HomeActivity)
        MaterialButton btnGoHome = findViewById(R.id.btnGoHome);
        btnGoHome.setOnClickListener(v -> {
            Intent intent = new Intent(OrderSuccessActivity.this, HomeActivity.class);
            // Очищаем стек, чтобы по кнопке "Назад" не вернуться на экран создания заказа
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}
