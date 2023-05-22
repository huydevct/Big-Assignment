package com.example.sqlite;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.sqlite.dal.NeutralSQLiteHelper;
import com.example.sqlite.dal.SQLiteHelper;

public class LoginActivity extends AppCompatActivity {
    private Button loginBtn, registerBtn, hidePasswordBtn, forgotPassBtn;
    private EditText usernameET, passwordET;
    private NeutralSQLiteHelper neutralSQLiteHelper;
    SQLiteHelper sqLiteHelper;
    private Intent loginIntent, regIntent;
    private CheckBox checkboxKeepLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        loginIntent = new Intent(LoginActivity.this, MainActivity.class);

        neutralSQLiteHelper = new NeutralSQLiteHelper(this);
        String key_username = neutralSQLiteHelper.getKeyAccount();
        if (!key_username.equals("")) {
            sqLiteHelper = new SQLiteHelper(this, key_username);
            loginIntent.putExtra("username", key_username);
            startActivity(loginIntent);
            finish();
        } else {
            initView();

            loginBtn.setOnClickListener(view -> {
                String username = usernameET.getText().toString(), password = passwordET.getText().toString();

                if (username.trim().length() == 0 || password.length() == 0) {
                    Toast.makeText(this, "Tên tài khoản hoặc mật khẩu không để trống", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (!neutralSQLiteHelper.login(username, password)) {
                    Toast.makeText(this, "Tên tài khoản hoặc mật khẩu không đúng, vui lòng thử lại", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (checkboxKeepLogin.isChecked()) {
                    neutralSQLiteHelper.setKeyAccount(username, password);
                }

                sqLiteHelper = new SQLiteHelper(this, username);

                loginIntent.putExtra("username", username);
                startActivity(loginIntent);
                Toast.makeText(this, "Đăng nhập thành công", Toast.LENGTH_LONG).show();
                finish();
            });

            registerBtn.setOnClickListener(view -> {
                regIntent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(regIntent);
            });

            hidePasswordBtn.setOnClickListener(view -> {
                if(passwordET.getTransformationMethod().equals(PasswordTransformationMethod.getInstance())){
                    passwordET.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                }
                else{
                    passwordET.setTransformationMethod(PasswordTransformationMethod.getInstance());
                }
            });

            forgotPassBtn.setOnClickListener(view->{
                startActivity(new Intent(LoginActivity.this, ForgotPassActivity.class));
            });
        }
    }

    private void initView() {
        loginBtn = findViewById(R.id.loginBtn);
        registerBtn = findViewById(R.id.registerBtn);
        hidePasswordBtn = findViewById(R.id.hidePasswordBtn);
        forgotPassBtn  = findViewById(R.id.forgotPassBtn);
        usernameET = findViewById(R.id.usernameET);
        passwordET = findViewById(R.id.passwordET);
        checkboxKeepLogin = findViewById(R.id.checkboxKeepLogin);
    }
}