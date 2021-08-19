package com.example.smartorder.adapter.staff;

import android.graphics.Color;
import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.smartorder.R;
import com.example.smartorder.constants.Constants;
import com.example.smartorder.fragment.staff.ListFoodOrderFragment;
import com.example.smartorder.model.menu.MenuOrder;
import com.example.smartorder.support.Support;

import java.util.List;


import de.hdodenhof.circleimageview.CircleImageView;

public class MenuOrderAdapter extends RecyclerView.Adapter<MenuOrderAdapter.MenuHolder> {
    private List<MenuOrder> menuOrders;
    private ListFoodOrderFragment context;


    public MenuOrderAdapter(ListFoodOrderFragment context, List<MenuOrder> list) {
        this.context = context;
        this.menuOrders = list;
    }

    @NonNull
    @Override
    public MenuHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = View.inflate(context.getContext(), R.layout.rv_list_menu_order, null);
        return new MenuHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull MenuHolder holder, int position) {
        MenuOrder menuOrder = menuOrders.get(position);
//        if (!menuOrder.getStatus()){
//            holder.tvNameMenu.setTextColor(Color.RED);
//            holder.tvPriceMenu.setTextColor(Color.RED);
//            holder.chkOrder.setVisibility(View.GONE);
//        }
        Glide.with(context.getContext()).load(Constants.LINK + menuOrders.get(position).getImage()).into(holder.imgFoodOrder);
        if (menuOrder.getSl() > 0) {
            holder.tvSl.setText(menuOrder.getSl() + "");
        } else {
            holder.tvSl.setText("1");
        }

        if (menuOrder.isChecked()) {
            holder.chkOrder.setImageDrawable(context.getContext().getDrawable(R.drawable.ic_check));
            holder.tvSl.setVisibility(View.VISIBLE);
            holder.btnTang.setVisibility(View.VISIBLE);
            holder.btnGiam.setVisibility(View.VISIBLE);
            holder.btnTang.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int sl = Integer.parseInt(holder.tvSl.getText().toString()) + 1;
                    holder.tvSl.setText(sl + "");
                    menuOrders.get(position).setSl(sl);
                }
            });
            holder.btnGiam.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int sl = Integer.parseInt(holder.tvSl.getText().toString());
                    if (sl > 1) {
                        sl -= 1;
                        holder.tvSl.setText(sl + "");
                    }
                    menuOrders.get(position).setSl(sl);
                }
            });
        } else {
            holder.chkOrder.setImageDrawable(context.getContext().getDrawable(R.drawable.ic_no_check));
            holder.tvSl.setVisibility(View.INVISIBLE);
            holder.btnTang.setVisibility(View.INVISIBLE);
            holder.btnGiam.setVisibility(View.INVISIBLE);
        }

        holder.chkOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                menuOrders.get(position).setChecked(!menuOrder.isChecked());
                menuOrders.get(position).setSl(Integer.parseInt(holder.tvSl.getText().toString()));
                notifyDataSetChanged();
            }
        });

        holder.tvNameMenu.setText(menuOrder.getName());

        holder.tvPriceMenu.setText("Giá: " + Support.decimalFormat(menuOrder.getPrice()) + " VNĐ");
    }

    @Override
    public int getItemCount() {
        return menuOrders.size();
    }

    public void filterList(List<MenuOrder> menuOrderFilter, ListFoodOrderFragment context) {
        menuOrders = menuOrderFilter;
        this.context = context;
    }


    public static class MenuHolder extends RecyclerView.ViewHolder {
        private TextView tvNameMenu;
        private TextView tvPriceMenu;
        private ImageView chkOrder;
        private ImageButton btnTang;
        private TextView tvSl;
        private ImageButton btnGiam;
        private CircleImageView imgFoodOrder;

        public MenuHolder(@NonNull View itemView) {
            super(itemView);
            tvNameMenu = (TextView) itemView.findViewById(R.id.tvNameMenu);
            tvPriceMenu = (TextView) itemView.findViewById(R.id.tvPriceMenu);
            chkOrder = itemView.findViewById(R.id.chkOrder);
            btnTang = (ImageButton) itemView.findViewById(R.id.btnTang);
            tvSl = (TextView) itemView.findViewById(R.id.tvSl);
            btnGiam = (ImageButton) itemView.findViewById(R.id.btnGiam);
            imgFoodOrder = (CircleImageView) itemView.findViewById(R.id.imgFoodOrder);
        }
    }
}
