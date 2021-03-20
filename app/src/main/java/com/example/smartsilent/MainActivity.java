package com.example.smartsilent;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.PopupMenu;

import com.example.smartsilent.Contacts.MakeContacts;
import com.example.smartsilent.Location.MapsActivity;
import com.example.smartsilent.TimeZone.MakeTimeZone;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static android.media.AudioManager.ADJUST_RAISE;

public class MainActivity extends AppCompatActivity {

    ImageButton addProfile;
    AudioManager audioManager;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        addProfile = findViewById(R.id.add_new_profile_button);

        checkAndRequestPermissions();

        /* check if profiles directory exists <=> if the user created any profile before
        if he didn't, create a profiles directory */
        Path path = FileSystems.getDefault().getPath(getApplicationContext().getFilesDir()+ "/profiles");
        File mydir;
        if (!Files.exists(path)) {
            mydir = new File(getApplicationContext().getFilesDir(), "profiles");
            mydir.mkdir();
        }

        addProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, MakeProfile.class);
                intent.putExtra("activity", "main");
                startActivity(intent);
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
}
