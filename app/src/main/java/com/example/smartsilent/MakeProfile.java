package com.example.smartsilent;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
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
import com.example.smartsilent.Location.MakeLocation;
import com.example.smartsilent.TimeZone.MakeTimeZone;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

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

    // de adaugat verificare prima
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.make_profile);

        mContext = getApplicationContext();

        //mProfileDatabase = new ProfileDatabaseHelper(mContext).getWritableDatabase();

        Bundle b = this.getIntent().getExtras();
        final String previous_activity = b.getString("activity");

        // if the previous activity was the MainActivity, create a new Profile Object,
        // otherwise, retrieve the Profile Object from the previous activity
        switch(previous_activity) {
            case "main":
                mProfile = new Profile(new ArrayList<String>(), new ArrayList<String>());
                break;
            default:
                mProfile = this.getIntent().getParcelableExtra("profile");

        }
        final String profile_name = b.getString("profile_name");

        // check if profiles directory exists <=> if the user created any profile before
        // if he didn't, create a profiles directory
        Path path = FileSystems.getDefault().getPath(mContext.getFilesDir()+ "/profiles");
        File mydir;
        if (!Files.exists(path)) {
            mydir = new File(mContext.getFilesDir(), "profiles");
            mydir.mkdir();
        }

        // get profile name that the user chose and send it to next activity
        File profileDirName = new File(mContext.getFilesDir() + "/profiles/" , profile_name);
        profileDirName.mkdir();

        final Bundle profile = new Bundle();
        profile.putString("profile_name", profile_name);
        profile.putParcelable("profile", mProfile);
        profile.putString("activiy", "make_profile");

        locationButton = findViewById(R.id.add_location_button);
        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MakeProfile.this, MakeLocation.class);
                intent.putExtras(profile);
                startActivity(intent);
            }
        });

        contactsButton = findViewById(R.id.add_contact_button);
        contactsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MakeProfile.this, MakeContacts.class);
                intent.putExtras(profile);
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

                    // add in database
                    for(int i = 0; i < contactsNames.size(); i++) {
                        if(getContact(contactsNames.get(i)) == null) {
                            ContentValues values = getContentValues(contactsNames.get(i), contactsNumbers.get(i));
                            mDatabase.insertWithOnConflict(ContactsDatabase.NAME, null, values, SQLiteDatabase.CONFLICT_REPLACE);
                        }
                    }

                }
                startActivity(intent);
            }
        });
    }
    public void onActivityResult(int reqCode, int resultCode, Intent data) {
        super.onActivityResult(reqCode, resultCode, data);
        switch (reqCode) {
            case (MAKE_CONTACTS):
                if (resultCode == Activity.RESULT_OK) {
                    Profile contactsInfo = data.getParcelableExtra("profile");
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

    private static ContentValues getContentValues(String name, String cNumber) {
        ContentValues values = new ContentValues();
        values.put(ContactsDatabase.Cols.CONTACT_NAME, name);
        values.put(ContactsDatabase.Cols.PHONE_NUMBER, cNumber);

        return values;
    }

    private ContactsCursorWrapper queryContacts(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                ContactsDatabase.NAME,
                null, // columns - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null  // orderBy
        );

        return new ContactsCursorWrapper(cursor);
    }

    public Pair<ArrayList<String>, ArrayList<String>> getContacts() {
        Pair<ArrayList<String>, ArrayList<String>> contacts = new Pair(new ArrayList<String>(), new ArrayList<String>());

        ContactsCursorWrapper cursor = queryContacts(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                contacts.first.add(cursor.getContact().first);
                contacts.second.add(cursor.getContact().second);
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return contacts;
    }

    public Pair<String, String> getContact(String contact_name) {

        ContactsCursorWrapper cursor = queryContacts(
                ContactsDatabase.Cols.CONTACT_NAME + " = ?",
                new String[] { contact_name.toString() }
        );

        try {
            if (cursor.getCount() == 0) {
                return null;
            }

            cursor.moveToFirst();
            return cursor.getContact();
        } finally {
            cursor.close();
        }
    }


}
