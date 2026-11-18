package com.example.gruz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

public class CreateOrderActivity extends AppCompatActivity {

    private LinearLayout layoutStep1, layoutStep2, layoutStep3;
    private TextView step1Circle, step2Circle, step3Circle, tvStepTitle, tvCalculatedPrice;
    private EditText etWeight, etVolume;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_order);

        // Инициализация элементов
        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        layoutStep1 = findViewById(R.id.layoutStep1);
        layoutStep2 = findViewById(R.id.layoutStep2);
        layoutStep3 = findViewById(R.id.layoutStep3);

        step1Circle = findViewById(R.id.step1Circle);
        step2Circle = findViewById(R.id.step2Circle);
        step3Circle = findViewById(R.id.step3Circle);
        tvStepTitle = findViewById(R.id.tvStepTitle);

        etWeight = findViewById(R.id.etWeight);
        etVolume = findViewById(R.id.etVolume);
        tvCalculatedPrice = findViewById(R.id.tvCalculatedPrice);

        MaterialButton btnNext1 = findViewById(R.id.btnNext1);
        MaterialButton btnNext2 = findViewById(R.id.btnNext2);
        MaterialButton btnSubmit = findViewById(R.id.btnSubmit);

        // --- ЛОГИКА ПЕРЕХОДА НА ШАГ 2 ---
        btnNext1.setOnClickListener(v -> {
            layoutStep1.setVisibility(View.GONE);
            layoutStep2.setVisibility(View.VISIBLE);
            // Меняем кружочки
            step1Circle.setBackgroundResource(R.drawable.bg_step_inactive);
            step1Circle.setTextColor(0xFF757575); // Серый текст
            step2Circle.setBackgroundResource(R.drawable.bg_step_active);
            step2Circle.setTextColor(0xFFFFFFFF); // Белый текст
            tvStepTitle.setText("Шаг 2 из 3 — Груз");
        });

        // --- ЛОГИКА ПЕРЕХОДА НА ШАГ 3 ---
        btnNext2.setOnClickListener(v -> {
            // Сначала считаем цену
            String weightStr = etWeight.getText().toString();
            String volumeStr = etVolume.getText().toString();
            if (weightStr.isEmpty() || volumeStr.isEmpty()) {
                Toast.makeText(this, "Введите вес и объем!", Toast.LENGTH_SHORT).show();
                return;
            }

            try {
                double weight = Double.parseDouble(weightStr);
                double volume = Double.parseDouble(volumeStr);
                int price = (int) ((weight * 100) + (volume * 500) + 500); // Формула
                tvCalculatedPrice.setText(price + " ₽");
            } catch (Exception e) {
                Toast.makeText(this, "Ошибка ввода", Toast.LENGTH_SHORT).show();
                return;
            }

            // Меняем экран
            layoutStep2.setVisibility(View.GONE);
            layoutStep3.setVisibility(View.VISIBLE);
            // Меняем кружочки
            step2Circle.setBackgroundResource(R.drawable.bg_step_inactive);
            step2Circle.setTextColor(0xFF757575);
            step3Circle.setBackgroundResource(R.drawable.bg_step_active);
            step3Circle.setTextColor(0xFFFFFFFF);
            tvStepTitle.setText("Шаг 3 из 3 — Оплата");
        });

        // --- ПОДТВЕРЖДЕНИЕ ЗАКАЗА ---
        btnSubmit.setOnClickListener(v -> {
            // В реальности тут мы бы отправили заказ на сервер и получили бы его ID
            // Пока просто переходим на экран успеха
            Intent intent = new Intent(CreateOrderActivity.this, OrderSuccessActivity.class);
            startActivity(intent);
            finish(); // Закрываем экран создания заказа
        });
    }
}
