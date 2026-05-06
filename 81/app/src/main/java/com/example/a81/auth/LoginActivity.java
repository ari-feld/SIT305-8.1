package com.example.a81.auth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.a81.R;
import com.example.a81.chat.ChatActivity;

public class LoginActivity extends AppCompatActivity {

    EditText etUsername;
    Button btnLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = findViewById(R.id.etUsername);
        btnLogin = findViewById(R.id.btnLogin);

        btnLogin.setOnClickListener(v -> {
            String username = etUsername.getText().toString();

            if (username.isEmpty()) {
                Toast.makeText(this, "Enter username", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(this, ChatActivity.class);
            intent.putExtra("username", username);
            startActivity(intent);
        });
    }
}