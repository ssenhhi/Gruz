package com.example.gruz.activity;
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

import com.example.gruz.models.LoginRequest;
import com.example.gruz.R;
import com.example.gruz.models.TokenResponse;
import com.example.gruz.models.UsersListResponse;
import com.example.gruz.network.ApiService;
import com.example.gruz.network.RetrofitClient;
import com.example.gruz.utils.TokenStorage;
import com.google.android.material.button.MaterialButton;

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
        tvGoToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });
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
                        TokenStorage tokenStorage = new TokenStorage(getApplicationContext());
                        tokenStorage.saveTokens(response.body().getToken(), null);
                        tokenStorage.saveUserEmail(login);
                        ApiService apiService = RetrofitClient.getApiService(getApplicationContext());
                        apiService.getAllUsers().enqueue(new Callback<UsersListResponse>() {
                            @Override
                            public void onResponse(Call<UsersListResponse> call, Response<UsersListResponse> response) {
                                if (response.isSuccessful() && response.body() != null && !response.body().getItems().isEmpty()) {
                                    String userId = response.body().getItems().get(0).getId();
                                    tokenStorage.saveUserId(userId);
                                    tokenStorage.saveUserId(response.body().getItems().get(0).getId());
                                    tokenStorage.saveUserName(response.body().getItems().get(0).getFullName());
                                    Toast.makeText(MainActivity.this, "Успешный вход!", Toast.LENGTH_SHORT).show();
                                    Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                                    startActivity(intent);
                                    finish();
                                } else {
                                    Toast.makeText(MainActivity.this, "Ошибка загрузки профиля", Toast.LENGTH_SHORT).show();
                                }
                            }
                            @Override
                            public void onFailure(Call<UsersListResponse> call, Throwable t) {
                                Toast.makeText(MainActivity.this, "Ошибка сети", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                    @Override
                    public void onFailure(Call<TokenResponse> call, Throwable t) {}
                });
            }
        });
    }
}