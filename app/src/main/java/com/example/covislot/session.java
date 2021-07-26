package com.example.covislot;

public class session {

    private String sID, date;
    private int avlbCapacity, minAge;
    private String vName;
    //private String slot[];

    public session( String id, String dt, int cap, int age, String name) {
        sID = id;
        date = dt;
        avlbCapacity = cap;
        minAge = age;
        vName = name;
    }

    public String getsID() {
        return sID;
    }
    public String getDate() {
        return date;
    }
    public int getAvlbCapacity() {
        return avlbCapacity;
    }
    public int getMinAge() {
        return minAge;
    }
    public String getvName() {
        return vName;
    }
    /*
    public String[] getSlot() {
        return slot;
    }
    */
}
