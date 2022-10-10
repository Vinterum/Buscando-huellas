package com.example.buscandohuellas;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;

import com.example.buscandohuellas.databinding.ActivityAuthBinding;

public class AuthActivity extends AppCompatActivity {

    private ActivityAuthBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        binding = ActivityAuthBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Setup
        setup();
    }

    private void setup() {
        String title = "Autenticación";
        binding.SignUpButton.setOnClickListener(view -> {
            if (binding.LoginCorreo.getEditText().getText().toString().length() > 0 && binding.LoginContrasena.getEditText().getText().toString().length() > 0);
        });

    }
}