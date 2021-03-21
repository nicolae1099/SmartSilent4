package com.example.smartsilent;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smartsilent.Contacts.ContactsDatabase;
import com.example.smartsilent.Contacts.ContactsDatabaseHelper;
import com.example.smartsilent.Contacts.ContactsDatabaseQuery;
import com.example.smartsilent.Contacts.DisplayContacts;
import com.example.smartsilent.Location.MapsActivity;
import com.example.smartsilent.TimeZone.MakeTimeZone;
import com.example.smartsilent.TimeZone.TimeZoneDatabase;
import com.example.smartsilent.TimeZone.TimeZoneDatabaseHelper;
import com.example.smartsilent.TimeZone.TimeZoneDatabaseQuery;

import java.util.ArrayList;

/**
 This class represents the activity where the user can create a custom profile.
 He can choose to custom his profile with contacts, timezones and/or locations,
 by pressing the corresponding button.
 The profile is saved in the database when the user presses the "save" button.
 */
public class MakeProfile extends AppCompatActivity {

    private Button locationButton;
    private Button contactsButton;
    private Button timezoneButton;
    private Button saveProfileButton;

    private Profile mProfile;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    final private int MAKE_CONTACTS = 1;
    final private int MAKE_TIMEZONE = 2;
    final private int MAKE_LOCATIONS = 3;

    private String mProfileName;
    private String mPreviousActivity;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.make_profile);

        mContext = getApplicationContext();

        mPreviousActivity = this.getIntent().getStringExtra("activity");

        // if the previous activity was the MainActivity, create a new Profile Object
        if(mPreviousActivity.compareTo("main") == 0) {
            mProfile = new Profile();
        }

        mProfileName = "profile_name";

        /* pass to the next activity the name of the current activity and
        the current Profile */
        final Bundle profile = new Bundle();
        profile.putParcelable("profile", mProfile);
        profile.putString("activiy", "make_profile");

        locationButton = findViewById(R.id.add_location_button);
        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MakeProfile.this, MapsActivity.class);
                intent.putExtras(profile);
                startActivity(intent);
            }
        });

        contactsButton = findViewById(R.id.add_contact_button);
        contactsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MakeProfile.this, DisplayContacts.class);
                startActivityForResult(intent, MAKE_CONTACTS);


            }
        });

        timezoneButton = findViewById(R.id.add_time_zone_button);
        timezoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MakeProfile.this, MakeTimeZone.class);
                intent.putExtras(profile);
                startActivity(intent);
            }
        });

        saveProfileButton = findViewById(R.id.save_profle);
        saveProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MakeProfile.this, MainActivity.class);
                Log.e("Scriu", "Dece " + mPreviousActivity);
                if(mPreviousActivity.compareTo("contacts") == 0) {
                    Log.e("Scriu", "Incerc");
                    putContacts();

                    ArrayList<String> hours = (ArrayList<String> ) mProfile.getTime_intervals();
                    hours.add("8:30-10:00,19:00-20:00");
                    hours.add("");
                    hours.add("");
                    hours.add("");
                    hours.add("");
                    hours.add("8:30-10:00");
                    hours.add("8:30-10:00,12:30-13:00,16:00-20:00");

                    putTimeZone();

                } else if(mPreviousActivity.compareTo("time_zone") == 0) {
                    putTimeZone();

                } else if(mPreviousActivity.compareTo("locations") == 0) {
                    putLocations();
                }

                startActivity(intent);
            }
        });
    }

    /** This method retrieves the returned value of the previous finished activity.
     * @param data contains a Profile object, customized by the user in one of the
     *             customisation activities: contacts, timezone or locations
     */
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        switch (reqCode) {
            case (MAKE_CONTACTS):
                if (resultCode == Activity.RESULT_OK) {

                    Profile contactsInfo = data.getParcelableExtra("contacts");
                    ArrayList<String> contactsName = (ArrayList<String>) contactsInfo.getContactsNames();
                    ArrayList<String> contactsNumber = (ArrayList<String>) contactsInfo.getContactsNumbers();

                    mProfile.setContactsNames(contactsName);
                    mProfile.setContactsNumbers(contactsNumber);

                    Log.e("Size", "Size is: " + contactsName.size());

                    mPreviousActivity = "contacts";


                   /* /**Date mockate pentru baza de date*/
                   /* mPreviousActivity = "time_zone";


                    ArrayList<String> hours = (ArrayList<String> ) mProfile.getTime_intervals();
                    hours.add("8:30-10:00,12:30-16:00,16:00-20:00");
                    hours.add("");
                    hours.add("");
                    hours.add("");
                    hours.add("");
                    hours.add("");
                    hours.add("8:30-10:00,12:30-15:00,16:00-20:00");*/

                }
                break;
            case (MAKE_TIMEZONE):
                if (resultCode == Activity.RESULT_OK) {
                    /**Nota: Nu sterge. Este model pentru ce am putea intoarce din activitate.
                     * Spre ex:
                     * @days: Array with days: Monday, Tuesday, ... , Sunday
                     * @hours: Array with time_intervals for each day: hours[0] - 8:30-10:00,12:30-14:00,16:00-20:00*/
                    /*
                    Profile time_intervals = data.getParcelableExtra("time_zone");
                    ArrayList<String> days = (ArrayList<String>) time_intervals.getContactsNames();
                    ArrayList<String> hours = (ArrayList<String>) time_intervals.getContactsNumbers();

                    mProfile.setContactsNames(days);
                    mProfile.setContactsNumbers(hours);

                    mPreviousActivity = "time_zone";
                    */



                    //mProfile.setTime_intervals(hours);

                }
                break;
            case (MAKE_LOCATIONS):
                break;
        }
    }


    public void putContacts(){
        // create database
        mDatabase = new ContactsDatabaseHelper(getApplicationContext(), mProfileName).getWritableDatabase();

        // retrieve contact info
        ArrayList<String> contactsNames = (ArrayList<String> )mProfile.getContactsNames();
        ArrayList<String> contactsNumbers = (ArrayList<String> )mProfile.getContactsNumbers();

        ContactsDatabaseQuery query = new ContactsDatabaseQuery(mDatabase);
        // add in database
        for (int i = 0; i < contactsNames.size(); i++) {
                ContentValues values = ContactsDatabaseQuery.getContentValues(contactsNames.get(i), contactsNumbers.get(i));
                mDatabase.replace(ContactsDatabase.NAME, null, values);
        }

        mDatabase.close();
    }
    public void putTimeZone() {
        // create database
        mDatabase = new TimeZoneDatabaseHelper(getApplicationContext(), mProfileName).getWritableDatabase();

        ArrayList<String> days = (ArrayList<String>) mProfile.getDays();
        ArrayList<String> hours = (ArrayList<String>) mProfile.getTime_intervals();

        TimeZoneDatabaseQuery query = new TimeZoneDatabaseQuery(mDatabase);

        // add in database
        for(int i = 0; i < hours.size(); i++) {
            ContentValues values = TimeZoneDatabaseQuery.getContentValues(days.get(i), hours.get(i));
            mDatabase.replace(TimeZoneDatabase.NAME, null, values);
        }

        // close database
        mDatabase.close();
    }
    public void putLocations(){}

}
