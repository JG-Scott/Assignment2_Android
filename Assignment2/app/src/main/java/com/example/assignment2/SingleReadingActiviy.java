package com.example.assignment2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class SingleReadingActiviy extends AppCompatActivity {

    DatabaseReference dbRef;
    ListView readingListView;
    SharedPreferences sharedPref;
    String familyMember;
    String uniqueID;
    FamilyMember memberToGetReadings;
    int size;
    DatabaseReference newDbRef;
    String[] uniqueIdList = new String[100];

    List<Reading> readingList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single_reading_activiy);
        familyMember = getIntent().getStringExtra("FamilyMember");

        sharedPref = getSharedPreferences("myPref", MODE_PRIVATE);
        uniqueID = sharedPref.getString("user_id", "default if empty");

        dbRef = FirebaseDatabase.getInstance().getReference("Users").child(uniqueID);
        readingListView = findViewById(R.id.readingListView);

        readingList = new ArrayList<Reading>();


        readingListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Reading reading = readingList.get(position);
                showUpdateDialog(reading.getId(), reading.getSystolicReading(), reading.getDiastolicReading());
                return false;
            }
        });
    }

    protected void onStart() {
        super.onStart();
        refreshReadings();
    }

    public void refreshReadings() {
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                int i = 0;
                for(DataSnapshot ds : dataSnapshot.child("family").getChildren()) {
                    String child = String.valueOf(i);
                    i++;
                    if(ds.child("name").getValue().toString().equals(familyMember)) {
                        newDbRef = dbRef.child("family").child(child).child("readings");
                        newDbRef.addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                                Arrays.fill(uniqueIdList, null);
                                readingList.clear();
                                for (DataSnapshot ds : dataSnapshot.getChildren()) {
                                    Reading reading = ds.getValue(Reading.class);
                                    readingList.add(reading);
                                    String k = ds.getKey();
                                    int key = Integer.parseInt(k);
                                    System.out.println(ds.getKey());
                                    uniqueIdList[key] = (ds.child("id").getValue().toString());
                                }
                                ReadingListAdapter adapter = new ReadingListAdapter(SingleReadingActiviy.this, readingList);
                                readingListView.setAdapter(adapter);
                            }

                            @Override
                            public void onCancelled(@NonNull DatabaseError databaseError) {

                            }
                        });
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });

    }

    private void updateReading(final String id, int systolic, int diastolic) {
        int x = 0;
        for (int i = 0; i < uniqueIdList.length; i++) {
            if (id.equals(uniqueIdList[i])) {
                x = i;
                break;
            }
        }
        DatabaseReference updateDbRef = newDbRef.child(String.valueOf(x));
        Reading reading = new Reading(systolic, diastolic);
        Task setValueTask = updateDbRef.setValue(reading);
        setValueTask.addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                Toast.makeText(SingleReadingActiviy.this,
                        "Reading Updated", Toast.LENGTH_LONG).show();
            }
        });
        setValueTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SingleReadingActiviy.this,
                        "Something went wrong" + e.toString(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void deleteReading(final String id) {
        int x = 0;
        for (int i = 0; i < uniqueIdList.length; i++) {
            if (id.equals(uniqueIdList[i])) {
                x = i;
                break;
            }
        }

        DatabaseReference deleteDbRef = newDbRef.child(String.valueOf(x));

        Task setRemoveTask = deleteDbRef.removeValue();
        setRemoveTask.addOnSuccessListener(new OnSuccessListener() {
            @Override
            public void onSuccess(Object o) {
                Toast.makeText(SingleReadingActiviy.this,
                        "Reading Deleted.",Toast.LENGTH_LONG).show();
            }
        });

        setRemoveTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SingleReadingActiviy.this,
                        "Something went wrong.\n" + e.toString(),
                        Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showUpdateDialog(final String id, final int systolic, int diastolic) {
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);

        LayoutInflater inflater = getLayoutInflater();
        String s = Integer.toString(systolic);

        final View dialogView = inflater.inflate(R.layout.update_dialog, null);
        dialogBuilder.setView(dialogView);

        final EditText editTextSystolic = dialogView.findViewById(R.id.editTextSystolic);
        editTextSystolic.setText(s);

        String d = Integer.toString(diastolic);
        final EditText editTextDiastolic = dialogView.findViewById(R.id.editTextDiastolic);
        editTextDiastolic.setText(d);

        final Button btnUpdate = dialogView.findViewById(R.id.btnUpdate);

        dialogBuilder.setTitle("Update Reading ");

        final AlertDialog alertDialog = dialogBuilder.create();
        alertDialog.show();

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String sys = editTextSystolic.getText().toString().trim();
                int s = Integer.parseInt(sys);
                String dys = editTextDiastolic.getText().toString().trim();
                int d = Integer.parseInt(dys);

                if (TextUtils.isEmpty(sys)) {
                    editTextSystolic.setError("First Name is required");
                    return;
                } else if (TextUtils.isEmpty(dys)) {
                    editTextDiastolic.setError("Last Name is required");
                    return;
                }

                updateReading(id, s, d);
                alertDialog.dismiss();
            }
        });

        final Button btnDelete = dialogView.findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteReading(id);
                alertDialog.dismiss();
            }
        });

    }
}