package com.example.covislot;

/**
 * This class {@link centres} object will contain information of vaccination centre nearby.
 */
public class centres {

    /**
     * The information of centres.
     */
    private int cId;
    private String cName, cAdd, cState, cDist, cBlock, cPin;
    private float lat, lon;
    private String startTime, endTime;

    public centres(int id, String name, String add, String s, String d, String b, String p, float l1, float l2, String sT, String eT) {
        cId = id;
        cName = name;
        cAdd = add;
        cState = s;
        cDist = d;
        cBlock = b;
        cPin = p;
        lat = l1;
        lon = l2;
        startTime = sT;
        endTime = eT;
    }

    public int getcId() {
        return cId;
    }

    public String getcName() {
        return cName;
    }

    public String getcAdd() {
        return cAdd;
    }

    public String getcState() {
        return cState;
    }

    public String getcDist() {
        return cDist;
    }

    public String getcBlock() {
        return cBlock;
    }

    public String getcPin() {
        return cPin;
    }

    public float getLat() {
        return lat;
    }

    public float getLon() {
        return lon;
    }

    public String getStartTime() {
        return startTime;
    }

    public String getEndTime() {
        return endTime;
    }
}
