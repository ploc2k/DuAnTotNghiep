package com.example.smartorder.adapter.admin;

import android.annotation.SuppressLint;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartorder.R;
import com.example.smartorder.model.bill.Bill;
import com.example.smartorder.model.user.User;
import com.example.smartorder.support.Support;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.List;
import java.util.Locale;

public class BillAdapter extends RecyclerView.Adapter<BillAdapter.BillHolder> {
    private Context context;
    private List<Bill> billList;
    private onItemClick onItemClick;


    public BillAdapter(Context context, List<Bill> billList, onItemClick onItemClick) {
        this.context = context;
        this.billList = billList;
        this.onItemClick = onItemClick;
    }

    public void filterList(List<Bill> billFilter, Context context) {
        billList = billFilter;
        this.context = context;
    }

    @NonNull
    @Override
    public BillHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rv_list_bill2, parent, false);

        return new BillHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BillHolder holder, int position) {
        holder.tvBillCode.setText(billList.get(position).getBillCode());
        holder.tvTableCode.setText("Bàn số: " + billList.get(position).getTableCode() + "");
        holder.tvStatus.setText(billList.get(position).getStatus());
        String[] date = billList.get(position).getDateBill().split("@");
        holder.tvDate.setText(date[0]);

        holder.tvTotalPrice.setText(Support.decimalFormat(billList.get(position).getTotalPrice()) + " VNĐ");
        if (billList.get(position).getStatus().equalsIgnoreCase("Đã thanh toán")) {
            holder.tvStatus.setTextColor(ContextCompat.getColor(context, R.color.tv_status_paid));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onItemClick.onClick(billList.get(position));
            }
        });

    }

    @Override
    public int getItemCount() {
        if (billList == null) return 0;
        return billList.size();
    }

    public static class BillHolder extends RecyclerView.ViewHolder {

        private TextView tvBillCode, tvTableCode, tvTotalPrice, tvStatus, tvDate;

        public BillHolder(@NonNull View itemView) {
            super(itemView);
            tvDate = itemView.findViewById(R.id.tvDate);
            tvBillCode = itemView.findViewById(R.id.tvBillCode);
            tvTableCode = itemView.findViewById(R.id.tvTableCode);
            tvTotalPrice = itemView.findViewById(R.id.tvTotalPrice);
            tvStatus = itemView.findViewById(R.id.tvStatus);
        }
    }

    public interface onItemClick {
        void onClick(Bill bill);
    }
}
