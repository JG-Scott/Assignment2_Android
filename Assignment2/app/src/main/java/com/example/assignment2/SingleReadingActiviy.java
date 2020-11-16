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
import java.util.List;


public class SingleReadingActiviy extends AppCompatActivity {

    DatabaseReference dbRef;
    ListView readingListView;
    SharedPreferences sharedPref;
    String familyMember;
    String uniqueID;
    FamilyMember memberToGetReadings;

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
                showUpdateDialog(reading.getSystolicReading(), reading.getDiastolicReading());
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
                User member = dataSnapshot.getValue(User.class);
                ArrayList<FamilyMember> family;
                family = member.getFamily();

                for (int i = 0; i < family.size(); i++) {
                    if (family.get(i).getName().equals(familyMember)) {
                        memberToGetReadings = family.get(i);
                        break;
                    }
                }
                readingList = memberToGetReadings.getReadings();
                ReadingListAdapter adapter = new ReadingListAdapter(SingleReadingActiviy.this, memberToGetReadings.getReadings());
                readingListView.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }
        });
    }

    private void updateReading(int systolic, int diastolic) {
        Reading reading = new Reading(systolic, diastolic);
        Task setValueTask = dbRef.setValue(reading);
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

    private void deleteReading() {

    }


    private void showUpdateDialog(final int systolic, int diastolic) {
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

                updateReading(s, d);

                alertDialog.dismiss();
            }
        });

        final Button btnDelete = dialogView.findViewById(R.id.btnDelete);
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                alertDialog.dismiss();
            }
        });

    }





}