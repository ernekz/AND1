package com.example.and1.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.and1.R;
import com.example.and1.model.Bike;
import com.example.and1.model.Order;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

public class HistoryFragment extends Fragment {
    Context context;
    DatabaseReference databaseReference;
    ArrayList<Bike> arrayList = new ArrayList<>();

    RecyclerViewAdapter rcAdapter;
    RecyclerView recyclerView;

    private FragmentActivity myContext;

    private View.OnClickListener onItemClickListener = new View.OnClickListener(){
        @Override
        public void onClick(View v) {
            RecyclerView.ViewHolder viewHolder = (RecyclerView.ViewHolder) v.getTag();
            int position = viewHolder.getAdapterPosition();
            Bike thisItem = arrayList.get(position);
            Bundle bundle = new Bundle();
            bundle.putParcelable("Object", thisItem);
            Fragment selectedFragment = new ListItemDetail();

            selectedFragment.setArguments(bundle);
            FragmentManager fm = myContext.getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            fm.beginTransaction().replace(R.id.fragment_container, selectedFragment).commit();
            //Toast.makeText(context, "You Clicked" + thisItem.getBikeId(), Toast.LENGTH_SHORT).show();
        }
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        databaseReference = FirebaseDatabase.getInstance().getReference("Orders");
        databaseReference.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Bike value = dataSnapshot.getValue(Bike.class);
                arrayList.add(value);
                rcAdapter.notifyDataSetChanged();
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_history, container, false);
        context = rootView.getContext();

        recyclerView = rootView.findViewById(R.id.history_recylerView);
        rcAdapter = new RecyclerViewAdapter(rootView.getContext(),arrayList);
        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(rcAdapter);
        rcAdapter.setOnItemClickListener(onItemClickListener);

        return rootView;

    }

    @Override
    public void onAttach(@NonNull android.content.Context context) {
        myContext = (FragmentActivity) context;
        super.onAttach(context);
    }

    @Override
    public void onStart() {
        super.onStart();
    }
}
