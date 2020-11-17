package com.example.assignment2;

import java.util.Date;
import java.util.UUID;


public class Reading {
    private Date readingDate;
    private int systolicReading;
    private int diastolicReading;
    private String condition;
    private String id;

    public Reading(int s, int d){
        readingDate = new Date();
        this.systolicReading = s;
        this.diastolicReading = d;
        condition = this.getCondition(s, d);
        createSalt();
    }

    public Reading(){}

    public int getSystolicReading() {
        return systolicReading;
    }

    public void setSystolicReading(int systolicReading) {
        this.systolicReading = systolicReading;
    }

    public int getDiastolicReading() {
        return diastolicReading;
    }

    public void setDiastolicReading(int diastolicReading) {
        this.diastolicReading = diastolicReading;
    }

    public Date getReadingDate() {
        return readingDate;
    }

    public String getCondition() {
        return condition;
    }

    public String getId() {return id;};

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
    public void createSalt() {
        String ts = String.valueOf(System.currentTimeMillis());
        String rand = UUID.randomUUID().toString();
        id = rand;
    }

}
