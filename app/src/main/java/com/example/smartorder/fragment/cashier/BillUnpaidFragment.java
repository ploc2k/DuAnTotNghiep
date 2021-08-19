package com.example.smartorder.fragment.cashier;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.smartorder.R;
import com.example.smartorder.activity.LoginActivity;
import com.example.smartorder.adapter.admin.BillAdapter;
import com.example.smartorder.api.APIModule;
import com.example.smartorder.api.RetrofitAPI;
import com.example.smartorder.constants.Constants;
import com.example.smartorder.fragment.ProfileFragment;
import com.example.smartorder.model.bill.Bill;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;
import io.socket.client.IO;
import io.socket.client.Socket;
import io.socket.emitter.Emitter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BillUnpaidFragment extends Fragment {

    private EditText edtSearch;
    private RecyclerView rvListBillUnpaid;
    private BillAdapter billAdapter;
    private List<Bill> billList;
    private RetrofitAPI retrofitAPI;
    private Socket socket;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bill_unpaid, container, false);
        initView(view);

        initRecycle();
        getDataFromServer();
        try {
            socket = IO.socket(Constants.LINK + "/").connect();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        //Nhận từ server về
        socket.on("reload", new Emitter.Listener() {
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
                    e.printStackTrace();
                }
            }
        });

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                filter(editable.toString());
            }
        });
        return view;
    }

    private void filter(String s) {
        List<Bill> billFilter = new ArrayList<>();
        for (Bill bill : billList) {
            if (bill.getBillCode().toLowerCase().contains(s.toLowerCase())) {
                billFilter.add(bill);
            }
        }
        billAdapter.filterList(billFilter, getActivity());
        billAdapter.notifyDataSetChanged();
    }

    private void getDataFromServer() {
        retrofitAPI = APIModule.getInstance().create(RetrofitAPI.class);
        retrofitAPI.getListUnpaid().enqueue(new Callback<List<Bill>>() {
            @Override
            public void onResponse(Call<List<Bill>> call, Response<List<Bill>> response) {
                List<Bill> bills = response.body();
                for (int i = 0; i < bills.size(); i++) {
                    String dateBill = bills.get(i).getDateBill();
                    String id = bills.get(i).getId();
                    String billCode = bills.get(i).getBillCode();
                    String nameCashier = bills.get(i).getNameCashier();
                    Integer tableCode = bills.get(i).getTableCode();
                    Integer totalPrice = bills.get(i).getTotalPrice();
                    String status = bills.get(i).getStatus();
                    String nameOrder = bills.get(i).getNameOrder();
                    Integer discount = bills.get(i).getDiscount();
                    billList.add(new Bill(dateBill, id, billCode, nameCashier, tableCode, totalPrice, status, nameOrder, discount));
                    billAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onFailure(Call<List<Bill>> call, Throwable t) {
                Toast.makeText(getActivity(), "Lỗi hệ thống " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initRecycle() {
        billList = new ArrayList<>();
        billAdapter = new BillAdapter(getActivity(), billList, new BillAdapter.onItemClick() {
            @Override
            public void onClick(Bill bill) {
                String billCode = bill.getBillCode();
                Integer tableCode = bill.getTableCode();
                Integer totalPrice = bill.getTotalPrice();
                getActivity().getSupportFragmentManager().beginTransaction().setCustomAnimations(R.anim.admin_fragment_view_bill_scale_enter, 0).add(R.id.frq, new PayBillFragment(billCode, tableCode, totalPrice), Constants.fragmentPayBill).commit();
            }
        });
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), 1);
        rvListBillUnpaid.setLayoutManager(gridLayoutManager);
        rvListBillUnpaid.setHasFixedSize(true);
        rvListBillUnpaid.setAdapter(billAdapter);
    }

    private void initView(View view) {
        edtSearch = (EditText) view.findViewById(R.id.edtSearch);
        rvListBillUnpaid = (RecyclerView) view.findViewById(R.id.rvListBillUnpaid);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (savedInstanceState != null) {

        }
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
    }
}