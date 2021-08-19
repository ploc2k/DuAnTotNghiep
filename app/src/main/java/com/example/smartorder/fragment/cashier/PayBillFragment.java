package com.example.smartorder.fragment.cashier;

import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartorder.R;
import com.example.smartorder.adapter.cashier.BillOneAdapter;
import com.example.smartorder.adapter.cashier.OneBillAdapter;
import com.example.smartorder.api.APIModule;
import com.example.smartorder.api.RetrofitAPI;
import com.example.smartorder.constants.Constants;
import com.example.smartorder.model.bill.BillOne;
import com.example.smartorder.model.response.ServerResponse;
import com.example.smartorder.support.Support;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import io.socket.client.IO;
import io.socket.client.Socket;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PayBillFragment extends Fragment {

    private List<BillOne> billOneList;
    //    private BillOneAdapter billOneAdapter;
    private OneBillAdapter billOneAdapter;
    private TextView tvBillCode;
    private TextView tvTableCode, tvGiamGia, tvThanhToan;
    private EditText edtCoupon;
    private RecyclerView rvListBillOne;
    private TextView tvTongtien;
    private Button btnCancel;
    private Button btnCoupon;
    private Button btnPay;
    private RetrofitAPI retrofitAPI;
    private String billCode = "";
    private Integer tableCode;
    private Integer totalMoney;
    private Integer coupon = 0;
    private Integer lastTotal = totalMoney;
    private Socket socket;

    public PayBillFragment(String billCode, Integer tableCode, Integer totalMoney) {
        this.billCode = billCode;
        this.tableCode = tableCode;
        this.totalMoney = totalMoney;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_pay_bill, container, false);
        initView(view);
        retrofitAPI = APIModule.getInstance().create(RetrofitAPI.class);
        initRecycleView();
        getBillOne();
        try {
            socket = IO.socket(Constants.LINK + "/").connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        tvBillCode.setText("Mã hóa đơn: " + billCode);
        tvTableCode.setText("Bàn số " + tableCode);
        tvTongtien.setText("Tổng tiền: " + Support.decimalFormat(totalMoney) + " VNĐ");
        tvThanhToan.setText("Thanh toán: " + Support.decimalFormat(totalMoney) + " VNĐ");
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelPay();
            }
        });
        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pay();
            }
        });
        btnCoupon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkCoupon();
            }
        });
        return view;
    }

    private void checkCoupon() {
        coupon = Integer.parseInt(edtCoupon.getText().toString().trim());
        if (coupon != 0) {
            Integer giamGia = totalMoney * coupon / 100;
            tvGiamGia.setText("Giảm giá: " + Support.decimalFormat(giamGia) + " VNĐ");
//            tvGiamGia.setPaintFlags(tvGiamGia.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
            lastTotal = totalMoney - giamGia;
            tvThanhToan.setText("Thanh toán: " + Support.decimalFormat(lastTotal) + " VNĐ");
        }
    }

    private void pay() {
        retrofitAPI.payBill(billCode, Constants.NameUser, coupon, lastTotal).enqueue(new Callback<ServerResponse>() {
            @Override
            public void onResponse(Call<ServerResponse> call, Response<ServerResponse> response) {
                if (response.code() == 200) {
                    socket.emit("payy", "Xong");
                    socket.disconnect();
                    Toast.makeText(getActivity(), response.body().getMessage(), Toast.LENGTH_SHORT).show();
                    Fragment payBillFrament = getFragmentManager().findFragmentByTag(Constants.fragmentPayBill);
                    if (payBillFrament != null) {
                        getFragmentManager().beginTransaction().setCustomAnimations(0, R.anim.admin_fragment_view_bill_scale_exit).remove(payBillFrament).replace(R.id.frq, new CashierFragment(), Constants.fragmentCashier).commit();
                    }
                }
            }

            @Override
            public void onFailure(Call<ServerResponse> call, Throwable t) {
                Toast.makeText(getActivity(), "Lỗi hệ thống" + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void cancelPay() {
        Fragment payBillFrament = getActivity().getSupportFragmentManager().findFragmentByTag(Constants.fragmentPayBill);
        if (payBillFrament != null) {
            getActivity().getSupportFragmentManager().beginTransaction().remove(payBillFrament).commit();
        }
    }

    private void initRecycleView() {
        billOneList = new ArrayList<>();
        billOneAdapter = new OneBillAdapter(getActivity(), billOneList);
        rvListBillOne.setLayoutManager(new LinearLayoutManager(getActivity()));
        rvListBillOne.setHasFixedSize(true);
        rvListBillOne.setAdapter(billOneAdapter);
    }

    private void getBillOne() {
        retrofitAPI.getDetailBill(billCode).enqueue(new Callback<List<BillOne>>() {
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

    private void initView(View view) {
        tvBillCode = (TextView) view.findViewById(R.id.tvBillCode);
        tvTableCode = (TextView) view.findViewById(R.id.tvTableCode);
        tvGiamGia = (TextView) view.findViewById(R.id.tvGiamGia);
        tvThanhToan = (TextView) view.findViewById(R.id.tvThanhToan);
        rvListBillOne = (RecyclerView) view.findViewById(R.id.rvListBillOne);
        tvTongtien = (TextView) view.findViewById(R.id.tvTongtien);
        edtCoupon = (EditText) view.findViewById(R.id.edtCoupon);
        btnCancel = (Button) view.findViewById(R.id.btnCancel);
        btnCoupon = (Button) view.findViewById(R.id.btnCoupon);
        btnPay = (Button) view.findViewById(R.id.btnPay);
    }
}