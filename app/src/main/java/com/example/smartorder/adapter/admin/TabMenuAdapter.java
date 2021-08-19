package com.example.smartorder.adapter.admin;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.smartorder.fragment.admin.menu.MenuDrinkFragment;
import com.example.smartorder.fragment.admin.menu.MenuFoodFragment;
import com.example.smartorder.fragment.admin.menu.MenuOtherFragment;

import java.util.ArrayList;
import java.util.List;

public class TabMenuAdapter extends FragmentStatePagerAdapter {
    private String listTab[] = {"Đồ ăn", "Đồ uống", "Khác"};
    List<Fragment> list = new ArrayList<>();

    public TabMenuAdapter(@NonNull FragmentManager fm) {
        super(fm);
        list.add(new MenuFoodFragment());
        list.add(new MenuDrinkFragment());
        list.add(new MenuOtherFragment());
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return list.get(position);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return listTab[position];
    }

}
