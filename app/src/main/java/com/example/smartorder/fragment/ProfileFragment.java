package com.example.smartorder.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.bumptech.glide.Glide;
import com.example.smartorder.R;
import com.example.smartorder.api.APIModule;
import com.example.smartorder.api.RetrofitAPI;
import com.example.smartorder.constants.Constants;
import com.example.smartorder.model.callback.CallBackInfoUser;
import com.example.smartorder.model.user.User;

import de.hdodenhof.circleimageview.CircleImageView;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ProfileFragment extends Fragment {

    private RetrofitAPI retrofitAPI;
    private CircleImageView imgAvatar;
    private TextView tvNameUser;
    private TextView tvPhone;
    private ImageButton btnEditProfile, imgDown;
    private TextView tvSName;
    private TextView tvSPhone;
    private TextView tvSAge;
    private TextView tvSAddress;
    private TextView tvSCMND;
    private CallBackInfoUser callBackInfoUser;
    private User user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile_, container, false);
        initView(view);
        getDataFromServer();
        btnEditProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                EditProfileFragment editProfileFragment = new EditProfileFragment();
                callBackInfoUser = editProfileFragment;
                callBackInfoUser.getInfoUser(user);
                transaction.setCustomAnimations(R.anim.admin_fragment_main_translate_enter_right_to_left, R.anim.admin_fragment_main_translate_exit_right_to_left).add(R.id.frq, editProfileFragment, Constants.fragmentEditProfile).commit();
            }
        });
        imgDown.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentTransaction transaction = getFragmentManager().beginTransaction();
                transaction.setCustomAnimations(0, R.anim.list_food_top_to_bottom).remove(ProfileFragment.this).commit();

            }
        });
        return view;
    }

    private void getDataFromServer() {
        retrofitAPI = APIModule.getInstance().create(RetrofitAPI.class);
        retrofitAPI.getInfoUser(Constants.idLogin).enqueue(new Callback<User>() {
            @Override
            public void onResponse(Call<User> call, Response<User> response) {
                user = response.body();
                Glide.with(getContext()).load(Constants.LINK + user.getAvatar()).into(imgAvatar);
                tvNameUser.setText(user.getFullName());
                tvSName.setText(user.getFullName());
                tvPhone.setText(user.getPhone());
                tvSPhone.setText(user.getPhone());
                tvSAddress.setText(user.getAddress());
                tvSAge.setText(user.getAge() + "");
                tvSCMND.setText(user.getIndentityCardNumber() + "");
            }

            @Override
            public void onFailure(Call<User> call, Throwable t) {
                Toast.makeText(getActivity(), "Lỗi hệ thống " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initView(View view) {
        imgAvatar = (CircleImageView) view.findViewById(R.id.imgAvatar);
        tvNameUser = (TextView) view.findViewById(R.id.tvNameUser);
        tvPhone = (TextView) view.findViewById(R.id.tvPhone);
        btnEditProfile = (ImageButton) view.findViewById(R.id.btnEditProfile);
        imgDown = (ImageButton) view.findViewById(R.id.imgDown);
        tvSName = (TextView) view.findViewById(R.id.tvSName);
        tvSPhone = (TextView) view.findViewById(R.id.tvSPhone);
        tvSAge = (TextView) view.findViewById(R.id.tvSAge);
        tvSAddress = (TextView) view.findViewById(R.id.tvSAddress);
        tvSCMND = (TextView) view.findViewById(R.id.tvSCMND);
    }
}