package com.example.and1.Fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.and1.Activity.MainActivity;
import com.example.and1.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class ProfileFragment extends Fragment {

    Button btnLogout, btnBalance;
    Context context;
    EditText balance;
    TextView currentBalance;
    Button addBalance;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseReference;

    private FragmentActivity myContext;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        final View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        context = rootView.getContext();

        btnLogout = rootView.findViewById(R.id.loggout);

       //btnBalance = rootView.findViewById(R.id.balance);

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intToMain = new Intent(rootView.getContext(), MainActivity.class);
                startActivity(intToMain);
            }
        });

        balance = rootView.findViewById(R.id.topUpBalance);
        currentBalance = rootView.findViewById(R.id.currentBalance);
        addBalance = rootView.findViewById(R.id.addBalance);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference();



        String key = mAuth.getCurrentUser().getUid();
        mDatabaseReference = mDatabase.getReference().child("balance").child(key).child("money");
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //String currentBal = (String) dataSnapshot.getValue().toString();
                //currentBalance.setText(currentBal);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
        addBalance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        Object currentBal = dataSnapshot.getValue();


                        double ba = Double.parseDouble(balance.getText().toString());
                        double add = Double.parseDouble(currentBal.toString());
                        double newValue = ba + add;
                        mDatabaseReference.setValue(newValue);
                        balanceSet();
                        Toast.makeText(rootView.getContext(), "You top-up you balance: " + ba + " dkk", Toast.LENGTH_SHORT).show();

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
            }
        });



        return rootView;
    }

    @Override
    public void onStart() {
        balanceSet();
        super.onStart();


    }

    public void balanceSet(){
        String key = mAuth.getCurrentUser().getUid();
        mDatabaseReference = mDatabase.getReference().child("balance").child(key).child("money");
        mDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Object currentBal = dataSnapshot.getValue();

                currentBalance.setText(currentBal.toString());

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        myContext = (FragmentActivity) context;
        super.onAttach(context);
    }
}

