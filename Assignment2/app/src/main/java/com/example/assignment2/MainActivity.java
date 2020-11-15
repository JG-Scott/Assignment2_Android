package com.example.assignment2;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothClass;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    String name;
    int s;
    int d;
    FirebaseDatabase database;
    DatabaseReference myRef;
    SharedPreferences sharedPref;
    User user = new User();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Spinner spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.familyMember, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);

        // get or create SharedPreferences
        sharedPref = getSharedPreferences("myPref", MODE_PRIVATE);
        database = FirebaseDatabase.getInstance();
        Button send = findViewById(R.id.button);
        send.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                sendReading();
            }
        });


        Button average = findViewById(R.id.buttonAVG);
        average.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                toAverageScreen();
            }
        });
    }

    public void sendReading(){
        Spinner spinner = findViewById(R.id.spinner);
        EditText dia = findViewById(R.id.Dia);
        EditText sys = findViewById(R.id.Sys);
        name = spinner.getSelectedItem().toString();
        d = Integer.parseInt(dia.getText().toString());
        s = Integer.parseInt(sys.getText().toString());
        String uniqueID;
        if(sharedPref.getString("user_id", null) == null){ // if user doesnt exist.
            uniqueID = UUID.randomUUID().toString();
            sharedPref.edit().putString("user_id", uniqueID).apply();


            myRef = database.getReference("Users").child(uniqueID);
            user.addReading(name, s, d);
            myRef.setValue(user);

        } else { //if user exists
            uniqueID = sharedPref.getString("user_id", "default if empty");
            existingUserSend(uniqueID);
        }

    }

    public void existingUserSend(String id){

        myRef = database.getReference("Users").child(id);
        myRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                // This method is called once with the initial value and again
                // whenever data at this location is updated.
                User user = dataSnapshot.getValue(User.class);

                Log.v("User", user.getFamily().get(0).getName());
                user.addReading(name, s, d);
                myRef.setValue(user);
            }
            @Override
            public void onCancelled(DatabaseError error) {
                // Failed to read value
                Log.w("hmm", "Failed to read value.", error.toException());
            }
        });
    }


    public void toAverageScreen(){
        Intent i = new Intent(getApplicationContext(), AverageActivity.class);
        startActivity(i);
    }
}   