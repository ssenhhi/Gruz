package com.example.gruz;
import android.util.Patterns;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private EditText etLogin, etPassword;
    private MaterialButton btnLogin;
    private TextView tvGoToRegister;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        etLogin = findViewById(R.id.etLogin);
        etPassword = findViewById(R.id.etPassword);
        btnLogin = findViewById(R.id.btnLogin);
        tvGoToRegister = findViewById(R.id.tvGoToRegister);
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String login = etLogin.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                if (TextUtils.isEmpty(login) || TextUtils.isEmpty(password)) {
                    Toast.makeText(MainActivity.this, "Заполните все поля!", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!Patterns.EMAIL_ADDRESS.matcher(login).matches()) {
                    Toast.makeText(MainActivity.this, "Введите корректный Email (например, user@mail.ru)", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (password.length() < 8) {
                    Toast.makeText(MainActivity.this, "Пароль должен быть от 8 символов", Toast.LENGTH_SHORT).show();
                    return;
                }
                ApiService api = RetrofitClient.getApiService(getApplicationContext());
                LoginRequest request = new LoginRequest(login, password);

                btnLogin.setEnabled(false);
                btnLogin.setText("Загрузка...");
                api.login(request).enqueue(new Callback<TokenResponse>() {
                    @Override
                    public void onResponse(Call<TokenResponse> call, Response<TokenResponse> response) {
                        btnLogin.setEnabled(true);
                        btnLogin.setText("Войти");
                        if (response.isSuccessful() && response.body() != null) {
                            TokenStorage tokenStorage = new TokenStorage(getApplicationContext());
                            tokenStorage.saveTokens(response.body().getToken(), null);
                            tokenStorage.saveUserEmail(login);
                            Toast.makeText(MainActivity.this, "Успешный вход!", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                            startActivity(intent);
                            finish();
                        } else {
                            if (response.code() == 400) {
                                Toast.makeText(MainActivity.this, "Неверный email или пароль", Toast.LENGTH_LONG).show();
                            } else if (response.code() == 404) {
                                Toast.makeText(MainActivity.this, "Пользователь с таким email не найден", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(MainActivity.this, "Ошибка сервера: " + response.code(), Toast.LENGTH_SHORT).show();
                            }
                        }
                    }
                    @Override
                    public void onFailure(Call<TokenResponse> call, Throwable t) {
                        btnLogin.setEnabled(true);
                        btnLogin.setText("Войти");
                        Toast.makeText(MainActivity.this, "Ошибка сети: " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }
}