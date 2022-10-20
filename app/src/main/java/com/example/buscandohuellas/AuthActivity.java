package com.example.buscandohuellas;

import android.content.Intent;
import android.os.Bundle;
import android.text.InputFilter;
import android.text.Spanned;
import android.util.Log;
import android.view.KeyEvent;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.buscandohuellas.databinding.ActivityAuthBinding;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class AuthActivity extends AppCompatActivity {

    private ActivityAuthBinding binding;
    private final FirebaseFirestore db = FirebaseFirestore.getInstance();
    private static final String TAG = "AuthActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        setTheme(R.style.Theme_BuscandoHuellas);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        binding = ActivityAuthBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Put filter to editText to prevent input of whitespaces
        TextInputEditText inputCorreo = binding.editTextCorreo;
        TextInputEditText inputContrasena = binding.editTextContrasena;
        inputContrasena.setFilters(new InputFilter[] { filter });
        inputCorreo.setFilters(new InputFilter[] { filter });

        //Setup
        setup();
    }

    //Filter to disable whitespaces
    InputFilter filter = new InputFilter() {
        public CharSequence filter(CharSequence source, int start, int end,
                                   Spanned dest, int dstart, int dend) {
            for (int i = start; i < end; i++) {
                if (Character.isWhitespace(source.charAt(i))) {
                    return "";
                }
            }
            return null;
        }

    };


    private void setup() {
        //Define action when clicking Sign Up button
        binding.SignUpButton.setOnClickListener(view -> {
            if (Objects.requireNonNull(binding.LoginCorreo.getEditText()).getText().toString().length() > 0
            && Objects.requireNonNull(binding.LoginContrasena.getEditText()).getText().toString().length() > 7) {
                FirebaseAuth.getInstance().createUserWithEmailAndPassword(binding.LoginCorreo.getEditText().getText().toString(),
                binding.LoginContrasena.getEditText().getText().toString()).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        addToDatabase();
                        showHome(Objects.requireNonNull(task.getResult().getUser()).getEmail(), ProviderType.BASIC);
                    } else {
                        showAlert();
                    }
                });
            } else {
                Toast.makeText(this, "La contraseña debe tener por lo menos 8 caracteres", Toast.LENGTH_SHORT).show();
            }
        });

        //Define action when clicking Log in button
        binding.LogInButton.setOnClickListener(view -> {
            if (Objects.requireNonNull(binding.LoginCorreo.getEditText()).getText().toString().length() > 0
                    && Objects.requireNonNull(binding.LoginContrasena.getEditText()).getText().toString().length() > 0) {
                FirebaseAuth.getInstance().signInWithEmailAndPassword(binding.LoginCorreo.getEditText().getText().toString(),
                        binding.LoginContrasena.getEditText().getText().toString()).addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                showHome(Objects.requireNonNull(task.getResult().getUser()).getEmail(), ProviderType.BASIC);
                            } else {
                                showAlert();
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
    private void showHome(String email, ProviderType provider) {
        Intent homeIntent = new Intent(this, MenuActivity.class);
        homeIntent.putExtra("email", email);
        homeIntent.putExtra("provider", ProviderType.BASIC.name());
        startActivity(homeIntent);
    }

    private void addToDatabase(){
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String email = user.getEmail();
            String uid = user.getUid();
            Map<String, Object> userdb = new HashMap<>();
            userdb.put("email", email);
            db.collection("Usuarios").document(uid).set(userdb).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Log.d(TAG, "DocumentSnapshot added with ID: ");
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Log.w(TAG, "Error adding document", e);
                        }
                    });
        }
    }
}