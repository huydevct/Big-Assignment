package com.example.sqlite;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewTreeObserver;

import com.example.sqlite.adapter.ViewPagerAdapter;
import com.example.sqlite.dal.NeutralSQLiteHelper;
import com.example.sqlite.dal.SQLiteHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView bottomNavigationView;
    private ViewPager viewPager;
    private FloatingActionButton fab;
    private Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        bottomNavigationView = findViewById(R.id.bottom_nav);
        viewPager = findViewById(R.id.viewPager);
        fab = findViewById(R.id.fab);

        fab.setOnClickListener(view -> {
            intent = new Intent(MainActivity.this, AddItemActivity.class);
            startActivity(intent);
        });

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        viewPager.setAdapter(viewPagerAdapter);
        viewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                switch (position) {
                    case 0:
                        bottomNavigationView.getMenu().findItem(R.id.mHome).setChecked(true);
                        fab.show();
                        break;
                    case 1:
                        bottomNavigationView.getMenu().findItem(R.id.mSearch).setChecked(true);
                        fab.show();
                        break;
                    case 2:
                        bottomNavigationView.getMenu().findItem(R.id.mAccount).setChecked(true);
                        fab.hide();
                        break;
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        bottomNavigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.mHome:
                        viewPager.setCurrentItem(0);
                        break;
                    case R.id.mSearch:
                        viewPager.setCurrentItem(1);
                        break;
                    case R.id.mAccount:
                        viewPager.setCurrentItem(2);
                        break;
                }
                return true;
            }
        });
    }
}