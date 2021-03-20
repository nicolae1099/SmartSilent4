package com.example.smartsilent;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 This class acts as a package containing all data within a user profile: contacts info,
 timezones and locations
 */

public class Profile implements Parcelable {

    private List<String> mContactsNames;
    private List<String> mContactsNumbers;

    // TODO: aici sa punem datele pentru time_zone
    private List<String> days;
    private List<String> time_intervals;

    public Profile() {
        mContactsNames = new ArrayList<>();
        mContactsNumbers = new ArrayList<>();
    }

    protected Profile(Parcel in) {
        this.mContactsNames = in.createStringArrayList();
        this.mContactsNumbers = in.createStringArrayList();
    }

    protected Profile(List<String> mContactsNames, List<String> mContactsNumbers) {
        this.mContactsNames = mContactsNames;
        this.mContactsNumbers = mContactsNumbers;
    }

    public static final Creator<Profile> CREATOR = new Creator<Profile>() {
        @Override
        public Profile createFromParcel(Parcel in) {
            return new Profile(in);
        }

        @Override
        public Profile[] newArray(int size) {
            return new Profile[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeStringList(mContactsNames);
        parcel.writeStringList(mContactsNumbers);
    }

    public List<String> getContactsNames() {
        return mContactsNames;
    }

    public void setContactsNames(List<String> mContactsNames) {
        this.mContactsNames = mContactsNames;
    }

    public List<String> getContactsNumbers() {
        return mContactsNumbers;
    }

    public void setContactsNumbers(List<String> mContactsNumbers) {
        this.mContactsNumbers = mContactsNumbers;
    }

    public void addContactName(String contactName) {
        mContactsNames.add(contactName);
    }

    public void addContactNumber(String contactNumber) {
        mContactsNumbers.add(contactNumber);
    }

}
