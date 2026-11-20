package com.example.gruz.activity;

import android.os.Bundle;
import android.text.TextUtils;
import android.util.Patterns;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gruz.R;
import com.example.gruz.models.UserResponse;
import com.example.gruz.models.UserUpdateRequest;
import com.example.gruz.network.ApiService;
import com.example.gruz.network.RetrofitClient;
import com.google.android.material.button.MaterialButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class EditProfileActivity extends AppCompatActivity {

    private EditText etFio, etLogin, etEmail, etPhone, etHouse, etStreet, etCity, etIndex;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        etFio = findViewById(R.id.etFio);
        etLogin = findViewById(R.id.etLogin);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
         etIndex = findViewById(R.id.etIndex);
         etCity = findViewById(R.id.etCity);
         etStreet = findViewById(R.id.etStreet);
         etHouse = findViewById(R.id.etHouse);
        ImageView btnBack = findViewById(R.id.btnBack);
        MaterialButton btnSave = findViewById(R.id.btnSave);
        MaterialButton btnCancel = findViewById(R.id.btnCancel);
        userId = getIntent().getStringExtra("user_id");
        if (userId == null) {
            Toast.makeText(this, "Ошибка: ID не передан", Toast.LENGTH_SHORT).show();
            finish();
            return;
        }
        btnBack.setOnClickListener(v -> finish());
        btnCancel.setOnClickListener(v -> finish());
        loadUserData();
        btnSave.setOnClickListener(v -> {
            if (etFio == null || etLogin == null || etEmail == null || etPhone == null) {
                Toast.makeText(this, "Ошибка верстки: не найдены поля", Toast.LENGTH_SHORT).show();
                return;
            }
            String newFio = etFio.getText().toString().trim();
            String newLogin = etLogin.getText().toString().trim();
            String newEmail = etEmail.getText().toString().trim();
            String newPhone = etPhone.getText().toString().trim();
            String newIndex = etIndex != null ? etIndex.getText().toString().trim() : "";
            String newCity = etCity != null ? etCity.getText().toString().trim() : "";
            String newStreet = etStreet != null ? etStreet.getText().toString().trim() : "";
            String newHouse = etHouse != null ? etHouse.getText().toString().trim() : "";
            if (TextUtils.isEmpty(newFio) || TextUtils.isEmpty(newLogin)) {
                Toast.makeText(this, "ФИО и Логин не могут быть пустыми", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(newEmail).matches()) {
                Toast.makeText(this, "Некорректный Email", Toast.LENGTH_SHORT).show();
                return;
            }
            String cleanPhone = newPhone.replaceAll("[\\s-]", "");
            if (!cleanPhone.matches("^\\+?[78][0-9]{10}$")) {
                Toast.makeText(this, "Телефон в формате: +7 999 123-45-67", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!newHouse.isEmpty() && !newHouse.matches("^[0-9][0-9a-zA-Zа-яА-Я\\s\\/\\-]*$")) {
                Toast.makeText(this, "Некорректный номер дома", Toast.LENGTH_SHORT).show();
                return;
            }
            ApiService api = RetrofitClient.getApiService(this);
            UserUpdateRequest request = new UserUpdateRequest(newFio, newEmail, cleanPhone, newLogin, newIndex, newCity, newStreet, newHouse);
            api.updateUser(userId, request).enqueue(new Callback<UserResponse>() {
                @Override
                public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                    if (response.isSuccessful()) {
                        Toast.makeText(EditProfileActivity.this, "Профиль обновлен!", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(EditProfileActivity.this, "Ошибка: " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<UserResponse> call, Throwable t) {
                    Toast.makeText(EditProfileActivity.this, "Ошибка сети: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
    private void loadUserData() {
        ApiService api = RetrofitClient.getApiService(this);
        api.getUserById(userId).enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    UserResponse user = response.body();
                    if (etFio != null) etFio.setText(user.getFullName());
                    if (etLogin != null) etLogin.setText(user.getLogin());
                    if (etEmail != null) etEmail.setText(user.getEmail());
                    if (etPhone != null) etPhone.setText(user.getPhone());
                    if (etIndex != null) etIndex.setText(user.getIndex());
                    if (etCity != null) etCity.setText(user.getCity());
                    if (etStreet != null) etStreet.setText(user.getStreet());
                    if (etHouse != null) etHouse.setText(user.getHomeNumber());
                }
            }
            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                Toast.makeText(EditProfileActivity.this, "Ошибка загрузки", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
