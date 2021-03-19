package com.example.smartsilent;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Pair;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smartsilent.Contacts.ContactsCursorWrapper;
import com.example.smartsilent.Contacts.ContactsDatabase;
import com.example.smartsilent.Contacts.ContactsDatabaseHelper;
import com.example.smartsilent.Contacts.MakeContacts;
import com.example.smartsilent.Location.MapsActivity;
import com.example.smartsilent.TimeZone.MakeTimeZone;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;

/**
 This class represents the activity where the user can create a custom profile.
 He can choose to custom his profile with contacts, timezones and/or locations,
 by pressing the corresponding button.
 The profile is saved in the database when the user presses the "save" button.
 */
public class MakeProfile extends AppCompatActivity {

    Button locationButton;
    Button contactsButton;
    Button timezoneButton;
    Button saveProfileButton;

    private Profile mProfile;
    private Context mContext;
    private SQLiteDatabase mDatabase;

    final private int MAKE_CONTACTS = 1;
    final private int MAKE_TIMEZONE = 2;
    final private int MAKE_LOCATIONS = 3;
    final private int LAUNCH_SECOND_ACTIVITY = 4;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.make_profile);

        mContext = getApplicationContext();

        Bundle b = this.getIntent().getExtras();
        final String previous_activity = b.getString("activity");

        // if the previous activity was the MainActivity, create a new Profile Object
        if(previous_activity.compareTo("main") == 0) {
            mProfile = new Profile(new ArrayList<String>(), new ArrayList<String>());
        }

        final String profile_name = b.getString("profile_name");

        // get profile name that the user chose and send it to next activity
        File profileDirName = new File(mContext.getFilesDir() + "/profiles/" , profile_name);
        profileDirName.mkdir();

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
               /* Intent intent = new Intent(MakeProfile.this, MakeContacts.class);
                intent.putExtras(profile);
                startActivityForResult(intent, MAKE_CONTACTS);
                */
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
                if(previous_activity != "main") {
                    // create database
                   mDatabase = new ContactsDatabaseHelper(getApplicationContext(), profile_name).getWritableDatabase();

                   // retrieve contact info
                    ArrayList<String> contactsNames = (ArrayList<String> )mProfile.getContactsNames();
                    ArrayList<String> contactsNumbers = (ArrayList<String> )mProfile.getContactsNumbers();

                    DatabaseQuery query = new DatabaseQuery(mDatabase);
                    // add in database
                    for(int i = 0; i < contactsNames.size(); i++) {
                        if(query.getContact(contactsNames.get(i)) == null) {
                            ContentValues values = query.getContentValues(contactsNames.get(i), contactsNumbers.get(i));
                            mDatabase.insertWithOnConflict(ContactsDatabase.NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
                        }
                    }

                    mDatabase.close();

                    File active = new File(getApplicationContext().getFilesDir() , "profiles");
                    File mimi = new File (active, "active_profiles");
                    FileWriter myWriter = null;
                    try {
                        myWriter = new FileWriter(mimi);
                        try {
                            myWriter.write(profile_name);
                            myWriter.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }



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

                }
                break;
            case (MAKE_TIMEZONE):
                break;
            case (MAKE_LOCATIONS):
                break;
        }
    }




}
