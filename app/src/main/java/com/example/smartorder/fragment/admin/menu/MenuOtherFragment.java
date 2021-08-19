package com.example.smartorder.fragment.admin.menu;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.smartorder.R;
import com.example.smartorder.adapter.admin.MenuDrinksAdapter;
import com.example.smartorder.adapter.admin.MenuOtherAdapter;
import com.example.smartorder.api.APIModule;
import com.example.smartorder.api.RetrofitAPI;
import com.example.smartorder.constants.Constants;
import com.example.smartorder.fragment.admin.MenuFragment;
import com.example.smartorder.fragment.admin.UserFragment;
import com.example.smartorder.model.callback.CallbackListMenu;
import com.example.smartorder.model.menu.Menu;
import com.example.smartorder.model.response.ServerResponse;
import com.example.smartorder.support.Support;

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

public class MenuOtherFragment extends Fragment {
    private List<Menu> menuListOther;
    private MenuOtherAdapter menuOtherAdapter;
    private RecyclerView rvList;
    private EditText edtSearch;
    private RetrofitAPI retrofitAPI;
    private int REQUEST_CODE_LOAD_IMAGE = 01234;
    private Uri uriImage = null;
    private ImageView imgFood;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu_other, container, false);
        initView(view);
        retrofitAPI = APIModule.getInstance().create(RetrofitAPI.class);
        menuListOther = new ArrayList<>();
        initRecycle();
        getAllMenuFromServer();
        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                filter(s.toString());
            }
        });
        return view;
    }

    private void initView(View view) {
        rvList = view.findViewById(R.id.rvList);
        edtSearch = (EditText) view.findViewById(R.id.edtSearch);
    }

    private void getAllMenuFromServer() {
        retrofitAPI.getAllMenu().enqueue(new Callback<List<Menu>>() {
            @Override
            public void onResponse(Call<List<Menu>> call, Response<List<Menu>> response) {
                List<Menu> menus = response.body();
                for (int i = 0; i < menus.size(); i++) {
                    String id = menus.get(i).getId();
                    String name = menus.get(i).getName();
                    Integer price = menus.get(i).getPrice();
                    String image = menus.get(i).getImage();
                    String type = menus.get(i).getType();
                    boolean status = menus.get(i).getStatus();
                    if (type.equals("Other")) {
                        menuListOther.add(new Menu(id, name, price, image, type, status));
                        menuOtherAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Menu>> call, Throwable t) {
                Toast.makeText(getActivity(), "Lỗi hệ thống" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void filter(String s) {
        List<Menu> menuOtherFilter = new ArrayList<>();
        for (Menu menu : menuListOther) {
            if (Support.deAccent(menu.getName()).toLowerCase().contains(s.toLowerCase())) {
                menuOtherFilter.add(menu);
            }
        }
        menuOtherAdapter.filterList(menuOtherFilter, getActivity());
        menuOtherAdapter.notifyDataSetChanged();
    }

    private void initRecycle() {
        menuOtherAdapter = new MenuOtherAdapter(menuListOther, getActivity(), new MenuOtherAdapter.OnClickListener() {
            @Override
            public void deleteOther(Menu menuOther, String id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Bạn có muốn xóa món ăn" + menuOther.getName() + " không?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                retrofitAPI.deleteOther(id).enqueue(new Callback<ServerResponse>() {
                                    @Override
                                    public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                                        Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                        Fragment menuFragment = getActivity().getSupportFragmentManager().findFragmentByTag(Constants.fragmentMenu);
                                        if (menuFragment != null) {
                                            getActivity().getSupportFragmentManager().beginTransaction().remove(menuFragment).replace(R.id.frm, new MenuFragment(), Constants.fragmentMenu).commit();
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ServerResponse> call, Throwable t) {
                                        Toast.makeText(getActivity(), "Lỗi hệ thống " + t.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).create().show();
            }

            @Override
            public void updateOther(Menu menuOther) {

                dialogUpdateOthers(menuOther);
            }
        });
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rvList.setLayoutManager(linearLayoutManager);
        rvList.setHasFixedSize(true);
        rvList.setAdapter(menuOtherAdapter);
        menuOtherAdapter.notifyDataSetChanged();
    }

    private void dialogUpdateOthers(Menu menuOther) {
        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        View alert = LayoutInflater.from(getContext()).inflate(R.layout.dialog_update_menu, null);
        alertDialog.setView(alert);
        alertDialog.setTitle("Chỉnh sửa thông tin đồ dùng khác");
        alertDialog.setCancelable(false);

        EditText edtName = alert.findViewById(R.id.edtNameFood);
        edtName.setText(String.valueOf(menuOther.getName()));
        EditText edtPrice = alert.findViewById(R.id.edtPriceFood);
        edtPrice.setText(String.valueOf(menuOther.getPrice()));
        TextView tvType = alert.findViewById(R.id.tvTypeFood);
        tvType.setText("Loại : " + menuOther.getType());
        RadioButton rdTrue = alert.findViewById(R.id.rdTrue), rdFalse = alert.findViewById(R.id.rdFalse);
        if (menuOther.getStatus()) {
            rdTrue.setChecked(true);
        } else {
            rdFalse.setChecked(true);
        }
        imgFood = alert.findViewById(R.id.imgAvtFood);
        Glide.with(getContext()).load(Constants.LINK + menuOther.getImage()).into(imgFood);

        Button btnCancel = alert.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        imgFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_CODE_LOAD_IMAGE);
            }
        });

        Button btnUpdate = alert.findViewById(R.id.btnAddFood);
        btnUpdate.setText("Chỉnh sửa");
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                boolean status = true;
                if (rdTrue.isChecked()) {
                    status = true;
                } else if (rdFalse.isChecked()) {
                    status = false;
                }
                if (!checkValidation(edtName, edtPrice)) {
                } else {
                    if (uriImage != null) {

                        String tenmon = edtName.getText().toString();
                        Integer price = Integer.parseInt(edtPrice.getText().toString());
                        File file = new File(Support.getPathFromUri(getContext(), uriImage));
                        RequestBody requestBody = RequestBody.create(MediaType.parse(
                                getContext().getContentResolver().getType(uriImage)), file);
                        MultipartBody.Part filePart = MultipartBody.Part.createFormData(
                                "avatar", file.getName(), requestBody);
                        retrofitAPI.updateOther(menuOther.getId(), tenmon, price, status, filePart).enqueue(new Callback<ServerResponse>() {
                            @Override
                            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                                Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                alertDialog.dismiss();
                                Fragment menuFragment = getActivity().getSupportFragmentManager().findFragmentByTag(Constants.fragmentMenu);
                                if (menuFragment != null) {
                                    getActivity().getSupportFragmentManager().beginTransaction().remove(menuFragment).replace(R.id.frm, new MenuFragment(), Constants.fragmentMenu).commit();
                                }
                            }

                            @Override
                            public void onFailure(Call<ServerResponse> call, Throwable t) {
                                Toast.makeText(getActivity(), "Lỗi hệ thống " + t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        String tenmon = edtName.getText().toString();
                        Integer price = Integer.parseInt(edtPrice.getText().toString());
                        retrofitAPI.updateOtherNoImage(menuOther.getId(), tenmon, status, price).enqueue(new Callback<ServerResponse>() {
                            @Override
                            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                                Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                alertDialog.dismiss();
                                Fragment menuFragment = getActivity().getSupportFragmentManager().findFragmentByTag(Constants.fragmentMenu);
                                if (menuFragment != null) {
                                    getActivity().getSupportFragmentManager().beginTransaction().remove(menuFragment).replace(R.id.frm, new MenuFragment(), Constants.fragmentMenu).commit();
                                }
                            }

                            @Override
                            public void onFailure(Call<ServerResponse> call, Throwable t) {
                                Toast.makeText(getActivity(), "Lỗi hệ thống " + t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }
            }
        });
        alertDialog.show();
    }

    private boolean checkValidation(EditText edtMonAn, EditText edtPrice) {
        if (edtMonAn.getText().toString().equals("")) {
            edtMonAn.setError("Chưa nhập tên");
            return false;
        } else if (edtPrice.getText().toString().isEmpty()) {
            edtPrice.setError("Chưa nhập giá");
            return false;
        }
        return true;
    }

    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            uriImage = uri;
            imgFood.setImageURI(uri);
        }
    }

}