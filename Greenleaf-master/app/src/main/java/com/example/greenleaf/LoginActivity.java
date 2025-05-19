package com.example.greenleaf;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity {
    EditText email, password;
    Button loginButton;
    DatabaseHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        // Initialize views
        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        loginButton = findViewById(R.id.login_button);

        dbHelper = new DatabaseHelper(this);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String strEmail = email.getText().toString().trim();
                String strPassword = password.getText().toString().trim();

                if (TextUtils.isEmpty(strEmail)) {
                    email.setError("Email is required");
                    return;
                }

                if (TextUtils.isEmpty(strPassword)) {
                    password.setError("Password is required");
                    return;
                }

                // Check user credentials
                boolean isValid = dbHelper.checkUser(strEmail, strPassword);

                if (isValid) {
                    Toast.makeText(LoginActivity.this, "Login successful! Welcome back to GreenLeaf", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(LoginActivity.this, "Invalid email or password", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public void onSignupClick(View view) {
        startActivity(new Intent(this, SignupActivity.class));
        finish();
    }


}