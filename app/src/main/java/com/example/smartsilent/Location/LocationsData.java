package com.example.smartsilent.Location;

import android.os.Parcel;
import android.os.Parcelable;

public class LocationsData implements Parcelable {
    protected LocationsData(Parcel in) {
    }

    public static final Creator<LocationsData> CREATOR = new Creator<LocationsData>() {
        @Override
        public LocationsData createFromParcel(Parcel in) {
            return new LocationsData(in);
        }

        @Override
        public LocationsData[] newArray(int size) {
            return new LocationsData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
    }
}
