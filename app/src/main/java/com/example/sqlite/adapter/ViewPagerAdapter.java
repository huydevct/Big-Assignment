package com.example.sqlite.adapter;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.sqlite.fragment.FragmentAccount;
import com.example.sqlite.fragment.FragmentHome;
import com.example.sqlite.fragment.FragmentSearch;

public class ViewPagerAdapter extends FragmentStatePagerAdapter {


    public ViewPagerAdapter(@NonNull FragmentManager fm, int behavior) {
        super(fm,behavior);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        switch (position){
            case 1: return new FragmentSearch();
            case 2: return new FragmentAccount();
            default: return new FragmentHome();
        }
    }

    @Override
    public int getCount() {
        return 3;
    }
}
