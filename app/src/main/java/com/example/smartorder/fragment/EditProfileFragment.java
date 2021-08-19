package com.example.smartorder.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.example.smartorder.R;
import com.example.smartorder.adapter.ProfileAdapter;
import com.example.smartorder.adapter.admin.TabMenuAdapter;
import com.example.smartorder.model.callback.CallBackInfoUser;
import com.example.smartorder.model.user.User;
import com.google.android.material.tabs.TabLayout;

public class EditProfileFragment extends Fragment implements CallBackInfoUser {

    private TabLayout tabEditProfile;
    private ViewPager vpEditPofile;
    private User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_profile, container, false);
        initView(view);
        vpEditPofile.setAdapter(new ProfileAdapter(getFragmentManager(), user));
        tabEditProfile.setupWithViewPager(vpEditPofile);
        return view;
    }

    private void initView(View view) {
        tabEditProfile = (TabLayout) view.findViewById(R.id.tabEditProfile);
        vpEditPofile = (ViewPager) view.findViewById(R.id.vpEditPofile);
    }

    @Override
    public void getInfoUser(User user) {
        this.user = user;
    }
}