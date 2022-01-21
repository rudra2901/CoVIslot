package com.example.covislot;

public class UserInfo {
    private String number;
    private String name;
    private int age;
    private boolean firstDose;

    public UserInfo(){
    }

    public UserInfo(String n, String nm, int a, boolean f) {
        this.number = n;
        this.name = nm;
        this.age = a;
        this.firstDose = f;
    }

    public String getNumber() {
        return number;
    }

    public String getName() {
        return name;
    }
    public int getAge() {
        return age;
    }
    public boolean isFirstDose() {
        return firstDose;
    }
}
