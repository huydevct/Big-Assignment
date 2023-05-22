package com.example.sqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.sqlite.dal.NeutralSQLiteHelper;
import com.example.sqlite.model.Account;

import java.util.regex.Pattern;

public class RegisterActivity extends AppCompatActivity {
    private Button registerBtn, cancelBtn, hidePasswordBtn, hideRePasswordBtn;
    private EditText usernameET, passwordET, re_passwordET, fullNameET, emailET;
    private String username, password, email, fullName;
    private NeutralSQLiteHelper neutralSQLiteHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
        neutralSQLiteHelper = new NeutralSQLiteHelper(this);

        registerBtn.setOnClickListener(view -> {
            username = usernameET.getText().toString();
            password = passwordET.getText().toString();
            email = emailET.getText().toString();
            fullName = fullNameET.getText().toString();

            if (!validateInputs()) {
                return;
            }

            if (!neutralSQLiteHelper.register(new Account(username, password, fullName, email))) {
                Toast.makeText(this, "Đăng ký thất bại, vui lòng thử lại bằng tên tài khoản khác", Toast.LENGTH_SHORT).show();
                return;
            }

            Toast.makeText(this, "Đăng ký thành công", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(RegisterActivity.this, LoginActivity.class);
            startActivity(intent);
        });

        hidePasswordBtn.setOnClickListener(view -> toggleDisplayPassword(passwordET));

        hideRePasswordBtn.setOnClickListener(view -> toggleDisplayPassword(re_passwordET));

        cancelBtn.setOnClickListener(view -> finish());
    }

    private void toggleDisplayPassword(EditText passET) {
        if (passET.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())) {
            passET.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        } else {
            passET.setTransformationMethod(PasswordTransformationMethod.getInstance());
        }
    }

    private boolean validateInputs() {
        if (username.trim().length() == 0 || password.length() == 0) {
            Toast.makeText(this, "Tên tài khoản hoặc mật khẩu không để trống", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (fullName.trim().length() == 0) {
            Toast.makeText(this, "Họ tên không để trống", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (email.trim().length() == 0 || !Pattern.compile("^(.+)@(\\S+)$").matcher(email).matches()) {
            Toast.makeText(this, "Email không hợp lệ hoặc không để trống", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (!passwordET.getText().toString().equals(re_passwordET.getText().toString())) {
            Toast.makeText(this, "Mật khẩu không khớp", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    private void initView() {
        registerBtn = findViewById(R.id.registerBtn);
        cancelBtn = findViewById(R.id.cancelBtn);
        hidePasswordBtn = findViewById(R.id.hidePasswordBtn);
        hideRePasswordBtn = findViewById(R.id.hideRePasswordBtn);
        usernameET = findViewById(R.id.usernameET);
        re_passwordET = findViewById(R.id.re_passwordET);
        fullNameET = findViewById(R.id.fullNameET);
        passwordET = findViewById(R.id.passwordET);
        emailET = findViewById(R.id.emailET);
    }
}