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

public class ReadingListAdapter extends ArrayAdapter<Reading> {
    private Activity context;

    private List<Reading> readingList;

    public ReadingListAdapter(Activity context, List<Reading> readingList) {
        super(context, R.layout.reading_listview, readingList);
        this.context = context;
        this.readingList = readingList;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @NonNull

    @Override

    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.reading_listview, null, true);
        TextView date = listViewItem.findViewById(R.id.dateTextView);
        TextView sys = listViewItem.findViewById(R.id.readingSysTextView);
        TextView dia = listViewItem.findViewById(R.id.readingDiaTextView);
        TextView condition = listViewItem.findViewById(R.id.readingCondition);

        Reading readings = readingList.get(position);

        date.setText(readings.getReadingDate().toString());
        float s = readings.getSystolicReading();
        float d = readings.getDiastolicReading();
        sys.setText(Float.toString(s));
        dia.setText(Float.toString(d));
        condition.setText(readings.getCondition());
        return listViewItem;
    }


}
