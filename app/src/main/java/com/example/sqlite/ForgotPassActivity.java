package com.example.sqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class ForgotPassActivity extends AppCompatActivity {
    private Button sendCodeBtn, cancelBtn;
    private EditText emailET, usernameET;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);
        initView();

        sendCodeBtn.setOnClickListener(view->{
            String username = usernameET.getText().toString();
            String email = emailET.getText().toString();

            if(username.isEmpty() || email.isEmpty()){
                Toast.makeText(this, "Tên tài khoản hay email không để trống", Toast.LENGTH_SHORT).show();
                return;
            }

            Intent intent = new Intent(ForgotPassActivity.this, ResetPassActivity.class);
            intent.putExtra("username", username);
            intent.putExtra("email", email);
            startActivity(intent);
        });

        cancelBtn.setOnClickListener(view-> finish());
    }

    private void initView(){
        sendCodeBtn = findViewById(R.id.sendCodeBtn);
        cancelBtn = findViewById(R.id.cancelBtn);
        emailET = findViewById(R.id.emailET);
        usernameET = findViewById(R.id.usernameET);
    }
}