package com.example.and1.Fragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.and1.R;
import com.example.and1.model.Bike;
import com.example.and1.model.Order;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class RecyclerViewAdapter  extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> implements Filterable {


    Context context;
    List<Bike> bikes;
    List<Bike> getBikesFilter;

    private static View.OnClickListener mOnItemClickListener;

    public RecyclerViewAdapter(Context context, List<Bike> bikes) {
        this.context = context;
        this.bikes = bikes;
        this.getBikesFilter = bikes;
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

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                FilterResults filterResults = new FilterResults();
                if(charSequence == null || charSequence.length() == 0){
                    filterResults.values = getBikesFilter;
                    filterResults.count = getBikesFilter.size();
                }else{
                    String str = charSequence.toString().toLowerCase();
                    List<Bike> bikeModels = new ArrayList<>();
                    for(Bike bikeModel: getBikesFilter){
                        if(bikeModel.getName().toLowerCase().contains(str)|| bikeModel.getType().toLowerCase().contains(str)){
                            bikeModels.add(bikeModel);
                        }
                    }
                    filterResults.values = bikeModels;
                    filterResults.count = bikeModels.size();
                }
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                if(filterResults.values !=null) {
                    getBikesFilter = (List<Bike>) filterResults.values;
                    notifyDataSetChanged();
                }
            }
        };
        return null;
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
