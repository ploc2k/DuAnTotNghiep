package com.example.smartorder.adapter.staff;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartorder.R;
import com.example.smartorder.model.table.Table;

import java.util.List;

public class StaffTableAdapter extends RecyclerView.Adapter<StaffTableAdapter.TableHolder> {
    private Context context;
    private List<Table> tableList;
    private OnClickListener onClickListener;


    public StaffTableAdapter(Context context, List<Table> tableList, OnClickListener onClickListener) {
        this.context = context;
        this.tableList = tableList;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public TableHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rv_list_table, parent, false);
        return new TableHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TableHolder holder, int position) {
        holder.tvTableCode.setText(tableList.get(position).getTableCode() + "");
        if (tableList.get(position).getStatus()) {
            holder.tvTableCode.setTextColor(ContextCompat.getColor(context, R.color.tv_status_unpaid));
        } else {
            holder.tvTableCode.setTextColor(ContextCompat.getColor(context, R.color.white));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickListener.order(position, view);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (tableList == null) return 0;
        return tableList.size();
    }

    public static class TableHolder extends RecyclerView.ViewHolder {
        private TextView tvTableCode;
        private ImageView imgTable;

        public TableHolder(@NonNull View itemView) {
            super(itemView);
            tvTableCode = itemView.findViewById(R.id.tvTableCode);
            imgTable = itemView.findViewById(R.id.imgTable);
        }
    }

    public interface OnClickListener {
        void order(int position, View view);
    }
}
