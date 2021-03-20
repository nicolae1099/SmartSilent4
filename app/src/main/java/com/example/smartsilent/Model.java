package com.example.smartsilent;

public class Model {

    private String name, phone_number;
    private boolean isChecked = false;

    public Model(String title, String phoneNumber) {
        this.name = title;
        this.phone_number = phoneNumber;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public void check() {
        isChecked = true;
    }

    public void uncheck() {
        isChecked = false;
    }

    public boolean getCheck() {
        return isChecked;
    }
}

