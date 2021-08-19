package com.example.smartorder.fragment.admin;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.example.smartorder.R;
import com.example.smartorder.adapter.admin.TabMenuAdapter;
import com.example.smartorder.api.APIModule;
import com.example.smartorder.api.RetrofitAPI;
import com.example.smartorder.constants.Constants;
import com.example.smartorder.fragment.admin.menu.MenuDrinkFragment;
import com.example.smartorder.fragment.admin.menu.MenuFoodFragment;
import com.example.smartorder.fragment.admin.menu.MenuOtherFragment;
import com.example.smartorder.model.callback.CallbackListMenu;
import com.example.smartorder.model.menu.Menu;
import com.example.smartorder.model.response.ServerResponse;
import com.example.smartorder.support.Support;
import com.google.android.material.tabs.TabLayout;
import com.melnykov.fab.FloatingActionButton;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class MenuFragment extends Fragment {

    private RetrofitAPI retrofitAPI;
    private TabLayout tabMenu;
    private ViewPager vpMenu;
    private FloatingActionButton fabAddMenu;
    private ImageView imvFood;
    private String type;
    private Uri uriImage = null;
    private int REQUEST_CODE_LOAD_IMAGE = 01234;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu, container, false);
        initView(view);
        retrofitAPI = APIModule.getInstance().create(RetrofitAPI.class);
        vpMenu.setAdapter(new TabMenuAdapter(getActivity().getSupportFragmentManager()));
        vpMenu.setOffscreenPageLimit(3);
        tabMenu.setupWithViewPager(vpMenu);
        fabAddMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogAddMenu();
            }
        });
        return view;
    }


    private void dialogAddMenu() {
        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        View alert = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_menu, null);
        alertDialog.setView(alert);
        alertDialog.setTitle("Thêm mới món ăn");
        alertDialog.setCancelable(false);
        EditText edtTenMon = alert.findViewById(R.id.edtNameFood);
        EditText edtPrice = alert.findViewById(R.id.edtPriceFood);
        imvFood = alert.findViewById(R.id.imgAvtFood);
        RadioGroup rdgType = alert.findViewById(R.id.rdgType);
        RadioButton rdFood = alert.findViewById(R.id.rdFood);
        RadioButton rdDrink = alert.findViewById(R.id.rdDrink);
        RadioButton rdOther = alert.findViewById(R.id.rdOther);
        Button btnAdd = alert.findViewById(R.id.btnAddFood);
        Button btnCancel = alert.findViewById(R.id.btnCancel);

        imvFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"),
                        REQUEST_CODE_LOAD_IMAGE);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {

                if (!checkValidation(edtTenMon, edtPrice)) {
                } else if (uriImage == null) {
                    imvFood.setBackgroundColor(Color.RED);
                    Toast.makeText(getContext(), "Bạn chưa chọn ảnh", Toast.LENGTH_SHORT).show();
                } else if (rdgType.getCheckedRadioButtonId() == -1) {
                    rdDrink.setTextColor(Color.RED);
                    rdFood.setTextColor(Color.RED);
                    rdOther.setTextColor(Color.RED);
                    Toast.makeText(getActivity(), "Bạn chưa chọn loại đồ ăn", Toast.LENGTH_SHORT).show();
                } else {
                    if (rdDrink.isChecked()) {
                        type = "Drink";
                    } else if (rdFood.isChecked()) {
                        type = "Food";
                    } else if (rdOther.isChecked()) {
                        type = "Other";
                    }
                    String tenmon = edtTenMon.getText().toString();
                    Integer price = Integer.parseInt(edtPrice.getText().toString());
                    File file = new File(Support.getPathFromUri(getContext(), uriImage));
                    RequestBody requestBody = RequestBody.create(MediaType.parse(
                            getContext().getContentResolver().getType(uriImage)), file);
                    MultipartBody.Part filePart = MultipartBody.Part.createFormData(
                            "avatar", file.getName(), requestBody);
                    retrofitAPI.createFood(tenmon, price, type, filePart).enqueue(new Callback<ServerResponse>() {
                        @Override
                        public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                            if (response.code() == 200) {
                                Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                alertDialog.dismiss();
                                Fragment menuFragment = getActivity().getSupportFragmentManager().findFragmentByTag(Constants.fragmentMenu);
                                if (menuFragment != null) {
                                    getActivity().getSupportFragmentManager().beginTransaction().remove(menuFragment).replace(R.id.frm, new MenuFragment(), Constants.fragmentMenu).commit();
                                }
                            } else {
                                Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            }

                        }

                        @Override
                        public void onFailure(Call<ServerResponse> call, Throwable t) {
                            Toast.makeText(getActivity(), "Lỗi hệ thống " + t.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
        alertDialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            uriImage = uri;
            imvFood.setImageURI(uri);
            imvFood.setBackgroundColor(Color.WHITE);
        }
    }

    private void initView(View view) {
        tabMenu = (TabLayout) view.findViewById(R.id.tabMenu);
        vpMenu = (ViewPager) view.findViewById(R.id.vpMenu);
        fabAddMenu = (FloatingActionButton) view.findViewById(R.id.fabAddMenu);
    }

    private boolean checkValidation(EditText edtMonAn, EditText edtPrice) {
        if (edtMonAn.getText().toString().equals("")) {
            edtMonAn.setError("Chưa nhập tên");
            return false;
        } else if (edtPrice.getText().toString().equals("")) {
            edtPrice.setError("Chưa nhập giá");
            return false;
        }

        return true;
    }
}