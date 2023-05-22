package com.example.sqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sqlite.dal.NeutralSQLiteHelper;

public class ChangePassActivity extends AppCompatActivity {
    private EditText oldPassET, newPassET, reNewPassET;
    private Button confirmBtn, cancelBtn;
    private NeutralSQLiteHelper neutralSQLiteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass);
        initView();

        neutralSQLiteHelper = new NeutralSQLiteHelper(this);
        Intent intent = getIntent();
        String oldPassword = intent.getStringExtra("password");

        confirmBtn.setOnClickListener(view -> {
            String oldPassIn = oldPassET.getText().toString(),
                    newPassIn = newPassET.getText().toString(),
                    reNewPassIn = reNewPassET.getText().toString();

            if (oldPassword.trim().isEmpty() || newPassIn.trim().isEmpty() || reNewPassIn.trim().isEmpty()) {
                Toast.makeText(this, "Các ô nhập không được để trống", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!oldPassword.equals(oldPassIn)) {
                Toast.makeText(this, "Mật khẩu cũ không đúng", Toast.LENGTH_SHORT).show();
                return;
            }
            if (!newPassIn.equals(reNewPassIn)) {
                Toast.makeText(this, "Mật khẩu mới không trùng khớp", Toast.LENGTH_SHORT).show();
                return;
            }
            if (oldPassword.equals(newPassIn)) {
                Toast.makeText(this, "Mật khẩu mới không được trùng lặp mật khẩu cũ", Toast.LENGTH_SHORT).show();
                return;
            }

            neutralSQLiteHelper.setPassword(newPassIn);
            finish();
        });

        cancelBtn.setOnClickListener(view -> finish());
    }

    private void initView() {
        oldPassET = findViewById(R.id.oldPassET);
        newPassET = findViewById(R.id.newPassET);
        reNewPassET = findViewById(R.id.reNewPassET);
        confirmBtn = findViewById(R.id.confirmBtn);
        cancelBtn = findViewById(R.id.cancelBtn);
    }
}