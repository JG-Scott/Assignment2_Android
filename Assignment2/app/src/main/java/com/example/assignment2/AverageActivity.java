package com.example.assignment2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.arch.core.internal.FastSafeIterableMap;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class AverageActivity extends AppCompatActivity {


    DatabaseReference databaseStudents;

    ListView avgListview;
    SharedPreferences sharedPref;
    List<FamilyMember> familyMemberList;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_average);

        sharedPref = getSharedPreferences("myPref", MODE_PRIVATE);
        String uniqueID = sharedPref.getString("user_id", "default if empty");

        databaseStudents = FirebaseDatabase.getInstance().getReference("Users").child(uniqueID);

        avgListview = findViewById(R.id.avgListview);

        avgListview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                FamilyMember f = familyMemberList.get(position);
                Intent i = new Intent(getApplicationContext(), SingleReadingActiviy.class);
                String name = f.getName();
                i.putExtra("FamilyMember", name);
                startActivity(i);
                return false;
            }
        });

    }

    protected void onStart() {
        super.onStart();
        refreshAvgs();
    }

    public void refreshAvgs() {

        databaseStudents.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                User member = dataSnapshot.getValue(User.class);
                familyMemberList = member.getFamily();
                AverageListAdapter adapter = new AverageListAdapter(AverageActivity.this, member.getFamily());
                avgListview.setAdapter(adapter);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) { }

        });
    }
}