package com.example.greenleaf;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.greenleaf.R;

public class SignupActivity extends AppCompatActivity {
    EditText fullName, email, phone, password;
    Button signupButton;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        // Initialize views
        fullName = findViewById(R.id.fullname);
        email = findViewById(R.id.email);
        phone = findViewById(R.id.phone);
        password = findViewById(R.id.password);
        signupButton = findViewById(R.id.signup_button);

        dbHelper = new DatabaseHelper(this);

        signupButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strFullName = fullName.getText().toString().trim();
                String strEmail = email.getText().toString().trim();
                String strPhone = phone.getText().toString().trim();
                String strPassword = password.getText().toString().trim();

                if (TextUtils.isEmpty(strFullName)) {
                    fullName.setError("Full name is required");
                    return;
                }

                if (TextUtils.isEmpty(strEmail)) {
                    email.setError("Email is required");
                    return;
                }

                if (TextUtils.isEmpty(strPhone)) {
                    phone.setError("Phone number is required");
                    return;
                }

                if (TextUtils.isEmpty(strPassword)) {
                    password.setError("Password is required");
                    return;
                }

                if (strPassword.length() < 6) {
                    password.setError("Password must be at least 6 characters");
                    return;
                }

                // Check if email already exists
                if (dbHelper.checkEmail(strEmail)) {
                    email.setError("Email already exists");
                    return;
                }

                // Add new user
                boolean isInserted = dbHelper.addUser(strFullName, strEmail, strPhone, strPassword);

                if (isInserted) {
                    Toast.makeText(SignupActivity.this, "Registration successful! Welcome to GreenLeaf", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SignupActivity.this, LoginActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(SignupActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void onLoginClick(View view) {
        startActivity(new Intent(this, LoginActivity.class));
        finish();
    }
}