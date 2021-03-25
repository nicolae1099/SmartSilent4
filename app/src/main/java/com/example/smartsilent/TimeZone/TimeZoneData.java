package com.example.smartsilent.TimeZone;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class TimeZoneData implements Parcelable {

    ArrayList<boolean[]> data;
    private List<String> days;

    public static final int NUM_DAYS = 7;
    public static final int NUM_HOURS = 24;

    public TimeZoneData() {
        data = new ArrayList<>();

        for (int i = 0; i < NUM_DAYS; i++) {
            data.add(new boolean[NUM_HOURS]);
        }
        days = new ArrayList<>(Arrays.asList("Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"));
    }

    public TimeZoneData(Parcel in) {
        data = new ArrayList<>();

        for (int i = 0; i < NUM_DAYS; i++) {
            data.add(new boolean[NUM_HOURS]);
        }

        days = new ArrayList<>(Arrays.asList("Sunday","Monday","Tuesday","Wednesday","Thursday","Friday","Saturday"));


        for(int i = 0; i < NUM_DAYS; i++) {
            in.readBooleanArray(data.get(i));
        }
        in.readStringList(days);
    }

    public List<String> getDays() {
        return days;
    }

    public void putData(int day, int hour, boolean value) {
        if (day < 1 || day > 7 || hour < 1 || hour > 24) return;
        data.get(day)[hour] = value;
    }

    public boolean getData(int day, int hour) {
        if (day < 1 || day > 7 || hour < 1 || hour > 24) return false;
        return data.get(day)[hour];
    }


    @Override
    public void writeToParcel(Parcel dest, int flags) {
        for(int i = 0;  i < NUM_DAYS; i++) {
            dest.writeBooleanArray(data.get(i));
        }
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<TimeZoneData> CREATOR = new Creator<TimeZoneData>() {
        @Override
        public TimeZoneData createFromParcel(Parcel in) {
            return new TimeZoneData(in);
        }

        @Override
        public TimeZoneData[] newArray(int size) {
            return new TimeZoneData[size];
        }
    };

    public ArrayList<boolean[]> getData() {
        return data;
    }

    public void setData(ArrayList<boolean[]> data) {
        this.data = data;
    }
}
