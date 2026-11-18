package com.example.gruz;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    private TextView tvFio, tvLogin, tvEmail, tvPhone;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ImageView btnBack = findViewById(R.id.btnBack);
        tvFio = findViewById(R.id.tvProfileFio);
        tvLogin = findViewById(R.id.tvProfileLogin);
        tvEmail = findViewById(R.id.tvProfileEmail);
        tvPhone = findViewById(R.id.tvProfilePhone);
        btnBack.setOnClickListener(v -> finish());
        loadProfileData();
        findViewById(R.id.btnEditProfile).setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
            intent.putExtra("fio", tvFio.getText().toString());
            intent.putExtra("email", tvEmail.getText().toString());
            intent.putExtra("phone", tvPhone.getText().toString());
            intent.putExtra("user_id", currentUserId); // Передаем найденный ID
            startActivity(intent);
        });
    }
    @Override
    protected void onResume() {
        super.onResume();
        loadProfileData();
    }
    private void loadProfileData() {
        ApiService api = RetrofitClient.getApiService(this);

        // Запрашиваем ВСЕХ пользователей (без фильтра)
        api.getAllUsers().enqueue(new Callback<UsersListResponse>() {
            @Override
            public void onResponse(Call<UsersListResponse> call, Response<UsersListResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    // Проверяем, есть ли вообще пользователи в ответе
                    if (response.body().getItems() != null && !response.body().getItems().isEmpty()) {

                        // Берем первого пользователя (у нас их пока мало)
                        UserResponse user = response.body().getItems().get(0);

                        currentUserId = user.getId();

                        // Выводим данные на экран
                        tvFio.setText(user.getFullName());
                        tvLogin.setText(user.getLogin());
                        tvEmail.setText(user.getEmail());
                        tvPhone.setText(user.getPhone());

                    } else {
                        Toast.makeText(ProfileActivity.this, "В базе нет пользователей", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(ProfileActivity.this, "Ошибка сервера: " + response.code(), Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<UsersListResponse> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, "Ошибка сети: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
