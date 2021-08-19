package com.example.smartorder.fragment.profile;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.smartorder.R;
import com.example.smartorder.api.APIModule;
import com.example.smartorder.api.RetrofitAPI;
import com.example.smartorder.constants.Constants;
import com.example.smartorder.model.response.ServerResponse;
import com.example.smartorder.model.user.User;
import com.example.smartorder.support.Support;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class EditInfoFragment extends Fragment {

    private CircleImageView imgAvatar;
    private CircleImageView imgSelect;
    private TextInputEditText tvName;
    private TextInputEditText tvPhone;
    private TextInputEditText tvAge;
    private TextInputEditText tvAddress;
    private Button btnSaveChange;
    private ImageButton btnBack;
    private Uri mUriImage = null;
    private final int REQUEST_CODE_LOAD_IMAGE = 1;
    private User user;
    private RetrofitAPI retrofitAPI;

    public EditInfoFragment(User user) {
        this.user = user;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_edit_info, container, false);
        initView(view);
        retrofitAPI = APIModule.getInstance().create(RetrofitAPI.class);
        Glide.with(getContext()).load(Constants.LINK + user.getAvatar()).into(imgAvatar);
        tvName.setText(user.getFullName());
        tvAddress.setText(user.getAddress());
        tvAge.setText(user.getAge() + "");
        tvPhone.setText(user.getPhone());
        imgSelect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                selectImage();
            }
        });
        btnSaveChange.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                updateInfo();
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment editProfileFragment = getActivity().getSupportFragmentManager().findFragmentByTag(Constants.fragmentEditProfile);
                if (editProfileFragment != null) {
                    getActivity().getSupportFragmentManager().beginTransaction()
                            .setCustomAnimations(R.anim.admin_fragment_main_translate_enter_left_to_right,
                                    R.anim.admin_fragment_main_translate_exit_left_to_right)
                            .remove(editProfileFragment)
                            .commit();
                }
            }
        });
        return view;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private void updateInfo() {
        if (!checkValidate(tvName, tvAge, tvPhone, tvAddress)) {
            Toast.makeText(getContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
        } else if (!(tvPhone.getText().toString().length() == 10)) {
            Toast.makeText(getContext(), "SDT có 10 chữ số. Vui lòng nhập lại", Toast.LENGTH_SHORT).show();
        } else {
            String name = tvName.getText().toString().trim();
            String phone = tvPhone.getText().toString().trim();
            String address = tvAddress.getText().toString().trim();
            Integer age = Integer.parseInt(tvAge.getText().toString().trim());
            if (mUriImage == null) {
                retrofitAPI.updateInfoNoImage(Constants.idLogin, name, phone, age, address).enqueue(new Callback<ServerResponse>() {
                    @Override
                    public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                        if (response.code() == 200) {
                            Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            Fragment editProfileFragment = getActivity().getSupportFragmentManager().findFragmentByTag(Constants.fragmentEditProfile);
                            if (editProfileFragment != null) {
                                getActivity().getSupportFragmentManager().beginTransaction()
                                        .setCustomAnimations(R.anim.admin_fragment_main_translate_enter_left_to_right,
                                                R.anim.admin_fragment_main_translate_exit_left_to_right)
                                        .remove(editProfileFragment)
                                        .commit();
                                Fragment profileFrament = getActivity().getSupportFragmentManager().findFragmentByTag(Constants.fragmentProfile);
                                if (profileFrament != null) {
                                    getFragmentManager().beginTransaction().detach(profileFrament).attach(profileFrament).commit();
                                }

                            }
                        } else {
                            Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ServerResponse> call, Throwable t) {
                        Toast.makeText(getActivity(), "Lỗi hệ thống" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            } else {
                File file = new File(Support.getPathFromUri(getContext(), mUriImage));
                RequestBody requestBody = RequestBody.create(MediaType.parse(
                        getContext().getContentResolver().getType(mUriImage)), file);
                MultipartBody.Part filePart = MultipartBody.Part.createFormData(
                        "avatar", file.getName(), requestBody);
                retrofitAPI.updateInfo(Constants.idLogin, name, phone, age, address, filePart).enqueue(new Callback<ServerResponse>() {
                    @Override
                    public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                        if (response.code() == 200) {
                            Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag(Constants.fragmentEditProfile);
                            if (fragment != null) {
                                getActivity().getSupportFragmentManager().beginTransaction()
                                        .setCustomAnimations(R.anim.admin_fragment_main_translate_enter_left_to_right,
                                                R.anim.admin_fragment_main_translate_exit_left_to_right)
                                        .remove(fragment)
                                        .commit();
                                Fragment profileFrament = getActivity().getSupportFragmentManager().findFragmentByTag(Constants.fragmentProfile);
                                if (profileFrament != null) {
                                    getFragmentManager().beginTransaction().detach(profileFrament).attach(profileFrament).commit();
                                }

                            }
                        } else {
                            Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ServerResponse> call, Throwable t) {
                        Toast.makeText(getActivity(), "Lỗi hệ thống" + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    }

    private void selectImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_CODE_LOAD_IMAGE);
    }

    private void initView(View view) {
        imgAvatar = (CircleImageView) view.findViewById(R.id.imgAvatar);
        imgSelect = (CircleImageView) view.findViewById(R.id.imgSelect);
        tvName = (TextInputEditText) view.findViewById(R.id.tvName);
        tvPhone = (TextInputEditText) view.findViewById(R.id.tvPhone);
        tvAge = (TextInputEditText) view.findViewById(R.id.tvAge);
        tvAddress = (TextInputEditText) view.findViewById(R.id.tvAddress);
        btnSaveChange = (Button) view.findViewById(R.id.btnSaveChange);
        btnBack = (ImageButton) view.findViewById(R.id.btnBack);
    }

    private boolean checkValidate(TextInputEditText tvName, TextInputEditText tvAge,
                                  TextInputEditText tvPhone, TextInputEditText tvAddress) {

        if (tvName.getText().toString().trim().isEmpty()) {
            tvName.setError("Chưa nhập họ tên");
            return false;
        } else if (tvPhone.getText().toString().trim().isEmpty()) {
            tvPhone.setError("Chưa nhập số điện thoại");
            return false;
        } else if (tvAge.getText().toString().trim().isEmpty()) {
            tvAge.setError("Chưa nhập tuổi");
            return false;
        } else if (tvAddress.getText().toString().trim().isEmpty()) {
            tvAddress.setError("Chưa nhập địa chỉ");
            return false;
        }
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            mUriImage = uri;
            imgAvatar.setImageURI(uri);
        }
    }
}