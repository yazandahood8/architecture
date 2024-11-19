package com.example.architecture;


import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.architecture.models.User;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class SignUpActivity extends AppCompatActivity {

    private EditText editTextName, editTextPhone, editTextEmail, editTextPassword, editTextConfirmPassword;
    private Button btnSignUp;
    private TextView textLogin;
    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Initialize Firebase Auth and Database reference
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference("Users");

        // Initialize UI elements
        editTextName = findViewById(R.id.editTextName);
        editTextPhone = findViewById(R.id.editTextPhone);
        editTextEmail = findViewById(R.id.editTextEmail);
        editTextPassword = findViewById(R.id.editTextPassword);
        editTextConfirmPassword = findViewById(R.id.editTextConfirmPassword);
        btnSignUp = findViewById(R.id.btnSignUp);
        textLogin = findViewById(R.id.textLogin);

        // Handle sign-up button click
        btnSignUp.setOnClickListener(v -> registerUser());

        // Redirect to login screen if user already has an account
        textLogin.setOnClickListener(v -> startActivity(new Intent(SignUpActivity.this, LoginActivity.class)));
    }

    private void registerUser() {
        String name = editTextName.getText().toString().trim();
        String phone = editTextPhone.getText().toString().trim();
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String confirmPassword = editTextConfirmPassword.getText().toString().trim();

        if (TextUtils.isEmpty(name)) {
            editTextName.setError("Name is required");
            return;
        }
        if (TextUtils.isEmpty(phone)) {
            editTextPhone.setError("Phone number is required");
            return;
        }
        if (TextUtils.isEmpty(email)) {
            editTextEmail.setError("Email is required");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            editTextPassword.setError("Password is required");
            return;
        }
        if (!password.equals(confirmPassword)) {
            editTextConfirmPassword.setError("Passwords do not match");
            return;
        }
        if (password.length() < 6) {
            editTextPassword.setError("Password should be at least 6 characters");
            return;
        }

        // Register new user with Firebase Authentication
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                // Save additional user data to Realtime Database
                String key = email.replace(".", ","); // Replace "." with "," to use email as key

                User user = new User(name, phone, email, password);
                mDatabase.child(key).setValue(user).addOnCompleteListener(databaseTask -> {
                    if (databaseTask.isSuccessful()) {
                        Toast.makeText(SignUpActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(SignUpActivity.this, SplashScreenActivity.class));
                        finish();
                    } else {
                        Toast.makeText(SignUpActivity.this, "Error: " + databaseTask.getException().getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                Toast.makeText(SignUpActivity.this, "Error: " + task.getException().getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}
