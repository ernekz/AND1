package com.example.and1.Fragments;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.and1.Activity.HomeActivity;
import com.example.and1.R;
import com.example.and1.model.Balance;
import com.example.and1.model.Bike;
import com.example.and1.model.Order;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;


public class OrderCreate extends Fragment {

    Context context;
    private FragmentActivity myContext;
    Button btnPay;
    EditText days;
    TextView bikeId, totalPrice;
    private static final String TAG = AddFragment.class.getName();


    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseReference, mBikeReference, mBalanceReference, mLoanerReference;

    public OrderCreate() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        final View rootView = inflater.inflate(R.layout.fragment_order, container, false);
        context = rootView.getContext();

        Bundle bundle = this.getArguments();
        if(bundle != null) {
            final Bike selectedBike = (Bike) bundle.getParcelable("SelectedBike");

            days = rootView.findViewById(R.id.days);
            bikeId = rootView.findViewById(R.id.bikeId);

            days.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    totalPrice = rootView.findViewById(R.id.totalPrice);
                    String daysOfRent = days.getText().toString();
                    int noOfDays = getBlockId(daysOfRent);

                    double price = noOfDays * selectedBike.getPrice();
                    totalPrice.setText("Total price: " + String.valueOf(price));

                }
            });

            mAuth = FirebaseAuth.getInstance();
            mDatabase = FirebaseDatabase.getInstance();
            mDatabaseReference = mDatabase.getReference();
            mBikeReference = mDatabase.getReference();
            mBalanceReference = mDatabase.getReference();
            mLoanerReference = mDatabase.getReference();



            bikeId.setText("Bike name: " + selectedBike.getName());

            btnPay = rootView.findViewById(R.id.btnPay);

            btnPay.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    final String bikeID = selectedBike.getBikeId();
                    final String currentUser = mAuth.getCurrentUser().getUid();
                    String period = days.getText().toString();
                    Double price = selectedBike.getPrice();
                    mDatabaseReference = mDatabase.getReference().child("Orders");

                    int timePeriod = Integer.parseInt(period);
                    final Double totalPriceFor = timePeriod * price;
                    final Order orderCreated = new Order(currentUser,bikeID, period, totalPriceFor);

                    mBikeReference = mDatabase.getReference().child("bikes").child(bikeID).child("taken");
                    mBalanceReference = mDatabase.getReference().child("balance").child(currentUser);
                    mLoanerReference = mDatabase.getReference().child("balance").child(selectedBike.getUserId());

                    mBalanceReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            final Balance bal = dataSnapshot.getValue(Balance.class);
                            if(totalPriceFor<= bal.getmoney()) {

                                mDatabaseReference.child(currentUser).setValue(orderCreated).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                      if(task.isSuccessful()) {
                                            mBikeReference.setValue(true);
                                            mBalanceReference.child("money").setValue(bal.getmoney() - totalPriceFor);
                                            updateLoanerMoney(totalPriceFor);
                                            Toast.makeText(rootView.getContext(), "You have rented the bike", Toast.LENGTH_SHORT).show();
                                            Intent intToMain = new Intent(context, HomeActivity.class);
                                            startActivity(intToMain);
                                        }

                                      else{
                                          Log.d(TAG, task.getException().getMessage());
                                      }
                                    }
                                });
                            }
                            else {
                                Toast.makeText(rootView.getContext(), "You don't have enough money. Please top-up your account!", Toast.LENGTH_SHORT).show();
                            }
                        }
                        public void updateLoanerMoney(double total){
                            mLoanerReference.addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                    final Balance loaner = dataSnapshot.getValue(Balance.class);
                                    mLoanerReference.child("money").setValue(loaner.getmoney() + total);
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError databaseError) {

                                }
                            });
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {

                        }
                    });

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


    @NonNull
    private Integer getBlockId(String id) {
        Integer blockId= 0;
        try {
            blockId= Integer.parseInt(id);
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }
        return blockId;
    }

}
