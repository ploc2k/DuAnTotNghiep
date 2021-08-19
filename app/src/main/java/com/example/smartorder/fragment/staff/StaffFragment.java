package com.example.smartorder.fragment.staff;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.smartorder.R;
import com.example.smartorder.activity.LoginActivity;
import com.example.smartorder.adapter.staff.StaffTableAdapter;
import com.example.smartorder.api.APIModule;
import com.example.smartorder.api.RetrofitAPI;
import com.example.smartorder.constants.Constants;
import com.example.smartorder.fragment.ProfileFragment;
import com.example.smartorder.model.callback.CallbackTalble;
import com.example.smartorder.model.table.Table;
import com.melnykov.fab.FloatingActionButton;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class StaffFragment extends Fragment {

    private androidx.appcompat.widget.Toolbar toolbar;
    private RecyclerView rvListTableStaff;
    private FloatingActionButton fabAddTable;
    private CircleImageView imgProfile;
    private RetrofitAPI retrofitAPI;
    private StaffTableAdapter staffTableAdapter;
    private List<Table> tableList;
    private Socket socket;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_staff, container, false);
        initView(view);
        Glide.with(getActivity()).load(Constants.LINK + Constants.AvatarUser).into(imgProfile);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ((AppCompatActivity) getActivity()).getSupportActionBar().setTitle("Order");
        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCustomPopupMenu(view);
            }
        });
//        getActivity().getSupportFragmentManager().beginTransaction().replace(R.id.frmTest, new ListTableOrderFragment(), Constants.fragmentListTableOrder).commit();
        try {
            socket = IO.socket(Constants.LINK + "/").connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        retrofitAPI = APIModule.getInstance().create(RetrofitAPI.class);
        tableList = new ArrayList<>();
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 2, RecyclerView.VERTICAL, false);
        rvListTableStaff.setLayoutManager(gridLayoutManager);
        staffTableAdapter = new StaffTableAdapter(getActivity(), tableList, new StaffTableAdapter.OnClickListener() {
            @Override
            public void order(int position, View view) {
                ListFoodOrderFragment listFoodOrderFragment = new ListFoodOrderFragment();
                CallbackTalble callbackTalble = listFoodOrderFragment;
                callbackTalble.getTable(tableList.get(position));
                BillPreviewFragment previewFragment = new BillPreviewFragment();
                CallbackTalble callbackTalble1 = previewFragment;
                callbackTalble1.getTable(tableList.get(position));
                if (tableList.get(position).getStatus()) {
                    showPopupChoicesMenu(view, listFoodOrderFragment, previewFragment);
                } else {
                    getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.list_food_bottom_to_top, 0).add(R.id.frq, listFoodOrderFragment, Constants.fragmentListFood).commit();
                }
            }
        });
        rvListTableStaff.setHasFixedSize(true);
        rvListTableStaff.setAdapter(staffTableAdapter);
        retrofitAPI.getAllTable().enqueue(new Callback<List<Table>>() {
            @Override
            public void onResponse(Call<List<Table>> call, Response<List<Table>> response) {
                List<Table> table = response.body();
                for (int i = 0; i < table.size(); i++) {
                    String id = table.get(i).getId();
                    Integer tableCode = table.get(i).getTableCode();
                    Integer tableSeats = table.get(i).getTableSeats();
                    boolean status = table.get(i).getStatus();
                    tableList.add(new Table(id, tableCode, tableSeats, status));
                    staffTableAdapter.notifyDataSetChanged();
                }
                Collections.sort(tableList, new Comparator<Table>() {
                    @Override
                    public int compare(Table table, Table t1) {
                        if (table.getTableCode() > t1.getTableCode()) {
                            return 1;
                        } else {
                            if (table.getTableCode() == t1.getTableCode()) {
                                return 0;
                            } else {
                                return -1;
                            }
                        }
                    }
                });
            }

            @Override
            public void onFailure(Call<List<Table>> call, Throwable t) {
                Toast.makeText(getActivity(), "Lỗi hệ thống" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        socket.on("reloadTable", new Emitter.Listener() {
            @Override
            public void call(Object... args) {
                JSONObject jsonObject = (JSONObject) args[0];
                try {
                    boolean reload = jsonObject.getBoolean("reload");
                    if (reload) {
                        socket.disconnect();
                        try {
                            getActivity().finish();
                            getActivity().startActivity(getActivity().getIntent());
                        } catch (Exception e) {
                            Log.d("TAG", "call: " + e.getMessage());
                        }

                    }
                } catch (JSONException e) {
                    Log.e("JSONException: ", e.getMessage());
                }
            }
        });
        return view;
    }

    private void initView(View view) {
        toolbar = view.findViewById(R.id.toolbar);
        imgProfile = (CircleImageView) view.findViewById(R.id.imgProfile);
        rvListTableStaff = (RecyclerView) view.findViewById(R.id.rvListTableStaff);
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

    private void showPopupChoicesMenu(View view, Fragment listFoodOrderFragment, Fragment previewFragment) {
        PopupMenu popupMenu = new PopupMenu(getActivity(), view);
        popupMenu.inflate(R.menu.choices_menu);
        popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.order:
                        getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.list_food_bottom_to_top, 0).add(R.id.frq, listFoodOrderFragment, Constants.fragmentListFood).commit();
                        break;
                    case R.id.invoicePreview:
                        getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.admin_fragment_main_translate_enter_right_to_left, 0).add(R.id.frq, previewFragment, Constants.fragmentPreviewBill).commit();
                        break;
                }
                return false;
            }
        });
        popupMenu.show();
    }


}