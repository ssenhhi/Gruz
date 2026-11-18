package com.example.gruz;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class OrdersActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private OrderAdapter adapter;
    private List<Order> orderList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_orders);

        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Инициализируем адаптер с пустым списком
        adapter = new OrderAdapter(orderList, order -> {
            Intent intent = new Intent(OrdersActivity.this, OrderDetailsActivity.class);
            intent.putExtra("order_id", order.getId());
            startActivity(intent);
        });
        recyclerView.setAdapter(adapter);

        // Загружаем данные с сервера
        loadOrders();
    }

    private void loadOrders() {
        ApiService api = RetrofitClient.getApiService(this);
        TokenStorage storage = new TokenStorage(this);
        String token = storage.getAccessToken();
        android.util.Log.d("TOKEN_CHECK", "Токен: " + (token != null ? "Есть" : "НЕТ"));
        api.getAllOrders().enqueue(new Callback<OrdersListResponse>() {
            @Override
            public void onResponse(Call<OrdersListResponse> call, Response<OrdersListResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<OrderResponse> items = response.body().getItems();
                    orderList.clear();

                    for (OrderResponse item : items) {
                        // Преобразуем ответ сервера в нашу модель для списка
                        orderList.add(new Order(
                                item.getId(),
                                item.getNumber(),
                                item.getStatus(),
                                item.getComment() != null ? item.getComment() : "Без комментария"
                        ));
                    }
                    adapter.notifyDataSetChanged(); // Обновляем список на экране
                } else {
                    Toast.makeText(OrdersActivity.this, "Ошибка загрузки заказов", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<OrdersListResponse> call, Throwable t) {
                Toast.makeText(OrdersActivity.this, "Ошибка сети: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
