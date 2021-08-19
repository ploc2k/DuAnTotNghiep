package com.example.smartorder.adapter.cashier;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.smartorder.R;
import com.example.smartorder.constants.Constants;
import com.example.smartorder.model.bill.BillOne;
import com.example.smartorder.support.Support;

import java.util.List;

public class OneBillAdapter extends RecyclerView.Adapter<OneBillAdapter.BillOneHolder> {

    Context context;
    List<BillOne> billOneList;

    public OneBillAdapter(Context context, List<BillOne> billOneList) {
        this.context = context;
        this.billOneList = billOneList;
    }

    @NonNull
    @Override
    public BillOneHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rv_bill_one, parent, false);
        return new BillOneHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BillOneHolder holder, int position) {
        holder.tvName.setText(billOneList.get(position).getName());
        holder.tvSl.setText(billOneList.get(position).getSl() + "");
        holder.tvTotal.setText(Support.decimalFormat(billOneList.get(position).getTotalMoney()) + " VND");
        Glide.with(context).load(Constants.LINK + billOneList.get(position).getImage()).into(holder.imgLogo);
    }

    @Override
    public int getItemCount() {
        if (billOneList == null) return 0;
        return billOneList.size();
    }

    public static class BillOneHolder extends RecyclerView.ViewHolder {
        private ImageView imgLogo;
        private TextView tvName;
        private TextView tvTotal;
        private TextView tvSl;


        public BillOneHolder(@NonNull View view) {
            super(view);
            imgLogo = (ImageView) view.findViewById(R.id.imgLogo);
            tvName = (TextView) view.findViewById(R.id.tvName);
            tvTotal = (TextView) view.findViewById(R.id.tvTotal);
            tvSl = (TextView) view.findViewById(R.id.tvSl);
        }
    }

}
