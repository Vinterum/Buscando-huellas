package com.example.buscandohuellas;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

enum Provider{
    BASIC
}

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
    }
}