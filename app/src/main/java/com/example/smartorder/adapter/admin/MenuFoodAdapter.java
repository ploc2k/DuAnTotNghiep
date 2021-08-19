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
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.smartorder.R;
import com.example.smartorder.constants.Constants;
import com.example.smartorder.model.menu.Menu;
import com.example.smartorder.support.Support;

import java.util.List;

public class MenuFoodAdapter extends RecyclerView.Adapter<MenuFoodAdapter.MenuHolder> {
    private List<Menu> menuListFood;
    private Context context;
    private OnClickListener onClickListener;

    public MenuFoodAdapter(List<Menu> menuListFood, Context context, OnClickListener onClickListener) {
        this.menuListFood = menuListFood;
        this.context = context;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public MenuHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rv_list_menu_food, parent, false);
        return new MenuHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MenuHolder holder, int position) {
        Glide.with(context).load(Constants.LINK + menuListFood.get(position).getImage()).into(holder.imgLogo);
        holder.tvNameMenu.setText(menuListFood.get(position).getName());
        holder.tvPriceMenu.setText(context.getString(R.string.text_adapter_price) + Support.decimalFormat(menuListFood.get(position).getPrice()) + " VNĐ");
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
                                onClickListener.deleteFood(menuListFood.get(position), menuListFood.get(position).getId());
                                break;
                            case R.id.update:
                                onClickListener.updateFood(menuListFood.get(position));
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
        if (menuListFood == null) return 0;
        return menuListFood.size();
    }

    public void filterList(List<Menu> menuDrinkFilter, Context context) {
        menuListFood = menuDrinkFilter;
        this.context = context;
    }


    public static class MenuHolder extends RecyclerView.ViewHolder {
        private ImageView imgLogo;
        private TextView tvNameMenu;
        private TextView tvPriceMenu;

        public MenuHolder(@NonNull View itemView) {
            super(itemView);
            imgLogo = (ImageView) itemView.findViewById(R.id.imgLogo);
            tvNameMenu = (TextView) itemView.findViewById(R.id.tvNameMenu);
            tvPriceMenu = (TextView) itemView.findViewById(R.id.tvPriceMenu);
        }
    }

    public interface OnClickListener {
        void deleteFood(Menu menuFood, String id);

        void updateFood(Menu menuFood);
    }

}
