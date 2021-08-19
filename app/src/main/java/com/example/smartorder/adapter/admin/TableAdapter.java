package com.example.smartorder.adapter.admin;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartorder.R;
import com.example.smartorder.model.table.Table;

import java.util.List;

public class TableAdapter extends RecyclerView.Adapter<TableAdapter.TableHolder> {
    private Context context;
    private List<Table> tableList;
    private OnClickListener onClickListener;


    public TableAdapter(Context context, List<Table> tableList, OnClickListener onClickListener) {
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

    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull TableHolder holder, int position) {
        holder.tvTableCode.setText("Bàn số " + (tableList.get(position).getTableCode()) + "");
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(context, holder.imgTable);
                popupMenu.getMenuInflater().inflate(R.menu.update_delete, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.delete:
                                onClickListener.delete(position, tableList.get(position).getId());
                                break;
                            case R.id.update:
                                onClickListener.update(position, tableList);
                                break;
                        }
                        return false;
                    }
                });
                popupMenu.show();
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
        void delete(int position, String id);

        void update(int position, List<Table> tableList);
    }

    public void addData(List<Table> tables) {
        int last_size = tableList.size();
        tableList.addAll(tables);
        notifyItemRangeChanged(last_size, tables.size());
    }
}
