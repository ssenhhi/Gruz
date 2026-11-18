package com.example.gruz;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.button.MaterialButton;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateOrderActivity extends AppCompatActivity {

    private LinearLayout layoutStep1, layoutStep2, layoutStep3;
    private TextView step1Circle, step2Circle, step3Circle, tvStepTitle, tvCalculatedPrice;
    private EditText etWeight, etVolume;
    private String currentOrderId = null; // ID заказа, созданного на шаге 1
    private int currentPrice = 0; // Цена, рассчитанная на шаге 2
    private String currentPaymentMethod = "Карта";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_order);

        // Инициализация элементов
        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());

        layoutStep1 = findViewById(R.id.layoutStep1);
        layoutStep2 = findViewById(R.id.layoutStep2);
        layoutStep3 = findViewById(R.id.layoutStep3);

        step1Circle = findViewById(R.id.step1Circle);
        step2Circle = findViewById(R.id.step2Circle);
        step3Circle = findViewById(R.id.step3Circle);
        tvStepTitle = findViewById(R.id.tvStepTitle);

        etWeight = findViewById(R.id.etWeight);
        etVolume = findViewById(R.id.etVolume);
        tvCalculatedPrice = findViewById(R.id.tvCalculatedPrice);

        MaterialButton btnNext1 = findViewById(R.id.btnNext1);
        MaterialButton btnNext2 = findViewById(R.id.btnNext2);
        MaterialButton btnSubmit = findViewById(R.id.btnSubmit);

        // --- ШАГ 1: СОЗДАНИЕ ЗАКАЗА ---
        btnNext1.setOnClickListener(v -> {
            // Проверка полей
            String fromCity = ((EditText) findViewById(R.id.etFromCity)).getText().toString();
            String toCity = ((EditText) findViewById(R.id.etToCity)).getText().toString();
            if (fromCity.isEmpty() || toCity.isEmpty()) {
                Toast.makeText(this, "Заполните города!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Создаем заказ на сервере
            TokenStorage storage = new TokenStorage(this);
            String userId = storage.getUserId();
            if (userId == null) {
                Toast.makeText(this, "Вы не авторизованы!", Toast.LENGTH_SHORT).show();
                return;
            }

            String orderNumber = "#" + (int)(Math.random() * 9000 + 1000);
            String comment = ((EditText) findViewById(R.id.etComment)).getText().toString();

            ApiService api = RetrofitClient.getApiService(this);
            OrderCreateRequest orderRequest = new OrderCreateRequest(orderNumber, "Создан", comment, userId);

            btnNext1.setEnabled(false);
            btnNext1.setText("Создание...");

            api.createOrder(orderRequest).enqueue(new Callback<OrderResponse>() {
                @Override
                public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                    btnNext1.setEnabled(true);
                    btnNext1.setText("Далее →");

                    if (response.isSuccessful() && response.body() != null) {
                        currentOrderId = response.body().getId(); // Сохраняем ID!

                        // Переходим на шаг 2
                        layoutStep1.setVisibility(View.GONE);
                        layoutStep2.setVisibility(View.VISIBLE);
                        step1Circle.setBackgroundResource(R.drawable.bg_step_inactive);
                        step2Circle.setBackgroundResource(R.drawable.bg_step_active);
                        tvStepTitle.setText("Шаг 2 из 3 — Груз");
                        Toast.makeText(CreateOrderActivity.this, "Заказ создан!", Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(CreateOrderActivity.this, "Ошибка создания заказа", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<OrderResponse> call, Throwable t) {
                    btnNext1.setEnabled(true);
                    btnNext1.setText("Далее →");
                    Toast.makeText(CreateOrderActivity.this, "Ошибка сети", Toast.LENGTH_SHORT).show();
                }
            });
        });

        // --- ШАГ 2: ГРУЗ И РАСЧЕТ ЦЕНЫ ---
        btnNext2.setOnClickListener(v -> {
            if (currentOrderId == null) {
                Toast.makeText(this, "Ошибка: Заказ не создан", Toast.LENGTH_SHORT).show();
                return;
            }

            String weightStr = etWeight.getText().toString();
            String volumeStr = etVolume.getText().toString();
            if (weightStr.isEmpty() || volumeStr.isEmpty()) {
                Toast.makeText(this, "Введите вес и объем!", Toast.LENGTH_SHORT).show();
                return;
            }

            double weight = Double.parseDouble(weightStr);
            double volume = Double.parseDouble(volumeStr);
            int length = Integer.parseInt(((EditText) findViewById(R.id.etLength)).getText().toString());
            int width = Integer.parseInt(((EditText) findViewById(R.id.etWidth)).getText().toString());
            int height = Integer.parseInt(((EditText) findViewById(R.id.etHeight)).getText().toString());

            // Считаем цену
            currentPrice = (int) ((weight * 100) + (volume * 500) + 500);
            tvCalculatedPrice.setText(currentPrice + " ₽");

            // Ищем тип груза и создаем груз
            String cargoTypeName = ((EditText) findViewById(R.id.etCargoType)).getText().toString().trim();
            ApiService api = RetrofitClient.getApiService(this);

            btnNext2.setEnabled(false);
            btnNext2.setText("Сохранение...");

            api.getCargoTypes().enqueue(new Callback<CargoTypesListResponse>() {
                @Override
                public void onResponse(Call<CargoTypesListResponse> call, Response<CargoTypesListResponse> response) {
                    String typeId = "2sefgc39cl4qxes"; // Обычный
                    if (response.isSuccessful() && response.body() != null) {
                        for (CargoTypeResponse type : response.body().getItems()) {
                            if (type.getName().equalsIgnoreCase(cargoTypeName)) {
                                typeId = type.getId();
                                break;
                            }
                        }
                    }

                    CargoCreateRequest cargoRequest = new CargoCreateRequest(currentOrderId, typeId, weight, volume, length, width, height);
                    api.createCargo(cargoRequest).enqueue(new Callback<CargoCreateRequest>() {
                        @Override
                        public void onResponse(Call<CargoCreateRequest> call, Response<CargoCreateRequest> response) {
                            btnNext2.setEnabled(true);
                            btnNext2.setText("Далее →");

                            if (response.isSuccessful()) {
                                // Переходим на шаг 3
                                layoutStep2.setVisibility(View.GONE);
                                layoutStep3.setVisibility(View.VISIBLE);
                                step2Circle.setBackgroundResource(R.drawable.bg_step_inactive);
                                step3Circle.setBackgroundResource(R.drawable.bg_step_active);
                                tvStepTitle.setText("Шаг 3 из 3 — Оплата");
                                Toast.makeText(CreateOrderActivity.this, "Груз сохранен!", Toast.LENGTH_SHORT).show();
                            } else {
                                Toast.makeText(CreateOrderActivity.this, "Ошибка сохранения груза", Toast.LENGTH_SHORT).show();
                            }
                        }

                        @Override
                        public void onFailure(Call<CargoCreateRequest> call, Throwable t) {
                            btnNext2.setEnabled(true);
                            btnNext2.setText("Далее →");
                            Toast.makeText(CreateOrderActivity.this, "Ошибка сети", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

                @Override
                public void onFailure(Call<CargoTypesListResponse> call, Throwable t) {
                    btnNext2.setEnabled(true);
                    btnNext2.setText("Далее →");
                }
            });
        });

        // --- ШАГ 3: ОПЛАТА ---
        btnSubmit.setOnClickListener(v -> {
            if (currentOrderId == null) {
                Toast.makeText(this, "Ошибка: Заказ не создан", Toast.LENGTH_SHORT).show();
                return;
            }

            // Получаем способ оплаты
            RadioGroup rgPayment = findViewById(R.id.radioGroupPayment);
            if (rgPayment != null) {
                int selectedId = rgPayment.getCheckedRadioButtonId();
                if (selectedId != -1) {
                    RadioButton selectedRb = findViewById(selectedId);
                    if (selectedRb != null) {
                        currentPaymentMethod = selectedRb.getText().toString();
                    }
                }
            }

            ApiService api = RetrofitClient.getApiService(this);
            PaymentCreateRequest paymentRequest = new PaymentCreateRequest(currentOrderId, currentPaymentMethod, currentPrice, "Не оплачено");

            btnSubmit.setEnabled(false);
            btnSubmit.setText("Отправка...");

            api.createPayment(paymentRequest).enqueue(new Callback<PaymentCreateRequest>() {
                @Override
                public void onResponse(Call<PaymentCreateRequest> call, Response<PaymentCreateRequest> response) {
                    btnSubmit.setEnabled(true);
                    btnSubmit.setText("Подтвердить заказ");

                    if (response.isSuccessful()) {
                        Intent intent = new Intent(CreateOrderActivity.this, OrderSuccessActivity.class);
                        startActivity(intent);
                        finish();
                    } else {
                        Toast.makeText(CreateOrderActivity.this, "Ошибка оплаты", Toast.LENGTH_SHORT).show();
                    }
                }

                @Override
                public void onFailure(Call<PaymentCreateRequest> call, Throwable t) {
                    btnSubmit.setEnabled(true);
                    btnSubmit.setText("Подтвердить заказ");
                    Toast.makeText(CreateOrderActivity.this, "Ошибка сети", Toast.LENGTH_SHORT).show();
                }
            });
        });
    }
}

