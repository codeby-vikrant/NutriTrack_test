package com.example.nutritrack_test;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class LoginActivity extends AppCompatActivity {
    
    private TextInputLayout emailLayout;
    private TextInputLayout passwordLayout;
    private TextInputEditText emailInput;
    private TextInputEditText passwordInput;
    private Button loginButton;
    private TextView switchToSignup;
    private View loginCard;
    
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        
        // Initialize views
        emailLayout = findViewById(R.id.email_layout);
        passwordLayout = findViewById(R.id.password_layout);
        emailInput = findViewById(R.id.email_input);
        passwordInput = findViewById(R.id.password_input);
        loginButton = findViewById(R.id.login_button);
        switchToSignup = findViewById(R.id.switch_to_signup);
        loginCard = findViewById(R.id.login_card);
        
        // Load animations
        Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        
        // Start animations
        loginCard.startAnimation(slideUp);
        loginButton.startAnimation(fadeIn);
        
        // Setup click listeners
        loginButton.setOnClickListener(v -> handleLogin());
        switchToSignup.setOnClickListener(v -> switchToSignup());
    }
    
    private void handleLogin() {
        String email = emailInput.getText().toString();
        String password = passwordInput.getText().toString();

        // Validate inputs
        if (email.isEmpty()) {
            emailLayout.setError("Email is required");
            return;
        }
        
        if (password.isEmpty()) {
            passwordLayout.setError("Password is required");
            return;
        }
        
        // Show loading animation
        loginButton.setEnabled(false);
        loginButton.setText("Logging in...");
        
        // TODO: Implement actual login logic here
        // For now, we'll just simulate a successful login
        new android.os.Handler().postDelayed(() -> {
            // Start main activity
            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(intent);
            finish();
        }, 1500);
    }
    
    private void switchToSignup() {
        // TODO: Implement signup screen transition
        Toast.makeText(this, "Sign up coming soon!", Toast.LENGTH_SHORT).show();
    }
} 