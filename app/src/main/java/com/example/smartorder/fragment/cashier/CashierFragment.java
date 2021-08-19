package com.example.smartorder.fragment.cashier;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.bumptech.glide.Glide;
import com.example.smartorder.R;
import com.example.smartorder.activity.LoginActivity;
import com.example.smartorder.adapter.admin.TabBillCashierAdapter;
import com.example.smartorder.adapter.admin.TabMenuAdapter;
import com.example.smartorder.api.RetrofitAPI;
import com.example.smartorder.constants.Constants;
import com.example.smartorder.fragment.ProfileFragment;
import com.google.android.material.tabs.TabLayout;
import com.melnykov.fab.FloatingActionButton;

import de.hdodenhof.circleimageview.CircleImageView;

public class CashierFragment extends Fragment {
    private TabLayout tabMenu;
    private ViewPager vpMenu;
    private CircleImageView imgProfile;
    private androidx.appcompat.widget.Toolbar toolbar;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_cashier, container, false);
        initView(view);
        Glide.with(getActivity()).load(Constants.LINK + Constants.AvatarUser).into(imgProfile);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Bill");
        vpMenu.setAdapter(new TabBillCashierAdapter(getActivity().getSupportFragmentManager()));
        tabMenu.setupWithViewPager(vpMenu);
        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCustomPopupMenu(view);
            }
        });
        return view;
    }

    private void initView(View view) {
        tabMenu = (TabLayout) view.findViewById(R.id.tabMenu);
        vpMenu = (ViewPager) view.findViewById(R.id.vpMenu);
        toolbar = view.findViewById(R.id.toolbar);
        imgProfile = (CircleImageView) view.findViewById(R.id.imgProfile);
    }

    private void showCustomPopupMenu(View view) {
        PopupMenu popupMenu = new PopupMenu(getActivity(), view);
        popupMenu.inflate(R.menu.my_menu);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.profile:
                        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
                        ProfileFragment profileFragment = new ProfileFragment();
                        transaction.setCustomAnimations(R.anim.list_food_bottom_to_top, 0).add(R.id.frq, profileFragment, Constants.fragmentProfile).commit();
                        break;
                    case R.id.logOut:
                        Intent intent = new Intent(getActivity(), LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        ActivityCompat.finishAffinity(getActivity());
                        getActivity().finish();
                        break;
                }
                return false;
            }
        });
        popupMenu.show();
    }

}