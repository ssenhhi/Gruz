package com.example.gruz.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gruz.models.CargoListResponse;
import com.example.gruz.models.CargoResponse;
import com.example.gruz.R;
import com.example.gruz.models.PaymentListResponse;
import com.example.gruz.models.PaymentResponse;
import com.example.gruz.models.StatusHistoryListResponse;
import com.example.gruz.models.StatusHistoryResponse;
import com.example.gruz.network.ApiService;
import com.example.gruz.network.RetrofitClient;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.tabs.TabLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrderDetailsActivity extends AppCompatActivity {
    private LinearLayout layoutHistoryList;
    private String orderId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        ImageView btnBack = findViewById(R.id.btnBack);
        TextView tvTitle = findViewById(R.id.tvOrderTitle);
        TabLayout tabLayout = findViewById(R.id.tabLayout);
        ScrollView layoutDetails = findViewById(R.id.layoutDetails);
        ScrollView layoutHistory = findViewById(R.id.layoutHistory);
        MaterialButton btnRepeat = findViewById(R.id.btnRepeat);
        orderId = getIntent().getStringExtra("order_id");
        if (orderId == null) {
            Toast.makeText(this, "Ошибка: ID заказа не передан", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        loadPaymentDetails();
        tvTitle.setText("Заказ #" + orderId);
        btnBack.setOnClickListener(v -> finish());
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

        btnRepeat.setOnClickListener(v -> {
            Intent intent = new Intent(OrderDetailsActivity.this, CreateOrderActivity.class);
            startActivity(intent);
        });
        loadCargoDetails();
        loadStatusHistory();
    }
    private void loadPaymentDetails() {
        ApiService api = RetrofitClient.getApiService(this);
        String filter = "(order='" + orderId + "')";

        api.getPaymentByOrder(filter).enqueue(new Callback<PaymentListResponse>() {
            @Override
            public void onResponse(Call<PaymentListResponse> call, Response<PaymentListResponse> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().getItems().isEmpty()) {
                    PaymentResponse payment = response.body().getItems().get(0);
                    TextView tvPayment = findViewById(R.id.tvDetailPayment);
                    tvPayment.setText(payment.getPreliminaryCost() + " ₽ • " + payment.getMethod());
                }
            }
            @Override
            public void onFailure(Call<PaymentListResponse> call, Throwable t) {}
        });
    }
    private void loadCargoDetails() {
        ApiService api = RetrofitClient.getApiService(this);
        String filter = "(order='" + orderId + "')";
        api.getCargoByOrder(filter).enqueue(new Callback<CargoListResponse>() {
            @Override
            public void onResponse(Call<CargoListResponse> call, Response<CargoListResponse> response) {
                if (response.isSuccessful() && response.body() != null && !response.body().getItems().isEmpty()) {
                    CargoResponse cargo = response.body().getItems().get(0);
                    String cargoText = String.format("%.1f кг • %.1f м³ • %d×%d×%d см",
                            cargo.getWeight(), cargo.getVolume(), cargo.getLength(), cargo.getWidth(), cargo.getHeight());
                    TextView tvCargo = findViewById(R.id.tvDetailCargo);
                    tvCargo.setText(cargoText);
                }
            }
            @Override
            public void onFailure(Call<CargoListResponse> call, Throwable t) {}
        });
    }
    private void loadStatusHistory() {
        ApiService api = RetrofitClient.getApiService(this);
        String filter = "(order='" + orderId + "')";
        api.getHistoryByOrder(filter).enqueue(new Callback<StatusHistoryListResponse>() {
            @Override
            public void onResponse(Call<StatusHistoryListResponse> call, Response<StatusHistoryListResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    LinearLayout container = findViewById(R.id.historyContainer);
                    if (container != null) {
                        container.removeAllViews();
                        for (StatusHistoryResponse item : response.body().getItems()) {
                            TextView tv = new TextView(OrderDetailsActivity.this);
                            tv.setText(item.getStatus() + " - " + item.getCreated().substring(0, 10));
                            tv.setPadding(0, 8, 0, 8);
                            container.addView(tv);
                        }
                    }
                }
            }
            @Override
            public void onFailure(Call<StatusHistoryListResponse> call, Throwable t) {}
        });
    }
}
