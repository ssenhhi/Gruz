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

    private EditText etFio, etEmail, etPhone;
    private String userId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        etFio = findViewById(R.id.etFio);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        ImageView btnBack = findViewById(R.id.btnBack);
        MaterialButton btnSave = findViewById(R.id.btnSave);
        MaterialButton btnCancel = findViewById(R.id.btnCancel);
        String fio = getIntent().getStringExtra("fio");
        String email = getIntent().getStringExtra("email");
        String phone = getIntent().getStringExtra("phone");
        userId = getIntent().getStringExtra("user_id");
        if (fio != null) etFio.setText(fio);
        if (email != null) etEmail.setText(email);
        if (phone != null) etPhone.setText(phone);
        btnBack.setOnClickListener(v -> finish());
        btnCancel.setOnClickListener(v -> finish());
        btnSave.setOnClickListener(v -> {
            String newFio = etFio.getText().toString().trim();
            String newEmail = etEmail.getText().toString().trim();
            String newPhone = etPhone.getText().toString().trim();
            if (userId == null || userId.isEmpty()) {
                Toast.makeText(this, "Ошибка: ID пользователя не найден", Toast.LENGTH_SHORT).show();
                finish();
                return;
            }
            if (TextUtils.isEmpty(newFio)) {
                Toast.makeText(this, "ФИО не может быть пустым", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!Patterns.EMAIL_ADDRESS.matcher(newEmail).matches()) {
                Toast.makeText(this, "Некорректный Email", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!newPhone.matches("^\\+?[78][0-9]{10}$")) {
                Toast.makeText(this, "Телефон в формате: +7 999 123-45-67", Toast.LENGTH_SHORT).show();
                return;
            }
            ApiService api = RetrofitClient.getApiService(this);
            UserUpdateRequest request = new UserUpdateRequest(newFio, newEmail, newPhone);
            String testId = getIntent().getStringExtra("user_id");
            api.updateUser(testId, request).enqueue(new Callback<UserResponse>() {
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
}