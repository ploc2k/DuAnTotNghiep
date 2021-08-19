package com.example.smartorder.adapter.admin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.smartorder.fragment.admin.BillFragment;
import com.example.smartorder.fragment.admin.menu.MenuFoodFragment;
import com.example.smartorder.fragment.cashier.BillUnpaidFragment;

public class TabBillCashierAdapter extends FragmentStatePagerAdapter {
    private String listTab[] = {"Thanh toán", "Đã thanh toán"};
    private BillUnpaidFragment billUnpaidFragment;
    private BillFragment billFragment;

    public TabBillCashierAdapter(@NonNull FragmentManager fm) {
        super(fm);
        billUnpaidFragment = new BillUnpaidFragment();
        billFragment = new BillFragment();
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return billUnpaidFragment;
        } else if (position == 1) {
            return billFragment;
        } else {
            return null;
        }
    }

    @Override
    public int getCount() {
        return listTab.length;
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return listTab[position];
    }
}
