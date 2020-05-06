package com.fikrihashfi.githubusersearch.ui.user;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.fikrihashfi.githubusersearch.R;
import com.fikrihashfi.githubusersearch.model.Users;

import java.util.ArrayList;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.ListViewHolder> {
    private ArrayList<Users> listUsers = new ArrayList<>();
    private UserAdapter.OnItemClickCallback onItemClickCallback;

    public UserAdapter() {

    }

    public UserAdapter(ArrayList<Users> list) {
        this.listUsers = list;
    }

    public void setListUsers(ArrayList<Users> listUsers) {
        this.listUsers.clear();
        this.listUsers.addAll(listUsers);
        notifyDataSetChanged();
    }

    public ArrayList<Users> getList() {
        return this.listUsers;
    }

    @NonNull
    @Override
    public ListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_row_user, parent, false);
        return new ListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ListViewHolder holder, int position) {
        Users users = listUsers.get(position);
        Glide.with(holder.itemView.getContext())
                .load(users.getAvatarUrl())
                .apply(new RequestOptions().override(55, 55))
                .into(holder.imgPhoto);
        holder.tvName.setText(users.getLogin());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickCallback.onItemClicked(listUsers.get(holder.getAdapterPosition()));
            }
        });
    }

    public interface OnItemClickCallback {
        void onItemClicked(Users user);
    }

    public void setOnItemClickCallback(UserAdapter.OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }


    @Override
    public int getItemCount() {
        return listUsers.size();
    }

    class ListViewHolder extends RecyclerView.ViewHolder {
        ImageView imgPhoto;
        TextView tvName;

        ListViewHolder(View itemView) {
            super(itemView);
            imgPhoto = itemView.findViewById(R.id.img_item_photo);
            tvName = itemView.findViewById(R.id.tv_item_name);
        }
    }
}
