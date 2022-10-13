package com.example.buscandohuellas;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.buscandohuellas.databinding.ActivityAuthBinding;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import java.security.Provider;

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
            if (binding.LoginCorreo.getEditText().getText().toString().length() > 0
            && binding.LoginContrasena.getEditText().getText().toString().length() > 0) {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(binding.LoginCorreo.getEditText().getText().toString(),
                binding.LoginContrasena.getEditText().getText().toString()).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {

                        } else {
                            showAlert();
                        }
                    }
                });
            }
        });

    }

    private void showAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Error");
        builder.setMessage("Se ha producido un error autenticando al usuario");
        builder.setPositiveButton("Aceptar", null);
        AlertDialog dialog = builder.create();
        dialog.show();
    }
    private void showHome(String email, Provider provider) {

        Object HomeActivity = null;
        Intent homeIntent = new Intent(this, HomeActivity.getClass());
        homeIntent.putExtra("email", email);
        homeIntent.putExtra("provider", provider.getName());
        startActivity(homeIntent);
    }
}