package com.example.gruz.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gruz.models.CargoCreateRequest;
import com.example.gruz.models.CargoTypeResponse;
import com.example.gruz.models.CargoTypesListResponse;
import com.example.gruz.models.OrderCreateRequest;
import com.example.gruz.models.OrderResponse;
import com.example.gruz.models.PaymentCreateRequest;
import com.example.gruz.R;
import com.example.gruz.network.ApiService;
import com.example.gruz.network.RetrofitClient;
import com.example.gruz.utils.TokenStorage;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CreateOrderActivity extends AppCompatActivity {

    private LinearLayout layoutStep1, layoutStep2, layoutStep3;
    private TextView step1Circle, step2Circle, step3Circle, tvStepTitle, tvCalculatedPrice;
    private EditText etWeight, etVolume, etFromCity, etFromAddress, etToCity, etToAddress, etComment;
    private String currentOrderId = null;
    private int currentPrice = 0;
    private String currentPaymentMethod = "Карта";
    private String orderNumber = "";
    private Spinner spinnerCargoType;
    private List<CargoTypeResponse> cargoTypesList = new ArrayList<>();
    private List<String> cargoTypeNames = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_order);

        spinnerCargoType = findViewById(R.id.spinnerCargoType);
        loadCargoTypes();
        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());
        etFromCity = findViewById(R.id.etFromCity);
         etToCity = findViewById(R.id.etToCity);
         etFromAddress = findViewById(R.id.etFromAddress);
         etToAddress = findViewById(R.id.etToAddress);
        etComment = findViewById(R.id.etComment);
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
        btnNext1.setOnClickListener(v -> {
            String fromCity = etFromCity.getText().toString().trim();
            String toCity = etToCity.getText().toString().trim();
            String fromStreet = etFromAddress.getText().toString().trim();
            String toStreet = etToAddress.getText().toString().trim();
            String comment = etComment.getText().toString().trim();
            if (fromCity.isEmpty() || toCity.isEmpty()) {
                Toast.makeText(this, "Заполните города!", Toast.LENGTH_SHORT).show();
                return;
            }
            TokenStorage storage = new TokenStorage(this);
            String userId = storage.getUserId();
            if (userId == null) {
                Toast.makeText(this, "Вы не авторизованы!", Toast.LENGTH_SHORT).show();
                return;
            }
            orderNumber = "#" + (int)(Math.random() * 9000 + 1000);
            ApiService api = RetrofitClient.getApiService(this);
            OrderCreateRequest orderRequest = new OrderCreateRequest(
                    orderNumber,
                    "Создан",
                    comment,
                    userId,
                    fromCity,
                    toCity,
                    fromStreet,
                    toStreet
            );

            btnNext1.setEnabled(false);
            btnNext1.setText("Создание...");
            api.createOrder(orderRequest).enqueue(new Callback<OrderResponse>() {
                @Override
                public void onResponse(Call<OrderResponse> call, Response<OrderResponse> response) {
                    btnNext1.setEnabled(true);
                    btnNext1.setText("Далее →");
                    if (response.isSuccessful() && response.body() != null) {
                        currentOrderId = response.body().getId();
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
            currentPrice = (int) ((weight * 100) + (volume * 500) + 500);
            tvCalculatedPrice.setText(currentPrice + " ₽");
            int selectedPosition = spinnerCargoType.getSelectedItemPosition();
            String typeId = "2sefgc39cl4qxes";

            if (selectedPosition != -1 && selectedPosition < cargoTypesList.size()) {
                typeId = cargoTypesList.get(selectedPosition).getId();
            }
            ApiService api = RetrofitClient.getApiService(this);
            btnNext2.setEnabled(false);
            btnNext2.setText("Сохранение...");
            CargoCreateRequest cargoRequest = new CargoCreateRequest(currentOrderId, typeId, weight, volume, length, width, height);
            api.createCargo(cargoRequest).enqueue(new Callback<CargoCreateRequest>() {
                @Override
                public void onResponse(Call<CargoCreateRequest> call, Response<CargoCreateRequest> response) {
                    btnNext2.setEnabled(true);
                    btnNext2.setText("Далее →");
                    if (response.isSuccessful()) {
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
        });
        btnSubmit.setOnClickListener(v -> {
            if (currentOrderId == null) {
                Toast.makeText(this, "Ошибка: Заказ не создан", Toast.LENGTH_SHORT).show();
                return;
            }
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
                        intent.putExtra("order_number", orderNumber);
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
    private void loadCargoTypes() {
        ApiService api = RetrofitClient.getApiService(this);
        api.getCargoTypes().enqueue(new Callback<CargoTypesListResponse>() {
            @Override
            public void onResponse(Call<CargoTypesListResponse> call, Response<CargoTypesListResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    cargoTypesList = response.body().getItems();
                    cargoTypeNames.clear();

                    for (CargoTypeResponse type : cargoTypesList) {
                        cargoTypeNames.add(type.getName());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter<>(CreateOrderActivity.this, android.R.layout.simple_spinner_item, cargoTypeNames);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spinnerCargoType.setAdapter(adapter);
                }
            }
            @Override
            public void onFailure(Call<CargoTypesListResponse> call, Throwable t) {
                Toast.makeText(CreateOrderActivity.this, "Ошибка загрузки типов груза", Toast.LENGTH_SHORT).show();
            }
        });
    }
}

