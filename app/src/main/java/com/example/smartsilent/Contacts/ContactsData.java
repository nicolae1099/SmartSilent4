package com.example.smartsilent.Contacts;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class ContactsData implements Parcelable {

    private ArrayList<String> contactsName;
    private ArrayList<String> phoneNumbers;

    protected ContactsData(Parcel in) {
        contactsName = new ArrayList<>();
        phoneNumbers = new ArrayList<>();
        in.readStringList(contactsName);
        in.readStringList(phoneNumbers);
    }

    public ContactsData() {
        contactsName = new ArrayList<>();
        phoneNumbers = new ArrayList<>();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeStringList(contactsName);
        dest.writeStringList(phoneNumbers);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ContactsData> CREATOR = new Creator<ContactsData>() {
        @Override
        public ContactsData createFromParcel(Parcel in) {
            return new ContactsData(in);
        }

        @Override
        public ContactsData[] newArray(int size) {
            return new ContactsData[size];
        }
    };

    public ArrayList<String> getContactsName() {
        return contactsName;
    }

    public void setContactsName(ArrayList<String> contactsName) {
        this.contactsName = contactsName;
    }

    public ArrayList<String> getPhoneNumbers() {
        return phoneNumbers;
    }

    public void setPhoneNumbers(ArrayList<String> phoneNumbers) {
        this.phoneNumbers = phoneNumbers;
    }
}
