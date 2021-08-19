package com.example.smartorder.fragment.staff;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartorder.R;
import com.example.smartorder.adapter.staff.BillPreviewAdapter;
import com.example.smartorder.api.APIModule;
import com.example.smartorder.api.RetrofitAPI;
import com.example.smartorder.constants.Constants;
import com.example.smartorder.model.bill.BillOne;
import com.example.smartorder.model.callback.CallbackTalble;
import com.example.smartorder.model.menu.ListBillUpdate;
import com.example.smartorder.model.response.ServerResponse;
import com.example.smartorder.model.table.Table;
import com.example.smartorder.support.Support;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class BillPreviewFragment extends Fragment implements CallbackTalble {


    private TextView tvTableCode, tvTotal;
    private ImageButton btnSave;
    private Integer tableCode;
    private RetrofitAPI retrofitAPI;
    private RecyclerView rvList;
    private int total = 0;
    private BillPreviewAdapter billPreviewAdapter;
    private ImageButton btnBack;
    private List<BillOne> billOneList;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_preview, container, false);
        initView(view);
        retrofitAPI = APIModule.getInstance().create(RetrofitAPI.class);
        tvTableCode.setText("Bàn số " + tableCode);
        initRecycleview();
        getListBillOneFromServer();
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                total = 0;
                for (int i = 0; i < billOneList.size(); i++) {
                    Integer totalMoney = billOneList.get(i).getTotalMoney();
                    total += totalMoney;
                }
                tvTotal.setText("Tổng: " + Support.decimalFormat(total) + " VNĐ");
                ListBillUpdate listBillUpdate = new ListBillUpdate();
                listBillUpdate.setBillOneList(billOneList);
                retrofitAPI.returnItems(listBillUpdate).enqueue(new Callback<ServerResponse>() {
                    @Override
                    public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                        if (response.code() == 200) {
                            Toast.makeText(getActivity(), "Lưu thành công", Toast.LENGTH_SHORT).show();
                            Fragment billPreViewFragment = getActivity().getSupportFragmentManager().findFragmentByTag(Constants.fragmentPreviewBill);
                            if (billPreViewFragment != null) {
                                getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(0, R.anim.admin_fragment_main_translate_exit_left_to_right).remove(billPreViewFragment).commit();
                            }
                        } else {
                            Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<ServerResponse> call, Throwable t) {
                        Toast.makeText(getActivity(), "Lỗi hệ thống " + t.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Fragment billPreViewFragment = getActivity().getSupportFragmentManager().findFragmentByTag(Constants.fragmentPreviewBill);
                if (billPreViewFragment != null) {
                    getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(0, R.anim.admin_fragment_main_translate_exit_left_to_right).remove(billPreViewFragment).commit();
                }

            }
        });
        return view;
    }

    private void getListBillOneFromServer() {
        retrofitAPI.getBillPreview(tableCode).enqueue(new Callback<List<BillOne>>() {
            @Override
            public void onResponse(Call<List<BillOne>> call, Response<List<BillOne>> response) {
                if (response.code() == 200) {
                    List<BillOne> billOnes = response.body();
                    for (int i = 0; i < billOnes.size(); i++) {
                        String billCode = billOnes.get(i).getBillCode();
                        String id = billOnes.get(i).getId();
                        String image = billOnes.get(i).getImage();
                        Integer sl = billOnes.get(i).getSl();
                        String name = billOnes.get(i).getName();
                        Integer price = billOnes.get(i).getPrice();
                        String type = billOnes.get(i).getType();
                        Integer totalMoney = billOnes.get(i).getTotalMoney();
                        billOneList.add(new BillOne(id, billCode, image, sl, name, price, type, totalMoney));
                        billPreviewAdapter.notifyDataSetChanged();
                        total += totalMoney;
                    }
                    tvTotal.setText("Tổng: " + Support.decimalFormat(total) + " VNĐ");
                } else {
                    Toast.makeText(getActivity(), "Không lấy được thông tin", Toast.LENGTH_SHORT).show();
                }

            }

            @Override
            public void onFailure(Call<List<BillOne>> call, Throwable t) {
                Toast.makeText(getActivity(), "Lỗi hệ thống " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initRecycleview() {
        billOneList = new ArrayList<>();
        billPreviewAdapter = new BillPreviewAdapter(getActivity(), billOneList);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        rvList.setLayoutManager(linearLayoutManager);
        rvList.setHasFixedSize(true);
        rvList.setAdapter(billPreviewAdapter);
    }

    private void initView(View view) {
        tvTableCode = (TextView) view.findViewById(R.id.tvTableCode);
        tvTotal = (TextView) view.findViewById(R.id.tvTotal);
        btnSave = (ImageButton) view.findViewById(R.id.btnSave);
        rvList = (RecyclerView) view.findViewById(R.id.rvList);
        btnBack = (ImageButton) view.findViewById(R.id.btnBack);

    }

    @Override
    public void getTable(Table table) {
        this.tableCode = table.getTableCode();
    }
}