package com.example.smartsilent;


import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;
import android.util.Pair;

import com.example.smartsilent.Contacts.ContactsData;
import com.example.smartsilent.Contacts.ContactsDatabase;
import com.example.smartsilent.Contacts.ContactsDatabaseHelper;
import com.example.smartsilent.Contacts.ContactsDatabaseQuery;
import com.example.smartsilent.Location.LocationsData;
import com.example.smartsilent.TimeZone.TimeZoneData;
import com.example.smartsilent.TimeZone.TimeZoneDatabase;
import com.example.smartsilent.TimeZone.TimeZoneDatabaseHelper;
import com.example.smartsilent.TimeZone.TimeZoneDatabaseQuery;

import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.TimeZone;

/**
 This class acts as a package containing all data within a user profile: contacts info,
 timezones and locations
 */

public class Profile implements Parcelable {


    private TimeZoneData timeZoneData;
    private ContactsData contactsData;
    private LocationsData locationsData;

    private static Profile instance = null;
    private  SQLiteDatabase mContactsDatabase = null;
    private  SQLiteDatabase mTimeZoneDatabase = null;
    private  SQLiteDatabase mLocationsDatabase = null;

    private Context context;

    private Profile(Context context) {
        this.context = context;


        timeZoneData = new TimeZoneData();
        contactsData = new ContactsData();

        read_contacts();
        read_timezone();
       // read_location();

    }

    private void read_contacts() {
        // create database
        mContactsDatabase = new ContactsDatabaseHelper(context, "profile_name").getWritableDatabase();

        // query database
        ContactsDatabaseQuery query = new ContactsDatabaseQuery(mContactsDatabase);
        Pair<ArrayList<String>, ArrayList<String>> contacts_info =  query.getContacts();

        contactsData.setContactsName(contacts_info.first);
        contactsData.setPhoneNumbers(contacts_info.second);

        mContactsDatabase.close();
    }
    private void read_timezone() {
        // create database
        mTimeZoneDatabase = new TimeZoneDatabaseHelper(context, "profile_name").getWritableDatabase();

        // query database
        TimeZoneDatabaseQuery query = new TimeZoneDatabaseQuery(mTimeZoneDatabase);
        Pair<ArrayList<String>, ArrayList<String>> time_zones =  query.getTimeZones();

        for(int i = 0; i < time_zones.second.size(); i++) {
            boolean[] day_hours = new boolean[TimeZoneData.NUM_HOURS];

            String[] selected_hours = time_zones.second.get(i).split(",");

            for (String selectedHour : selected_hours) {
                if (selectedHour.compareTo("") != 0) {
                    int selected_hour = Integer.parseInt(selectedHour) - 1;
                    day_hours[selected_hour] = true;
                }
            }

            timeZoneData.getData().set(i, day_hours);
        }

        mTimeZoneDatabase.close();

    }
    private void read_location() {

    }

    public static Profile getInstance(Context context) {
        if(instance == null) {
            instance = new Profile(context);
        }
        return instance;
    }

    protected Profile(Parcel in) {
        in.readParcelable(TimeZoneData.class.getClassLoader());
        in.readParcelable(ContactsData.class.getClassLoader());
      //  in.readParcelable(LocationsData.class.getClassLoader());
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


        parcel.writeParcelable(timeZoneData, 0);
        parcel.writeParcelable(contactsData, 0);
        //parcel.writeParcelable(locationsData, 0);

    }

    public ContactsData getContactsData() {
        return contactsData;
    }


    public TimeZoneData getTimeZone() {
        return timeZoneData;
    }

    public LocationsData getLocationsData() {
        return locationsData;
    }

    public void updateTimeZoneDatabase() {
        // create database
        mTimeZoneDatabase = new TimeZoneDatabaseHelper(context, "profile_name").getWritableDatabase();

        ArrayList<String> days = timeZoneData.getDays();
        ArrayList<boolean[]> hours = timeZoneData.getData();

        ArrayList<String> hours_per_day = new ArrayList<>();
        for(int i = 0; i < TimeZoneData.NUM_DAYS; i++) {
            StringBuilder sb = new StringBuilder();

            hours_per_day.add("");

            for(int j = 0; j < TimeZoneData.NUM_HOURS; j++) {
                if(hours.get(i)[j]) {
                    if(sb.length() != 0) {
                        sb.append(",");
                    }
                    sb.append(j + 1);
                }
            }

            hours_per_day.set(i, sb.toString());
        }

        // add in database
        //Log.e("TAG", " " + hours_per_day.size());
        for(int i = 0; i < hours_per_day.size(); i++) {
            //Log.e("TAG", " " + days.size());
            ContentValues values = TimeZoneDatabaseQuery.getContentValues(days.get(i), hours_per_day.get(i));
            mTimeZoneDatabase.replace(TimeZoneDatabase.NAME, null, values);
        }

        // close database
        mTimeZoneDatabase.close();
    }

    public void updateContactsDatabase(ContactsData selected_contacts, ContactsData unselected_contacts) {

        // create database
        mContactsDatabase = new ContactsDatabaseHelper(context, "profile_name").getWritableDatabase();

        // add in database
        for (int i = 0; i < selected_contacts.getContactsName().size(); i++) {

            ContentValues values = ContactsDatabaseQuery.getContentValues
                    (selected_contacts.getContactsName().get(i), selected_contacts.getPhoneNumbers().get(i));
            mContactsDatabase.replace(ContactsDatabase.NAME, null, values);
        }

        // remove from database
        for(int i = 0; i < unselected_contacts.getContactsName().size(); i++) {
            mContactsDatabase.delete(ContactsDatabase.NAME, ContactsDatabase.Cols.CONTACT_NAME + " = ?",
                    new String[] { unselected_contacts.getContactsName().get(i).toString() });
        }

        mContactsDatabase.close();

        // add in contactsData
        for (int i = 0; i < selected_contacts.getContactsName().size(); i++) {
            //selected_contacts
            contactsData.getContactsName().add(selected_contacts.getContactsName().get(i));
            contactsData.getPhoneNumbers().add(selected_contacts.getPhoneNumbers().get(i));
        }

        // remove from contactsData
        for(int i = 0; i < unselected_contacts.getContactsName().size(); i++) {
            contactsData.getContactsName().remove(unselected_contacts.getContactsName().get(i));
            contactsData.getPhoneNumbers().remove(unselected_contacts.getPhoneNumbers().get(i));
        }
    }


}
