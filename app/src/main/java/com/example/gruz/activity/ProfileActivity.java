package com.example.gruz.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gruz.R;
import com.example.gruz.models.UserResponse;
import com.example.gruz.models.UsersListResponse;
import com.example.gruz.network.ApiService;
import com.example.gruz.network.RetrofitClient;
import com.example.gruz.utils.TokenStorage;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProfileActivity extends AppCompatActivity {

    private TextView tvFio, tvLogin, tvEmail, tvPhone, tvAddress;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        ImageView btnBack = findViewById(R.id.btnBack);
        Button btnLogout = findViewById(R.id.btnLogout);
        tvFio = findViewById(R.id.tvProfileFio);
        tvLogin = findViewById(R.id.tvProfileLogin);
        tvEmail = findViewById(R.id.tvProfileEmail);
        tvPhone = findViewById(R.id.tvProfilePhone);
        tvAddress = findViewById(R.id.tvProfileAddress);
        btnBack.setOnClickListener(v -> finish());
        loadProfileData();
        findViewById(R.id.btnEditProfile).setOnClickListener(v -> {
            Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
            intent.putExtra("user_id", currentUserId);
            intent.putExtra("fio", tvFio.getText().toString());
            intent.putExtra("email", tvEmail.getText().toString());
            intent.putExtra("phone", tvPhone.getText().toString());
            intent.putExtra("user_id", currentUserId);
            startActivity(intent);
        });
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfileActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        loadProfileData();
    }
    private void loadProfileData() {
        TokenStorage storage = new TokenStorage(this);
        String userId = storage.getUserId();
        if (userId == null) {
            Toast.makeText(this, "Ошибка: Вы не авторизованы", Toast.LENGTH_SHORT).show();
            return;
        }
        ApiService api = RetrofitClient.getApiService(this);
        api.getUserById(userId).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserResponse user = response.body();
                    currentUserId = user.getId();
                    tvFio.setText(user.getFullName());
                    String fullAddress = "";
                    if (user.getIndex() != null) fullAddress += user.getIndex() + ", ";
                    if (user.getCity() != null) fullAddress += user.getCity() + ", ";
                    if (user.getStreet() != null) fullAddress += user.getStreet() + ", ";
                    if (user.getHomeNumber() != null) fullAddress += user.getHomeNumber();
                    if (tvAddress != null) {
                        tvAddress.setText(fullAddress);
                    }
                    tvLogin.setText(user.getLogin());
                    tvEmail.setText(user.getEmail());
                    tvPhone.setText(user.getPhone());
                } else {
                    Toast.makeText(ProfileActivity.this, "Пользователь не найден", Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(ProfileActivity.this, "Ошибка сети", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
