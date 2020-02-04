package com.example.and1.Fragments;

import android.os.Bundle;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.and1.R;
import com.example.and1.model.Bike;
import com.squareup.picasso.Picasso;


public class ListItemDetail extends Fragment {

    Context context;
    TextView textUserId, bikeType, available, until, priceForBike;
    ImageView imageView;
    Button btnRent;
    private FragmentActivity myContext;

    public ListItemDetail(){

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_item_detail, container, false);
        context = rootView.getContext();
        Bundle bundle = this.getArguments();
        if(bundle != null) {
            final Bike selectedBike = (Bike) bundle.getParcelable("Object");

            textUserId = rootView.findViewById(R.id.textViewUser);
            bikeType = rootView.findViewById(R.id.bikeType);
            available = rootView.findViewById(R.id.available);
            until = rootView.findViewById(R.id.until);
            priceForBike = rootView.findViewById(R.id.priceForBike);
            imageView = rootView.findViewById(R.id.imageBike);

            textUserId.setText(selectedBike.getName());
            bikeType.setText("Type of bike: "+ selectedBike.getType());
            available.setText("Available from: " + selectedBike.getFrom());
            until.setText("Until: " + selectedBike.getUntil());
            priceForBike.setText("Price for one day: " + selectedBike.getPrice().toString());
            Picasso.with(context)
                    .load(selectedBike.getnImageUrl())
                    .fit()
                    .centerCrop()
                    .into(imageView);


            btnRent = rootView.findViewById(R.id.rent_it);
            btnRent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("SelectedBike", selectedBike);

                    Fragment selectedFragment = new OrderCreate();
                    selectedFragment.setArguments(bundle);

                    FragmentManager fm = myContext.getSupportFragmentManager();
                    FragmentTransaction ft = fm.beginTransaction();

                    fm.beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
                }
            });

        }

        return rootView;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        myContext = (FragmentActivity) context;
        super.onAttach(context);
    }

}


