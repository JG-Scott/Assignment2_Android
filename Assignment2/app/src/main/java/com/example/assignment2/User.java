package com.example.assignment2;

import androidx.annotation.RequiresPermission;

import java.util.ArrayList;

public class User {
    private ArrayList<FamilyMember> Family;

    public void User(){
        Family = new ArrayList<>();
    }

    public ArrayList<FamilyMember> getFamily() {
        return Family;
    }

    public void addFamily(FamilyMember person) {
        this.Family.add(person);
    }

    public void addReading(String name, int s, int d){
        boolean memberExists = false;
        for(FamilyMember f : Family){
            if(f.getName().equals(name)){
             f.addReading(new Reading(s, d));
             memberExists = true;
            }
        }
        if(!memberExists){
            FamilyMember f = new FamilyMember(name);
            f.addReading(new Reading(s, d));
            Family.add(f);
        }
    }
}
