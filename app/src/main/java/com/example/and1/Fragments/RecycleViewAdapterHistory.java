package com.example.and1.Fragments;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.and1.R;
import com.example.and1.model.Order;

import java.util.List;

public class RecycleViewAdapterHistory extends RecyclerView.Adapter<RecycleViewAdapterHistory.MyViewHolder> {

    android.content.Context context;
    List<Order> orders;


    public RecycleViewAdapterHistory(android.content.Context context, List<Order> orders){
        this.context = context;
        this.orders = orders;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
       View view = LayoutInflater.from(context).inflate(R.layout.item_order, parent, false);
       MyViewHolder vHolder = new MyViewHolder(view);

        return vHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //holder.tvPrice.setText(orders.get(position).getTotalPrice());
        //TODO: Fix the parsing and get the orders by current user.
        holder.tvPeriod.setText(orders.get(position).getPeriod());
    }


    @Override
    public int getItemCount() {
        return orders.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView tvPrice;
        private TextView tvPeriod;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPrice = itemView.findViewById(R.id.totalPrice);
            tvPeriod = itemView.findViewById(R.id.timePeriod);
            itemView.setTag(this);
        }
    }
}
