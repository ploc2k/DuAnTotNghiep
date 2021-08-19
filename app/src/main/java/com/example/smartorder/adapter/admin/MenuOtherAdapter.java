package com.example.smartorder.adapter.admin;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupMenu;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.smartorder.R;
import com.example.smartorder.constants.Constants;
import com.example.smartorder.model.menu.Menu;
import com.example.smartorder.support.Support;

import java.util.List;

public class MenuOtherAdapter extends RecyclerView.Adapter<MenuOtherAdapter.MenuHolder> {
    private List<Menu> menuListOther;
    private Context context;
    private OnClickListener onClickListener;


    public MenuOtherAdapter(List<Menu> menuListOther, Context context, OnClickListener listener) {
        this.menuListOther = menuListOther;
        this.context = context;
        this.onClickListener = listener;
    }

    @NonNull
    @Override
    public MenuHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rv_list_menu_other, parent, false);
        return new MenuHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuHolder holder, int position) {
        Glide.with(context).load(Constants.LINK + menuListOther.get(position).getImage()).into(holder.imgLogo);
        holder.tvNameMenu.setText(menuListOther.get(position).getName());
        holder.tvPriceMenu.setText(context.getString(R.string.text_adapter_price) + Support.decimalFormat(menuListOther.get(position).getPrice()) + " VNĐ");
        if (menuListOther.get(position).getStatus()) {
            holder.tvAmountMenu.setText("Trạng thái: Còn Hàng");
            holder.tvAmountMenu.setTextColor(ContextCompat.getColor(context, R.color.tv_status_paid));
        } else {
            holder.tvAmountMenu.setText("Trạng thái: Hết Hàng");
            holder.tvAmountMenu.setTextColor(ContextCompat.getColor(context, R.color.tv_status_unpaid));
        }
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                PopupMenu popupMenu = new PopupMenu(context, holder.imgLogo);
                popupMenu.getMenuInflater().inflate(R.menu.update_delete, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.delete:
                                onClickListener.deleteOther(menuListOther.get(position), menuListOther.get(position).getId());
                                break;
                            case R.id.update:
                                onClickListener.updateOther(menuListOther.get(position));
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
        if (menuListOther == null) return 0;
        return menuListOther.size();
    }

    public void filterList(List<Menu> menuDrinkFilter, Context context) {
        menuListOther = menuDrinkFilter;
        this.context = context;
    }


    public static class MenuHolder extends RecyclerView.ViewHolder {
        private ImageView imgLogo;
        private TextView tvNameMenu;
        private TextView tvPriceMenu;
        private TextView tvAmountMenu;

        public MenuHolder(@NonNull View itemView) {
            super(itemView);
            imgLogo = (ImageView) itemView.findViewById(R.id.imgLogo);
            tvNameMenu = (TextView) itemView.findViewById(R.id.tvNameMenu);
            tvPriceMenu = (TextView) itemView.findViewById(R.id.tvPriceMenu);
            tvAmountMenu = (TextView) itemView.findViewById(R.id.tvAmountMenu);
        }
    }

    public interface OnClickListener {
        void deleteOther(Menu menuOther, String id);

        void updateOther(Menu menuOther);
    }


}
