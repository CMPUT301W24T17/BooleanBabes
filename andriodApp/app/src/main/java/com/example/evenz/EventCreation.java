package com.example.evenz;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class EventCreation extends AppCompatActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.create_event);

        findViewById(R.id.back_less).setOnClickListener(v -> finish());
    }
}