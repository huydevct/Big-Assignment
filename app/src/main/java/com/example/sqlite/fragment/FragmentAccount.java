package com.example.sqlite.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.sqlite.ChangePassActivity;
import com.example.sqlite.LoginActivity;
import com.example.sqlite.MainActivity;
import com.example.sqlite.R;
import com.example.sqlite.dal.NeutralSQLiteHelper;
import com.example.sqlite.dal.SQLiteHelper;
import com.example.sqlite.model.Account;
import com.example.sqlite.model.Item;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;
import java.util.Objects;

public class FragmentAccount extends Fragment {
    private TextView usernameTV, fullNameTV, emailTV;
    private Button changePassBtn, logOutBtn;
    private NeutralSQLiteHelper neutralSQLiteHelper;
    private Intent intent;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initView(view);

        neutralSQLiteHelper = new NeutralSQLiteHelper(getContext());

        Account account = neutralSQLiteHelper.getAccount();
        usernameTV.setText("Tên tài khoản: " + account.getUsername());
        fullNameTV.setText("Họ tên: " + account.getUsername());
        emailTV.setText("Email: " + account.getEmail());

        changePassBtn.setOnClickListener(View->{
            intent = new Intent(getActivity(), ChangePassActivity.class);
            intent.putExtra("password",account.getPassword());
            startActivity(intent);
        });

        logOutBtn.setOnClickListener(View -> {
            neutralSQLiteHelper.setKeyAccount("default", "default");
            intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
            requireActivity().onBackPressed();
        });
    }

    private void initView(View view){
        usernameTV=view.findViewById(R.id.usernameTV);
        fullNameTV=view.findViewById(R.id.fullNameTV);
        emailTV=view.findViewById(R.id.emailTV);
        changePassBtn=view.findViewById(R.id.changePassBtn);
        logOutBtn= view.findViewById(R.id.logOutBtn);
    }
}
