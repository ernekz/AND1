package com.example.and1.Fragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.and1.R;
import com.example.and1.model.Bike;
import com.squareup.picasso.Picasso;

import java.util.List;

public class RecyclerViewAdapter  extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {


    Context context;
    List<Bike> bikes;
    private static View.OnClickListener mOnItemClickListener;

    public RecyclerViewAdapter(Context context, List<Bike> bikes) {
        this.context = context;
        this.bikes = bikes;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_bike, parent,false);

        MyViewHolder vHolder = new MyViewHolder(view);

        return vHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        holder.tvName.setText(bikes.get(position).getName());
        holder.tvPrice.setText(bikes.get(position).getPrice().toString());
        holder.tvUntil.setText(bikes.get(position).getUntil());
        Picasso.with(context)
                .load(bikes.get(position).getnImageUrl())
                .fit()
                .centerCrop()
                .into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return bikes.size();
    }

    public void setOnItemClickListener(View.OnClickListener itemClickListener){
        mOnItemClickListener = itemClickListener;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder{

        private TextView tvName;
        private TextView tvUntil;
        private TextView tvPrice;
        private ImageView imageView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            tvName = itemView.findViewById(R.id.bike_name);
            tvUntil = itemView.findViewById(R.id.until_bike);
            tvPrice = itemView.findViewById(R.id.priceForDay);
            imageView = itemView.findViewById(R.id.img_bike);


            itemView.setTag(this);
            itemView.setOnClickListener(mOnItemClickListener);
        }
    }

}
