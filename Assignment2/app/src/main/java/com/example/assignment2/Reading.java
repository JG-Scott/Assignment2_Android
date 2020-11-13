package com.example.assignment2;

import java.util.Date;

public class Reading {
    private Date readingDate;
    private int systolicReading;
    private int diastolicReading;
    private String condition;

    public Reading(int s, int d){
        readingDate = new Date();
        this.systolicReading = s;
        this.diastolicReading = d;
        condition = this.getCondition(s, d);
    }

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
