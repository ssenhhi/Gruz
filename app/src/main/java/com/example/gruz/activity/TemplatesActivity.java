package com.example.gruz.activity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.gruz.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class TemplatesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_templates);

        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> finish());
        FloatingActionButton fab = findViewById(R.id.fabAddTemplate);
        fab.setOnClickListener(v -> {
            Toast.makeText(this, "Создание нового шаблона (скоро)", Toast.LENGTH_SHORT).show();
        });
        MaterialButton btn1 = findViewById(R.id.btnUse1);
        btn1.setOnClickListener(v -> Toast.makeText(this, "Шаблон 'Хрупкий груз' выбран", Toast.LENGTH_SHORT).show());
        MaterialButton btn2 = findViewById(R.id.btnUse2);
        btn2.setOnClickListener(v -> Toast.makeText(this, "Шаблон 'Документы' выбран", Toast.LENGTH_SHORT).show());
        MaterialButton btn3 = findViewById(R.id.btnUse3);
        btn3.setOnClickListener(v -> Toast.makeText(this, "Шаблон 'Мебель' выбран", Toast.LENGTH_SHORT).show());
        MaterialButton btn4 = findViewById(R.id.btnUse4);
        btn4.setOnClickListener(v -> Toast.makeText(this, "Шаблон 'Скоропортящееся' выбран", Toast.LENGTH_SHORT).show());
    }
}
