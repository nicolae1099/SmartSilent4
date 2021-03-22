package com.example.smartsilent;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.Activity;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.smartsilent.Contacts.ContactsData;
import com.example.smartsilent.Contacts.DisplayContacts;
import com.example.smartsilent.Location.MapsActivity;
import com.example.smartsilent.TimeZone.MakeTimeZone;
import com.example.smartsilent.TimeZone.TimeZoneData;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;

    private Button locationButton;
    private Button contactsButton;
    private Button timezoneButton;

    private Profile mProfile;
    final private int MAKE_CONTACTS = 1;
    final private int MAKE_TIMEZONE = 2;
    final private int MAKE_LOCATIONS = 3;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        checkAndRequestPermissions();

        /* check if profiles directory exists <=> if the user created any profile before
        if he didn't, create a profiles directory */
        Path path = FileSystems.getDefault().getPath(getApplicationContext().getFilesDir()+ "/profiles");
        File mydir;

        if (!Files.exists(path)) {
            mydir = new File(getApplicationContext().getFilesDir(), "profiles");
            mydir.mkdir();
        }

        mProfile = Profile.getInstance(this.getApplicationContext());

        locationButton = findViewById(R.id.add_location_button);
        locationButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MapsActivity.class);
                intent.putExtra("location_data", mProfile.getLocationsData());
                startActivityForResult(intent, MAKE_LOCATIONS);
            }
        });

        contactsButton = findViewById(R.id.add_contact_button);
        contactsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DisplayContacts.class);
                intent.putExtra("contacts", mProfile.getContactsData());
                startActivityForResult(intent, MAKE_CONTACTS);
            }
        });

        timezoneButton = findViewById(R.id.add_time_zone_button);
        timezoneButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MakeTimeZone.class);
                intent.putExtra("time_zone", mProfile.getTimeZone());
                startActivityForResult(intent, MAKE_TIMEZONE);
            }
        });

    }

    private  boolean checkAndRequestPermissions() {
        NotificationManager notificationManager =
            (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        if (!notificationManager.isNotificationPolicyAccessGranted()) {
            Intent intent = new Intent(android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);
            startActivity(intent);
        }
        int readPhoneState = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
        int read_call_log = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG);
        int read_contacts = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CONTACTS);

        List listPermissionsNeeded = new ArrayList<>();
        if (readPhoneState != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_PHONE_STATE);
        }

        if (read_call_log != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_CALL_LOG);
        }

        if (read_contacts != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_CONTACTS);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    (String[]) listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                    REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
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

                    ContactsData selected_contacts = data.getParcelableExtra("selected_contacts");
                    ContactsData unselected_contacts = data.getParcelableExtra("unselected_contacts");

                    mProfile.updateContactsDatabase(selected_contacts, unselected_contacts);

                }
                break;
            case (MAKE_TIMEZONE):
                if (resultCode == Activity.RESULT_OK) {

                    TimeZoneData in = data.getParcelableExtra("time_zone");
                    Log.e("TAG", " " + in.getData().size());
                    mProfile.getTimeZone().setData(in.getData());

                    mProfile.updateTimeZoneDatabase();
                }
                break;
            case (MAKE_LOCATIONS):

               // LocationsData in = data.getParcelableExtra("locations");
               // saved_features.put("make_locations", true);
                break;
        }
    }



}
