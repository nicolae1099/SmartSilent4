package com.example.smartsilent;

public class Model {

    private String name, phone_number;
    private int img;
    boolean isChecked = false;

    public Model(String title, String data, int img) {
        this.name = title;
        this.phone_number = data;
        this.img = img;
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

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
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

