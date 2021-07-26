package com.example.covislot;

/**
 * This class {@link vaccineFee} object will contain information of vaccines available.
 */
public class vaccineFee {
    private String vName, fee;

    public vaccineFee(String name, String f) {
        vName = name;
        fee = f;
    }

    public String getFee() {
        return fee;
    }

    public String getvName() {
        return vName;
    }
}

/**
 * This class {@link vaccineFee} object will contain information of vaccines available.
 */