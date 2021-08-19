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
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
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
import com.example.smartorder.api.APIModule;
import com.example.smartorder.api.RetrofitAPI;
import com.example.smartorder.constants.Constants;
import com.example.smartorder.fragment.admin.MenuFragment;
import com.example.smartorder.model.callback.CallbackListMenu;
import com.example.smartorder.model.menu.Menu;
import com.example.smartorder.model.response.ServerResponse;
import com.example.smartorder.support.Support;

import java.io.File;
import java.text.Normalizer;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import de.hdodenhof.circleimageview.CircleImageView;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;

public class MenuDrinkFragment extends Fragment {

    private RecyclerView rvListMenuDrink;
    private MenuDrinksAdapter menuDrinksAdapter;
    private RetrofitAPI retrofitAPI;
    private List<Menu> menuListDrink;
    private EditText edtTenMon;
    private EditText edtPrice;
    private EditText edAmonut;
    private EditText edtSearch;
    private CircleImageView imvFood;
    private Spinner spnType;
    private TextView tvAmount;
    private Button btnUpdate, btnCancel;
    private Uri uriImage = null;
    private int REQUEST_CODE_LOAD_IMAGE = 01234;
    GridLayoutManager gridLayoutManager;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_menu_drink, container, false);
        initView(view);
        Log.e("onCreateView: ", "onCreateView");
        retrofitAPI = APIModule.getInstance().create(RetrofitAPI.class);
        menuListDrink = new ArrayList<>();
        rvListDrinks();
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


    private void filter(String s) {
        List<Menu> menuDrinkFilter = new ArrayList<>();
        for (Menu menu : menuListDrink) {
            if (Support.deAccent(menu.getName()).toLowerCase().contains(s.toLowerCase())) {
                menuDrinkFilter.add(menu);
            }
        }
        menuDrinksAdapter.filterList(menuDrinkFilter, getActivity());
        menuDrinksAdapter.notifyDataSetChanged();
    }

    private void initView(View view) {
        rvListMenuDrink = (RecyclerView) view.findViewById(R.id.rvListMenuDrink);
        edtSearch = (EditText) view.findViewById(R.id.edtSearch);
        gridLayoutManager = new GridLayoutManager(getContext(), 2);
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
                    if (type.equals("Drink")) {
                        menuListDrink.add(new Menu(id, name, price, image, type, status));
                        menuDrinksAdapter.notifyDataSetChanged();
                    }
                }
            }

            @Override
            public void onFailure(Call<List<Menu>> call, Throwable t) {
                Toast.makeText(getActivity(), "Lỗi hệ thống" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void rvListDrinks() {
        menuDrinksAdapter = new MenuDrinksAdapter(menuListDrink, getContext(), new MenuDrinksAdapter.OnClickListener() {
            @Override
            public void deleteDrink(Menu menuDrink, String id) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("Bạn có muốn xóa đồ uống " + menuDrink.getName() + " không ?")
                        .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                retrofitAPI.deleteDrink(id).enqueue(new Callback<ServerResponse>() {
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
            public void updateDrink(Menu menuDrink) {
                dialogUpdateDrink(menuDrink);
            }
        });
//        rvListMenuDrink.setLayoutManager(new LinearLayoutManager(getContext()));
        rvListMenuDrink.setLayoutManager(gridLayoutManager);
        rvListMenuDrink.setHasFixedSize(true);
        rvListMenuDrink.setAdapter(menuDrinksAdapter);
    }


    private void dialogUpdateDrink(Menu menuDrink) {
        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        View alert = LayoutInflater.from(getContext()).inflate(R.layout.dialog_update_menu, null);
        alertDialog.setTitle("Cập nhật thông tin món ăn");
        alertDialog.setView(alert);
        alertDialog.setCancelable(false);

        edtTenMon = alert.findViewById(R.id.edtNameFood);
        edtTenMon.setText(String.valueOf(menuDrink.getName()));
        edtPrice = alert.findViewById(R.id.edtPriceFood);
        edtPrice.setText(String.valueOf(menuDrink.getPrice()));
        TextView tvType = alert.findViewById(R.id.tvTypeFood);
        tvType.setText("Loại : " + menuDrink.getType());
        imvFood = alert.findViewById(R.id.imgAvtFood);
        Glide.with(getContext()).load(Constants.LINK + menuDrink.getImage()).into(imvFood);
        RadioButton rdTrue = alert.findViewById(R.id.rdTrue), rdFalse = alert.findViewById(R.id.rdFalse);
        if (menuDrink.getStatus()) {
            rdTrue.setChecked(true);
        } else {
            rdFalse.setChecked(true);
        }
        btnCancel = alert.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        imvFood.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_CODE_LOAD_IMAGE);
            }
        });
        btnUpdate = alert.findViewById(R.id.btnAddFood);
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
                if (!checkValidation(edtTenMon, edtPrice)) {
                } else {
                    if (uriImage != null) {
                        String tenmon = edtTenMon.getText().toString();
                        Integer price = Integer.parseInt(edtPrice.getText().toString());
                        File file = new File(Support.getPathFromUri(getContext(), uriImage));
                        RequestBody requestBody = RequestBody.create(MediaType.parse(
                                getContext().getContentResolver().getType(uriImage)), file);
                        MultipartBody.Part filePart = MultipartBody.Part.createFormData(
                                "avatar", file.getName(), requestBody);
                        retrofitAPI.updateDrink(menuDrink.getId(), tenmon, status, price, filePart).enqueue(new Callback<ServerResponse>() {
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
                                Toast.makeText(getActivity(), "Lỗi hệ thống" + t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        String tenmon = edtTenMon.getText().toString();
                        Integer price = Integer.parseInt(edtPrice.getText().toString());
                        retrofitAPI.updateDrinkNoImage(menuDrink.getId(), tenmon, status, price).enqueue(new Callback<ServerResponse>() {

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
                                Toast.makeText(getActivity(), "Lỗi hệ thống" + t.getMessage(), Toast.LENGTH_SHORT).show();
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            uriImage = uri;
            imvFood.setImageURI(uri);
        }
    }
}