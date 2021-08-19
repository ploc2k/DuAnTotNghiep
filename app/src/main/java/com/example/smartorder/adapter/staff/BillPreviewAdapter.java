package com.example.smartorder.adapter.staff;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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

public class BillPreviewAdapter extends RecyclerView.Adapter<BillPreviewAdapter.BillPreviewHolder> {

    private Context context;
    private List<BillOne> billOneList;


    public BillPreviewAdapter(Context context, List<BillOne> billOneList) {
        this.context = context;
        this.billOneList = billOneList;
    }

    @NonNull
    @Override
    public BillPreviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rv_bill_preview, parent, false);
        return new BillPreviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BillPreviewHolder holder, int position) {
        BillOne billOnes = billOneList.get(position);
        Glide.with(context).load(Constants.LINK + billOnes.getImage()).into(holder.imgLogo);
        holder.tvSl.setText(billOnes.getSl() + "");
        holder.tvName.setText(billOnes.getName());
        holder.tvTotal.setText(Support.decimalFormat(billOnes.getTotalMoney()) + " VNĐ");
//        holder.btnTang.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                int sl = Integer.parseInt(holder.tvSl.getText().toString());
//                sl += 1;
//                holder.tvSl.setText(String.valueOf(sl));
//                billOneList.get(position).setSl(sl);
//                int totalMoney = sl * billOneList.get(position).getPrice();
//                holder.tvTotal.setText(Support.decimalFormat(totalMoney) + " VNĐ");
//                billOneList.get(position).setTotalMoney(totalMoney);
//            }
//        });
        if (billOnes.getType().equals("Food")) {
            holder.btnGiam.setVisibility(View.INVISIBLE);
        }
        holder.btnGiam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int sl = Integer.parseInt(holder.tvSl.getText().toString());
                if (sl > 1) {
                    sl -= 1;
                    holder.tvSl.setText(String.valueOf(sl));
                }
                billOneList.get(position).setSl(sl);
                int totalMoney = sl * billOneList.get(position).getPrice();
                holder.tvTotal.setText(Support.decimalFormat(sl * billOneList.get(position).getPrice()) + " VNĐ");
                billOneList.get(position).setTotalMoney(totalMoney);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (billOneList == null) return 0;
        return billOneList.size();
    }


    public static class BillPreviewHolder extends RecyclerView.ViewHolder {
        private ImageView imgLogo;
        private TextView tvName;
        private TextView tvTotal;
        //        private ImageButton btnTang;
        private TextView tvSl;
        private ImageButton btnGiam;

        public BillPreviewHolder(@NonNull View itemView) {
            super(itemView);
            imgLogo = (ImageView) itemView.findViewById(R.id.imgLogo);
            tvName = (TextView) itemView.findViewById(R.id.tvName);
            tvTotal = (TextView) itemView.findViewById(R.id.tvTotal);
//            btnTang = (ImageButton) itemView.findViewById(R.id.btnTang);
            tvSl = (TextView) itemView.findViewById(R.id.tvSl);
            btnGiam = (ImageButton) itemView.findViewById(R.id.btnGiam);
        }
    }
}
