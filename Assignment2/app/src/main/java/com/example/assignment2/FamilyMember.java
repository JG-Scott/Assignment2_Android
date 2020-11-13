package com.example.assignment2;

import java.util.ArrayList;

public class FamilyMember {
    private String name;
    private ArrayList<Reading> readings;

    public FamilyMember(String name) {
        this.name = name;
        readings = new ArrayList<>();
    }

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
}
