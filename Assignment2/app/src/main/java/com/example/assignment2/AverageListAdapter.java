package com.example.assignment2;

import android.app.Activity;
import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;

import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;

import java.util.List;

public class AverageListAdapter extends ArrayAdapter<FamilyMember> {

    private Activity context;

    private List<FamilyMember> toDoList;


    public AverageListAdapter(Activity context, List<FamilyMember> ToDoList) {
        super(context, R.layout.average_listview, ToDoList);
        this.context = context;
        this.toDoList = ToDoList;

    }


    public AverageListAdapter(Context context, int resource, List<FamilyMember> objects, Activity context1, List<FamilyMember> ToDoList) {
        super(context, resource, objects);
        this.context = context1;
        this.toDoList = ToDoList;

    }


    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull

    @Override

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.average_listview, null, true);
        TextView name = listViewItem.findViewById(R.id.nameTextView);
        TextView sys = listViewItem.findViewById(R.id.sysTextView);
        TextView dia = listViewItem.findViewById(R.id.diaTextView);
        TextView condition = listViewItem.findViewById(R.id.condition);

        FamilyMember members = toDoList.get(position);
        name.setText(members.getName());
        float s = members.getAvgSys(10);
        float d = members.getAvgDia(10);
        sys.setText(Float.toString(s));
        dia.setText(Float.toString(d));
        condition.setText(getCondition((int)s, (int)d));
        return listViewItem;
    }

    public String getCondition(int s, int d){
        if(s < 120 && d < 80){
            return "Normal";
        } else if((s >=120 && s <= 129) && d < 80) {
            return "Elevated";
        } else if((s >=130 && s <= 139) || (d >= 80 && d <= 89)){
            return "High blood pressure (Stage 1)";
        } else if((s >= 140 && s < 180) || (d >= 90 && d < 120)){
            return "High blood pressure (Stage 2)";
        } else {
            return "Hypertensive Crisis: Consult your doctor immediately";
        }

    }
    }