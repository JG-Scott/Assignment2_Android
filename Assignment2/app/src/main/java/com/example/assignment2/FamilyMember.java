package com.example.assignment2;

import java.util.ArrayList;

public class FamilyMember {
    private String name;
    private ArrayList<Reading> readings;

    public FamilyMember(String name) {
        this.name = name;
        readings = new ArrayList<>();
    }

    public FamilyMember() {};

    public ArrayList<Reading> getReadings() {
        return readings;
    }

    public void addReading(Reading reading) {
        this.readings.add(reading);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public float getAvgSys(int month){
        float sum = 0;
        float count = 0;
        for(Reading r : readings){
            if(r.getReadingDate().getMonth() == month){
                sum += (float)r.getSystolicReading();
                count++;
            }
        }
        return sum/count;
    }

    public float getAvgDia(int month){
        float sum = 0;
        float count = 0;
        for(Reading r : readings){
            if(r.getReadingDate().getMonth() == month){
                sum += (float)r.getDiastolicReading();
                count++;
            }
        }
        return sum/count;
    }

}
