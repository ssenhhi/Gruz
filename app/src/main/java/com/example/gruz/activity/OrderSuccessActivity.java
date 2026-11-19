package com.example.gruz.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gruz.R;
import com.google.android.material.button.MaterialButton;

public class OrderSuccessActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_success);

        TextView tvOrderNumber = findViewById(R.id.tvOrderNumber);
        String orderNumber = getIntent().getStringExtra("order_number");
        if (orderNumber != null) {
            tvOrderNumber.setText("Номер заказа: " + orderNumber);
        }
        MaterialButton btnGoToOrder = findViewById(R.id.btnGoToOrder);
        btnGoToOrder.setOnClickListener(v -> {
            Intent intent = new Intent(OrderSuccessActivity.this, OrdersActivity.class);
            startActivity(intent);
            finish();
        });
        MaterialButton btnGoHome = findViewById(R.id.btnGoHome);
        btnGoHome.setOnClickListener(v -> {
            Intent intent = new Intent(OrderSuccessActivity.this, HomeActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
        });
    }
}
