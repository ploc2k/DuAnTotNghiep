package com.example.smartorder.adapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.example.smartorder.fragment.EditProfileFragment;
import com.example.smartorder.fragment.admin.menu.MenuDrinkFragment;
import com.example.smartorder.fragment.admin.menu.MenuFoodFragment;
import com.example.smartorder.fragment.profile.ChangePassFragment;
import com.example.smartorder.fragment.profile.EditInfoFragment;
import com.example.smartorder.model.user.User;

public class ProfileAdapter extends FragmentStatePagerAdapter {
    private String listTab[] = {"Cập nhật thông tin", "Đổi mật khẩu"};
    private EditInfoFragment editInfoFragment;
    private ChangePassFragment changePassFragment;
    private User user;

    public ProfileAdapter(@NonNull FragmentManager fm, User user) {
        super(fm);
        this.user = user;
        editInfoFragment = new EditInfoFragment(user);
        changePassFragment = new ChangePassFragment(user);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        if (position == 0) {
            return editInfoFragment;
        } else if (position == 1) {
            return changePassFragment;
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
