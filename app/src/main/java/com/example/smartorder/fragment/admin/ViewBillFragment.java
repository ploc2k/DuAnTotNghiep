package com.example.smartorder.fragment.admin;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartorder.R;
import com.example.smartorder.adapter.cashier.BillOneAdapter;
import com.example.smartorder.api.APIModule;
import com.example.smartorder.api.RetrofitAPI;
import com.example.smartorder.constants.Constants;
import com.example.smartorder.model.bill.Bill;
import com.example.smartorder.model.bill.BillOne;
import com.example.smartorder.support.Support;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ViewBillFragment extends Fragment implements View.OnClickListener {
    private Bill bill;
    private TextView tvTableCode, tvTotalMoney, tvCashier, tvDate, tvNameOrder, tvDiscount, tvTotal, tvBillCode;
    private ImageButton btnClose;
    private RetrofitAPI retrofitAPI;
    private List<BillOne> billOneList;
    private RecyclerView rvList;
    private BillOneAdapter billOneAdapter;

    public ViewBillFragment(Bill bill) {
        this.bill = bill;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view_bill, container, false);
        initView(view);

        retrofitAPI = APIModule.getInstance().create(RetrofitAPI.class);
        tvBillCode.setText("Mã HD: " + bill.getBillCode());
        tvTableCode.setText("Bàn số: " + bill.getTableCode());
        tvCashier.setText("Thu ngân: " + bill.getNameCashier());
        tvNameOrder.setText("Order: " + bill.getNameOrder());
        int discount = bill.getDiscount();
        int totalMoney = bill.getTotalPrice();
        int total = totalMoney * 100 / (100 - discount);
        int discountMoney = total * discount / 100;
        tvDiscount.setText("Giảm giá: " + Support.decimalFormat(discountMoney) + " VND");
        tvTotal.setText("Tổng tiền: " + Support.decimalFormat(total) + " VND");
        tvTotalMoney.setText("Thanh toán: " + Support.decimalFormat(bill.getTotalPrice()) + " VND");
        tvDate.setText("Ngày HD: " + bill.getDateBill());
        initRecycleView();
        getBillOne();
        btnClose.setOnClickListener(this);
        return view;
    }

    private void initView(View view) {
        tvTableCode = (TextView) view.findViewById(R.id.tvTableCode);
        btnClose = (ImageButton) view.findViewById(R.id.btnClose);
        rvList = (RecyclerView) view.findViewById(R.id.rvList);
        tvTotalMoney = view.findViewById(R.id.tvTotalMoney);
        tvCashier = view.findViewById(R.id.tvNameCashier);
        tvDate = view.findViewById(R.id.tvDate);
        tvNameOrder = view.findViewById(R.id.tvNameOrder);
        tvDiscount = view.findViewById(R.id.tvDiscount);
        tvTotal = view.findViewById(R.id.tvTotal);
        tvBillCode = view.findViewById(R.id.tvBillCode);

    }

    private void initRecycleView() {
        billOneList = new ArrayList<>();
        billOneAdapter = new BillOneAdapter(getActivity(), billOneList);
        rvList.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvList.setHasFixedSize(true);
        rvList.setAdapter(billOneAdapter);
    }

    private void getBillOne() {
        retrofitAPI.getDetailBill(bill.getBillCode()).enqueue(new Callback<List<BillOne>>() {
            @Override
            public void onResponse(Call<List<BillOne>> call, Response<List<BillOne>> response) {
                List<BillOne> billOnes = response.body();
                for (int i = 0; i < billOnes.size(); i++) {
                    String id = billOnes.get(i).getId();
                    String billCode = billOnes.get(i).getBillCode();
                    String image = billOnes.get(i).getImage();
                    Integer sl = billOnes.get(i).getSl();
                    String name = billOnes.get(i).getName();
                    Integer price = billOnes.get(i).getPrice();
                    String type = billOnes.get(i).getType();
                    Integer totalMoney = billOnes.get(i).getTotalMoney();
                    billOneList.add(new BillOne(id, billCode, image, sl, name, price, type, totalMoney));
                    billOneAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<BillOne>> call, Throwable t) {
                Toast.makeText(getActivity(), "Lỗi hệ thống" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnClose:
                Fragment fragment = getActivity().getSupportFragmentManager().findFragmentByTag(Constants.fragmentViewBill);
                if (fragment != null) {
                    getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(0, R.anim.admin_fragment_view_bill_scale_exit).remove(fragment).commit();
                }
                break;
        }
    }
}