package com.example.smartsilent.Contacts;

public class ContactModel {

    private String name, phone_number;
    private int isChecked = -1;

    public ContactModel(String title, String phoneNumber) {
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
        isChecked = 1;
    }

    public void resetCheck() {
        isChecked = -1;
    }

    public void uncheck() {
        isChecked = 0;
    }

    public int getCheck() {
        return isChecked;
    }
}

