package com.example.smartorder.fragment.admin;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.smartorder.R;
import com.example.smartorder.adapter.admin.UserAdapter;
import com.example.smartorder.api.APIModule;
import com.example.smartorder.api.RetrofitAPI;
import com.example.smartorder.constants.Constants;
import com.example.smartorder.model.menu.Menu;
import com.example.smartorder.model.response.ServerResponse;
import com.example.smartorder.model.user.User;
import com.example.smartorder.support.Support;
import com.melnykov.fab.FloatingActionButton;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static android.app.Activity.RESULT_OK;


public class UserFragment extends Fragment {
    private RecyclerView rvListUser;
    private List<User> userList;
    private UserAdapter userAdapter;
    private RetrofitAPI retrofitAPI;
    private FloatingActionButton fabAddStaff;
    private final int REQUEST_CODE_LOAD_IMAGE = 1;
    private Uri mUriImage = null;
    private CircleImageView imgAvatar;
    private String role = "";
    private EditText edtSearch;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);
        initView(view);
        retrofitAPI = APIModule.getInstance().create(RetrofitAPI.class);
        initRecycleView();
        getAllUser();
        fabAddStaff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialogAddStaff();
            }
        });
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

    private void getAllUser() {
        retrofitAPI.getAllUser().enqueue(new Callback<List<User>>() {
            @Override
            public void onResponse(Call<List<User>> call, Response<List<User>> response) {
                List<User> users = response.body();
                for (int i = 0; i < users.size(); i++) {
                    String id = users.get(i).getId();
                    String passWord = users.get(i).getPassWord();
                    String role = users.get(i).getRole();
                    String fullName = users.get(i).getFullName();
                    String phone = users.get(i).getPhone();
                    String address = users.get(i).getAddress();
                    Integer age = users.get(i).getAge();
                    String avatar = users.get(i).getAvatar();
                    Integer indentityCardNumber = users.get(i).getIndentityCardNumber();
                    userList.add(new User(id, passWord, role, fullName, phone, address, age, avatar, indentityCardNumber));
                    userAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<User>> call, Throwable t) {
                Toast.makeText(getActivity(), "Lỗi hệ thống " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initRecycleView() {
        userList = new ArrayList<>();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        rvListUser.setLayoutManager(linearLayoutManager);
        userAdapter = new UserAdapter(getContext(), userList, new UserAdapter.OnClickListener() {
            @Override
            public void deleteUser(int position, String id) {
                dialogDeleteUser(id, position);
            }

            @Override
            public void updateUser(int position, List<User> userList) {
                dialogUpdateUser(position, userList);
            }


        });
        rvListUser.setHasFixedSize(true);
        rvListUser.setAdapter(userAdapter);
    }

    private void dialogDeleteUser(String id, int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
        builder.setMessage("Bạn có muốn xóa nhân viên " + userList.get(position).getFullName() + " không ?")
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        retrofitAPI.deleteUser(id).enqueue(new Callback<ServerResponse>() {
                            @Override
                            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                                Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                Fragment userFragment = getActivity().getSupportFragmentManager().findFragmentByTag(Constants.fragmentUser);
                                if (userFragment != null) {
                                    getActivity().getSupportFragmentManager().beginTransaction().remove(userFragment).replace(R.id.frm, new UserFragment(), Constants.fragmentUser).commit();
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
        })
                .create().show();
    }

    private void filter(String s) {
        List<User> userListFilter = new ArrayList<>();

        for (User user : userList) {
            if (Support.deAccent(user.getFullName()).toLowerCase().contains(s.toLowerCase())) {
                userListFilter.add(user);
            }

        }
        userAdapter.filterList(userListFilter, getActivity());
        userAdapter.notifyDataSetChanged();
    }


    private void initView(View view) {
        rvListUser = (RecyclerView) view.findViewById(R.id.rvListUser);
        fabAddStaff = (FloatingActionButton) view.findViewById(R.id.fabAddStaff);
        edtSearch = (EditText) view.findViewById(R.id.edtSearch);
        fabAddStaff.attachToRecyclerView(rvListUser);
    }


    private void dialogAddStaff() {
        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();

        View alert = LayoutInflater.from(getContext()).inflate(R.layout.dialog_add_staff, null);
        List<String> roles = new ArrayList<>();
        roles.add("Admin");
        roles.add("Staff");
        roles.add("Cashier");
        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(getContext(), R.layout.support_simple_spinner_dropdown_item, roles);
        alertDialog.setTitle("Tạo mới nhân viên");
        alertDialog.setView(alert);
        alertDialog.setCancelable(false);
        imgAvatar = alert.findViewById(R.id.imgAvatar);
        EditText edtFullName = (EditText) alert.findViewById(R.id.edtNameUser);
        EditText edtPhone = (EditText) alert.findViewById(R.id.edtPhoneUser);
        EditText edtAge = (EditText) alert.findViewById(R.id.edtAge);
        EditText edtAddress = (EditText) alert.findViewById(R.id.edtAddress);
        EditText edtCmnd = (EditText) alert.findViewById(R.id.edtCmnd);
        Spinner spnRole = (Spinner) alert.findViewById(R.id.spnRole);
        Button btnCancel = alert.findViewById(R.id.btnCancel);
        Button btnAddUser = alert.findViewById(R.id.btnAddUser);
        spnRole.setAdapter(arrayAdapter);

        spnRole.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                role = roles.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        imgAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_CODE_LOAD_IMAGE);
            }
        });

        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                alertDialog.dismiss();
            }
        });
        btnAddUser.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View view) {
                if (!checkValidate(edtFullName, edtPhone, edtAge, edtAddress, edtCmnd)) {
                    Toast.makeText(getContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                } else if (mUriImage == null) {
                    imgAvatar.setBackgroundColor(Color.RED);
                    Toast.makeText(getContext(), "Bạn chưa chọn ảnh", Toast.LENGTH_SHORT).show();
                } else if (mUriImage != null) {
                    String fullName = edtFullName.getText().toString().trim();
                    String phone = edtPhone.getText().toString().trim();
                    Integer cmnd = Integer.valueOf(edtCmnd.getText().toString().trim());
                    Integer age = Integer.valueOf(edtAge.getText().toString().trim());
                    String address = edtAddress.getText().toString().trim();

                    File file = new File(Support.getPathFromUri(getContext(), mUriImage));
                    RequestBody requestBody = RequestBody.create(MediaType.parse(getContext().getContentResolver().getType(mUriImage)), file);
                    MultipartBody.Part filePart = MultipartBody.Part.createFormData("avatar", file.getName(), requestBody);

                    retrofitAPI.createUser(fullName, phone, cmnd, age, address, role, filePart).enqueue(new Callback<ServerResponse>() {
                        @Override
                        public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                            Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                            alertDialog.dismiss();
                            Fragment userFragment = getActivity().getSupportFragmentManager().findFragmentByTag(Constants.fragmentUser);
                            if (userFragment != null) {
                                getActivity().getSupportFragmentManager().beginTransaction().remove(userFragment).replace(R.id.frm, new UserFragment(), Constants.fragmentUser).commit();
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

    private void dialogUpdateUser(int position, List<User> userList) {
        User user = userList.get(position);
        AlertDialog alertDialog = new AlertDialog.Builder(getContext()).create();
        View alert = LayoutInflater.from(getContext()).inflate(R.layout.dialog_update_staff, null);
        alertDialog.setView(alert);
        alertDialog.setTitle("Chỉnh sửa thông tin nhân viên");
        alertDialog.setCancelable(false);
        imgAvatar = alert.findViewById(R.id.imgAvatar);
        EditText edtFullName = alert.findViewById(R.id.edtNameUser);
        edtFullName.setText(String.valueOf(user.getFullName()));
        EditText edtPhone = alert.findViewById(R.id.edtPhoneUser);
        edtPhone.setText(String.valueOf(user.getPhone()));
        EditText edtCMND = alert.findViewById(R.id.edtCmnd);
        edtCMND.setText(String.valueOf(user.getIndentityCardNumber()));
        EditText edtAge = alert.findViewById(R.id.edtAge);
        edtAge.setText(String.valueOf(user.getAge()));
        EditText edtAddress = alert.findViewById(R.id.edtAddress);
        edtAddress.setText(String.valueOf(user.getAddress()));
        Glide.with(getContext()).load(Constants.LINK + user.getAvatar()).into(imgAvatar);
        Spinner spnRole = (Spinner) alert.findViewById(R.id.spnRole);
        List<String> roles = new ArrayList<>();
        roles.add("Admin");
        roles.add("Staff");
        roles.add("Cashier");
        ArrayAdapter arrayAdapter = new ArrayAdapter<String>(getContext(),
                R.layout.support_simple_spinner_dropdown_item, roles);
        spnRole.setAdapter(arrayAdapter);
        spnRole.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                role = roles.get(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        imgAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Picture"), REQUEST_CODE_LOAD_IMAGE);
            }
        });


        Button btnCancel = alert.findViewById(R.id.btnCancel);
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        Button btnUpdate = alert.findViewById(R.id.btnUpdateUser);
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.KITKAT)
            @Override
            public void onClick(View v) {
                if (!checkValidate(edtFullName, edtPhone, edtAge, edtAddress, edtCMND)) {
                    Toast.makeText(getContext(), "Vui lòng nhập đầy đủ thông tin", Toast.LENGTH_SHORT).show();
                } else {
                    if (mUriImage != null) {
                        String fullName = edtFullName.getText().toString().trim();
                        String phone = edtPhone.getText().toString().trim();
                        Integer cmnd = Integer.parseInt(edtCMND.getText().toString().trim());
                        Integer age = Integer.parseInt(edtAge.getText().toString().trim());
                        String address = edtAddress.getText().toString().trim();
                        File file = new File(Support.getPathFromUri(getContext(), mUriImage));
                        RequestBody requestBody = RequestBody.create(MediaType.parse(
                                getContext().getContentResolver().getType(mUriImage)), file);
                        MultipartBody.Part filePart = MultipartBody.Part.createFormData(
                                "avatar", file.getName(), requestBody);

                        retrofitAPI.updateUser(user.getId(), fullName, phone, cmnd, age, address, role, filePart).enqueue(new Callback<ServerResponse>() {
                            @Override
                            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                                Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                alertDialog.dismiss();
                                FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
                                fragmentTransaction.detach(UserFragment.this).attach(UserFragment.this).commit();
                            }

                            @Override
                            public void onFailure(Call<ServerResponse> call, Throwable t) {
                                Toast.makeText(getActivity(), "Lỗi hệ thống " + t.getMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        String fullName = edtFullName.getText().toString().trim();
                        String phone = edtPhone.getText().toString().trim();
                        Integer cmnd = Integer.parseInt(edtCMND.getText().toString().trim());
                        Integer age = Integer.parseInt(edtAge.getText().toString().trim());
                        String address = edtAddress.getText().toString().trim();

                        retrofitAPI.updateUserNoImage(user.getId(), fullName, phone, cmnd, age, address, role).enqueue(new Callback<ServerResponse>() {
                            @Override
                            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                                Toast.makeText(getContext(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                                alertDialog.dismiss();
                                Fragment userFragment = getActivity().getSupportFragmentManager().findFragmentByTag(Constants.fragmentUser);
                                if (userFragment != null) {
                                    getActivity().getSupportFragmentManager().beginTransaction().remove(userFragment).replace(R.id.frm, new UserFragment(), Constants.fragmentUser).commit();
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


    private boolean checkValidate(EditText edFullName, EditText edtPhone, EditText edtAge,
                                  EditText edtAddress, EditText edtCMND) {
        if (edFullName.getText().toString().trim().isEmpty()) {
            edFullName.setError("Chưa nhập họ tên");
            return false;
        } else if (edtPhone.getText().toString().trim().isEmpty()) {
            edtPhone.setError("Chưa nhập tuổi");
            return false;
        } else if (edtPhone.getText().toString().trim().length() != 10) {
            edtPhone.setError("SDT chỉ gồm 10 số");
            return false;
        } else if (edtAge.getText().toString().trim().isEmpty()) {
            edtAge.setError("Chưa nhập tuổi");
            return false;
        } else if (edtAddress.getText().toString().trim().isEmpty()) {
            edtAddress.setError("Chưa nhập địa chỉ");
            return false;
        } else if (edtCMND.getText().toString().trim().length() != 9) {
            edtCMND.setError("Số CMND phải là 9 hoặc 12 số");
            return false;
        }
        return true;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_LOAD_IMAGE && resultCode == RESULT_OK && data != null) {
            Uri uri = data.getData();
            mUriImage = uri;
            imgAvatar.setImageURI(uri);
            imgAvatar.setBackgroundColor(Color.WHITE);

        }
    }
}