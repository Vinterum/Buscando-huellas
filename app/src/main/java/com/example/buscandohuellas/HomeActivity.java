package com.example.buscandohuellas;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.buscandohuellas.databinding.ActivityHomeBinding;
import com.google.firebase.auth.FirebaseAuth;

enum ProviderType{
    BASIC
}

public class HomeActivity extends AppCompatActivity {

    private ActivityHomeBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        
        //Setup
        Bundle bundle = getIntent().getExtras();
        String email = bundle.getString("email");
        String provider = bundle.getString("provider");
        setup(email, provider);
    }

    private void setup(String email, String provider) {
        String title = "Inicio";
        binding.textViewEmail.setText(email);
        binding.textViewProveedor.setText(provider);

        binding.logOutButton.setOnClickListener(view -> {
            FirebaseAuth.getInstance().signOut();
            onBackPressed();
        });
    }
}