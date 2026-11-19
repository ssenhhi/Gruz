package com.example.gruz.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.gruz.models.NotificationResponse;
import com.example.gruz.models.NotificationsListResponse;
import com.example.gruz.R;
import com.example.gruz.adapters.NotificationAdapter;
import com.example.gruz.network.ApiService;
import com.example.gruz.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class NotificationsActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private NotificationAdapter adapter;
    private List<NotificationResponse> notifList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notifications);

        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        recyclerView = findViewById(R.id.recyclerViewNotifications);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        adapter = new NotificationAdapter(notifList);
        recyclerView.setAdapter(adapter);

        loadNotifications();
    }
    private void loadNotifications() {
        ApiService api = RetrofitClient.getApiService(this);
        api.getAllNotifications().enqueue(new Callback<NotificationsListResponse>() {
            @Override
            public void onResponse(Call<NotificationsListResponse> call, Response<NotificationsListResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    notifList.clear();
                    notifList.addAll(response.body().getItems());
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(NotificationsActivity.this, "Ошибка загрузки", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<NotificationsListResponse> call, Throwable t) {
                Toast.makeText(NotificationsActivity.this, "Ошибка сети", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
