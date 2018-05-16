package com.wpam.noheads.wifi;

import android.content.Context;

import android.databinding.DataBindingUtil;
import android.databinding.ViewDataBinding;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.wpam.noheads.Model.User;
import com.wpam.noheads.R;

import java.util.List;




public class Adapter extends RecyclerView.Adapter<Holder> {
    private Context context;
    private List<User> users;

    public Adapter(Context context, List<User> users) {
        this.context = context;
        this.users = users;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewDataBinding binding = DataBindingUtil.inflate(
                LayoutInflater.from(context),
                R.layout.user_list_item,
                parent,
                false);

        return new Holder(binding.getRoot());
    }

    @Override
    public void onBindViewHolder(Holder holder, int position) {
        User user = users.get(position);
        holder.binding.setUser(user);
    }

    @Override
    public int getItemCount() {
        return users.size();
    }
}