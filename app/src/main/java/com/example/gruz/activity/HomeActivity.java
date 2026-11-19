package com.example.gruz.activity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gruz.models.CitiesListResponse;
import com.example.gruz.models.CityResponse;
import com.example.gruz.R;
import com.example.gruz.adapters.CityAdapter;
import com.example.gruz.network.ApiService;
import com.example.gruz.network.RetrofitClient;
import com.example.gruz.utils.TokenStorage;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private CityAdapter adapter;
    private List<CityResponse> cityList = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        recyclerView = findViewById(R.id.recyclerViewCities);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new CityAdapter(cityList);
        recyclerView.setAdapter(adapter);
        loadCities();
        TextView tvHello = findViewById(R.id.tvHello);
        TokenStorage storage = new TokenStorage(this);
        String userName = storage.getUserName();
        if (userName != null && userName.contains(" ")) {
            String[] parts = userName.split(" ");
            if (parts.length >= 2) {
                tvHello.setText("Привет, " + parts[1] + "!");
            } else {
                tvHello.setText("Привет, " + userName + "!");
            }
        } else {
            tvHello.setText("Привет, " + userName + "!");
        }
        MaterialButton btnCreateOrder = findViewById(R.id.btnCreateOrder);
        BottomNavigationView bottomNav = findViewById(R.id.bottomNav);
        btnCreateOrder.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, CreateOrderActivity.class);
            startActivity(intent);
        });
        TextView btnNotifications = findViewById(R.id.btnNotifications);
        btnNotifications.setOnClickListener(v -> {
            Intent intent = new Intent(HomeActivity.this, NotificationsActivity.class);
            startActivity(intent);
        });
        bottomNav.setOnItemSelectedListener(item -> {
            int id = item.getItemId();
            if (id == R.id.nav_home) {
                return true;
            } else if (id == R.id.nav_orders) {
                Intent intent = new Intent(HomeActivity.this, OrdersActivity.class);
                startActivity(intent);
                return false;
            } else if (id == R.id.nav_templates) {
                Intent intent = new Intent(HomeActivity.this, TemplatesActivity.class);
                startActivity(intent);
                return false;
            } else if (id == R.id.nav_profile) {
            Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
            startActivity(intent);
            return false;
        }
            return false;
        });
    }
    private void loadCities() {
        ApiService api = RetrofitClient.getApiService(this);
        api.getCities().enqueue(new Callback<CitiesListResponse>() {
            @Override
            public void onResponse(Call<CitiesListResponse> call, Response<CitiesListResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    cityList.clear();
                    cityList.addAll(response.body().getItems());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(HomeActivity.this, "Ошибка загрузки городов", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<CitiesListResponse> call, Throwable t) {
                Toast.makeText(HomeActivity.this, "Ошибка сети", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
