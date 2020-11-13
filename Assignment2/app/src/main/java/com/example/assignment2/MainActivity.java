package com.example.assignment2;

import androidx.appcompat.app.AppCompatActivity;

import android.bluetooth.BluetoothClass;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    String n;
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


    }

    public void sendReading(View view){
        EditText name = findViewById(R.id.Name);
        EditText dia = findViewById(R.id.Dia);
        EditText sys = findViewById(R.id.Sys);
        n = name.getText().toString();
        int d = Integer.parseInt(dia.getText().toString());
        int s = Integer.parseInt(sys.getText().toString());
           user.addReading(n, s, d);
        String uniqueID;
        if(sharedPref.getString("user_id", "null").equals("null")){
            uniqueID = UUID.randomUUID().toString();
        } else {
            uniqueID = sharedPref.getString("user_id", "default if empty");
        }

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("Users").child(uniqueID);

        myRef.setValue(user);
    }
}   