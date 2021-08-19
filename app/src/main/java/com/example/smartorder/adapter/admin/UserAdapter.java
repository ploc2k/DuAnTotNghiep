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
import com.example.smartorder.model.table.Table;
import com.example.smartorder.model.user.User;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserHolder> {
    private Context context;
    private List<User> userList;
    private OnClickListener onClickListener;


    public UserAdapter(Context context, List<User> userList, OnClickListener onClickListener) {
        this.context = context;
        this.userList = userList;
        this.onClickListener = onClickListener;
    }

    @NonNull
    @Override
    public UserHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.rv_list_user, parent, false);
        return new UserHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserHolder holder, int position) {
        Glide.with(context).load(Constants.LINK + userList.get(position).getAvatar()).into(holder.imgUser);
        holder.tvNameUser.setText(userList.get(position).getFullName());
        holder.tvRole.setText("Chức vụ: " + userList.get(position).getRole());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                PopupMenu popupMenu = new PopupMenu(context, holder.itemView);
                popupMenu.getMenuInflater().inflate(R.menu.update_delete, popupMenu.getMenu());
                popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()) {
                            case R.id.delete:
                                onClickListener.deleteUser(position, userList.get(position).getId());
                                break;
                            case R.id.update:
                                onClickListener.updateUser(position, userList);
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
        return userList.size();
    }

    public void filterList(List<User> userFilter, Context context) {
        userList = userFilter;
        this.context = context;
    }

    public static class UserHolder extends RecyclerView.ViewHolder {
        private ImageView imgUser;
        private TextView tvNameUser;
        private TextView tvRole;

        public UserHolder(@NonNull View itemView) {
            super(itemView);
            imgUser = (ImageView) itemView.findViewById(R.id.imgUser);
            tvNameUser = (TextView) itemView.findViewById(R.id.tvNameUser);
            tvRole = (TextView) itemView.findViewById(R.id.tvRole);
        }
    }

    public interface OnClickListener {
        void deleteUser(int position, String id);

        void updateUser(int position, List<User> userList);
    }

}
