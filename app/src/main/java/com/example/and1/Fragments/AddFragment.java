package com.example.and1.Fragments;

import android.app.DatePickerDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.and1.Activity.HomeActivity;
import com.example.and1.R;
import com.example.and1.model.Bike;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static android.app.Activity.RESULT_OK;

public class AddFragment extends Fragment {
    Context context;
    Button btn, btnChoose;
    EditText location, price, from, until, name;
    DatePickerDialog datePickerDialog;
    ImageView imageView;

    Uri mImageUri;
    private static final String TAG = AddFragment.class.getName();
    private static final int PICK_IMAGE_REQUEST = 1;


    private FirebaseAuth mAuth;
    private FirebaseDatabase mDatabase;
    private DatabaseReference mDatabaseReference;
    private StorageReference mStorageRef;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       //TODO Create a bike model and pass it to firebase database
        final View rootView = inflater.inflate(R.layout.fragment_add, container, false);
        context = rootView.getContext();

        final Spinner spinner = (Spinner) rootView.findViewById(R.id.spinner);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(context,
                R.array.bikeTypes, android.R.layout.simple_spinner_item);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance();
        mDatabaseReference = mDatabase.getReference();
        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");

        Button btn =(Button) rootView.findViewById(R.id.create);
        btnChoose = rootView.findViewById(R.id.chooseFile);
        imageView = rootView.findViewById(R.id.imageChosen);
        btnChoose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });
        final EditText location = (EditText) rootView.findViewById(R.id.location);
        final EditText from = (EditText) rootView.findViewById(R.id.fromDate);
        final EditText until = (EditText) rootView.findViewById(R.id.untilDate);
        final EditText price = (EditText) rootView.findViewById(R.id.price);
        final EditText name = (EditText) rootView.findViewById(R.id.input_name);
        from.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                final int year = calendar.get(Calendar.YEAR);
                final int month = calendar.get(Calendar.MONTH);
                final int day = calendar.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String date  = day+"-"+(month+1)+"-"+year;
                        from.setText(date);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });

        until.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar calendar = Calendar.getInstance();
                final int year = calendar.get(Calendar.YEAR);
                final int month = calendar.get(Calendar.MONTH);
                final int day = calendar.get(Calendar.DAY_OF_MONTH);

                datePickerDialog = new DatePickerDialog(context, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        String date  = day+"-"+(month+1)+"-"+year;
                        until.setText(date);
                    }
                }, year, month, day);
                datePickerDialog.show();
            }
        });
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userId = mAuth.getCurrentUser().getUid();
                String type = spinner.getSelectedItem().toString();
                String loc = location.getText().toString();
                String start = from.getText().toString();
                String finish = until.getText().toString();
                String pr = price.getText().toString();
                String bikeName = name.getText().toString();

                double priceParsed = Double.parseDouble(pr);

                mDatabaseReference = mDatabase.getReference().child("bikes");
                if(CheckDates(start,finish)){
                    String key = mDatabaseReference.push().getKey();
                Bike bike = new Bike(userId, key, type, loc, start, finish, priceParsed, bikeName, "");
                uploadFile(bike);
                }
                else {
                    Toast.makeText(context, "Start date cannot be after the end date!!",Toast.LENGTH_SHORT).show();
                }

            }
        });
        return rootView;
    }

    private void openFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null
            && data.getData()!= null){
            mImageUri = data.getData();

            Picasso.with(context).load(mImageUri).into(imageView);

        }
    }

    private String getFileExtension(Uri uri){
        ContentResolver cR = context.getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(cR.getType(uri));
    }

    private void uploadFile(Bike bikes){
        if(mImageUri != null){
            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis()
                    + "." + getFileExtension(mImageUri));
            fileReference.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                           // mProgressBar.setProgress(0);
                        }
                    },500);

                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String url = uri.toString();
                            bikes.setnImageUrl(url);
                            mDatabaseReference.child(bikes.getBikeId()).setValue(bikes).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(context, "You created the bike", Toast.LENGTH_SHORT).show();
                                        Intent intToMain = new Intent(context, HomeActivity.class);
                                        startActivity(intToMain);
                                    }
                                    else{
                                        Log.d(TAG, task.getException().getMessage());
                                    }
                                }
                            });
                        }
                    });

                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(context, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            })
            .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onProgress(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                  double progress = (100.0 * taskSnapshot.getBytesTransferred()/taskSnapshot.getTotalByteCount());
                  //mProgressBar.setProgress((int) progress);
                }
            });
        }else {
            Toast.makeText(context, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean CheckDates(String startDate, String endDate) {

        SimpleDateFormat dfDate = new SimpleDateFormat("dd-MM-yyyy");

        boolean b = false;

        try {
            if (dfDate.parse(startDate).before(dfDate.parse(endDate))) {
                b = true;  // If start date is before end date.

            } else if (dfDate.parse(startDate).equals(dfDate.parse(endDate))) {
                b = true;  // If two dates are equal.
            } else {
                b = false; // If start date is after the end date.

            }
        } catch (ParseException e) {
            e.printStackTrace();
        }

        return b;
    }
}
