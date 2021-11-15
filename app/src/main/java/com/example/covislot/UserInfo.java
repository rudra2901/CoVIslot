package com.example.covislot;

public class UserInfo {
    public String number;
    public String name;
    public int age;
    public boolean firstDose;

    public UserInfo(){
    }

    public UserInfo(String n, String nm, int a, boolean f) {
        this.number = n;
        this.name = nm;
        this.age = a;
        this.firstDose = f;
    }

}
