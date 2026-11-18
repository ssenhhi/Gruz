package com.example.gruz;

import android.util.Patterns;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RegisterActivity extends AppCompatActivity {
    private EditText etEmail, etUsername, etPassword, etPasswordConfirm, etFio, etPhone;
    private Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etEmail = findViewById(R.id.etRegEmail);
        etUsername = findViewById(R.id.etRegUsername);
        etPassword = findViewById(R.id.etRegPassword);
        etPasswordConfirm = findViewById(R.id.etRegPasswordConfirm);
        etFio = findViewById(R.id.etRegFio);
        etPhone = findViewById(R.id.etRegPhone);
        btnRegister = findViewById(R.id.btnRegister);
        if (etEmail == null || etUsername == null || etPassword == null || etPasswordConfirm == null || etFio == null || etPhone == null || btnRegister == null) {
            Toast.makeText(this, "Ошибка: не найдены поля в XML!", Toast.LENGTH_LONG).show();
            return;
        }
        btnRegister.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            String passwordConfirm = etPasswordConfirm.getText().toString().trim();
            String fio = etFio.getText().toString().trim();
            String phone = etPhone.getText().toString().trim();
            EditText etIndex = findViewById(R.id.etRegIndex);
            EditText etCity = findViewById(R.id.etRegCity);
            EditText etStreet = findViewById(R.id.etRegStreet);
            String index = etIndex.getText().toString().trim();
            String city = etCity.getText().toString().trim();
            String street = etStreet.getText().toString().trim();
            if (email.isEmpty() || !Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
                Toast.makeText(this, "Введите корректный Email (example@mail.ru)", Toast.LENGTH_SHORT).show();
                return;
            }
            if (username.length() < 3) {
                Toast.makeText(this, "Логин должен быть от 3 символов", Toast.LENGTH_SHORT).show();
                return;
            }
            if (password.length() < 8) {
                Toast.makeText(this, "Пароль должен быть от 8 символов!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!password.equals(passwordConfirm)) {
                Toast.makeText(this, "Пароли не совпадают!", Toast.LENGTH_SHORT).show();
                return;
            }
            if (fio.isEmpty() || !fio.matches("^[a-zA-Zа-яА-Я\\s]+$")) {
                Toast.makeText(this, "ФИО должно содержать только буквы", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!phone.matches("^\\+?[78][0-9]{10}$")) {
                Toast.makeText(this, "Телефон в формате: +7 999 123-45-67", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!index.matches("^[0-9]{6}$")) {
                Toast.makeText(this, "Индекс должен состоять из 6 цифр", Toast.LENGTH_SHORT).show();
                return;
            }
            if (city.isEmpty() || !city.matches("^[a-zA-Zа-яА-Я\\-]+$")) {
                Toast.makeText(this, "Город должен содержать только буквы", Toast.LENGTH_SHORT).show();
                return;
            }
            if (street.isEmpty() || !street.matches("^[a-zA-Zа-яА-Я0-9\\s\\.]+$")) {
                Toast.makeText(this, "Проверьте написание улицы", Toast.LENGTH_SHORT).show();
                return;
            }
            ApiService api = RetrofitClient.getApiService(getApplicationContext());
            RegisterRequest request = new RegisterRequest(username, fio, phone, email, password);

            btnRegister.setEnabled(false);
            btnRegister.setText("Загрузка...");
            api.register(request).enqueue(new Callback<UserResponse>() {
                @Override
                public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                    btnRegister.setEnabled(true);
                    btnRegister.setText("Зарегистрироваться");

                    if (response.isSuccessful()) {
                        TokenStorage tokenStorage = new TokenStorage(getApplicationContext());
                        Toast.makeText(RegisterActivity.this, "Успешно! Теперь войдите.", Toast.LENGTH_LONG).show();
                        finish();
                    } else {
                        Toast.makeText(RegisterActivity.this, "Ошибка: " + response.code(), Toast.LENGTH_SHORT).show();
                    }
                }
                @Override
                public void onFailure(Call<UserResponse> call, Throwable t) {
                    btnRegister.setEnabled(true);
                    btnRegister.setText("Зарегистрироваться");
                    Toast.makeText(RegisterActivity.this, "Ошибка сети: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}
