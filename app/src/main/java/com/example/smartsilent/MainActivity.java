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

public class MainActivity extends AppCompatActivity implements PopupMenu.OnMenuItemClickListener {

    ImageButton addProfile;
    AudioManager audioManager;
    public static final int REQUEST_ID_MULTIPLE_PERMISSIONS = 1;

    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        checkAndRequestPermissions();

        addProfile = findViewById(R.id.add_new_profile_button);
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        /* check if profiles directory exists <=> if the user created any profile before
        if he didn't, create a profiles directory */
        Path path = FileSystems.getDefault().getPath(getApplicationContext().getFilesDir()+ "/profiles");
        File mydir;
        if (!Files.exists(path)) {
            mydir = new File(getApplicationContext().getFilesDir(), "profiles");
            mydir.mkdir();

            //File active_profiles = new File(getApplicationContext().getFilesDir() + "/profiles", "active_profiles");
           // active_profiles.mkdir();
        }

        if (!notificationManager.isNotificationPolicyAccessGranted()) {

            Intent intent = new Intent(android.provider.Settings.ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);

            startActivity(intent);
        }
        /*addProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                //audioManager.adjustVolume(AudioManager.ADJUST_MUTE, 0);
                //audioManager.adjustStreamVolume(AudioManager.STREAM_VOICE_CALL, AudioManager.ADJUST_LOWER, 0);
                Intent intent = new Intent(MainActivity.this, ProfileName.class);
                startActivity(intent);
            }
        });

         */


    }

    private  boolean checkAndRequestPermissions() {

        int readPhoneState = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE);
        int read_call_log = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_CALL_LOG);
        List listPermissionsNeeded = new ArrayList<>();
        if (readPhoneState != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_PHONE_STATE);
        }

        if (read_call_log != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_CALL_LOG);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this,
                    (String[]) listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]),
                    REQUEST_ID_MULTIPLE_PERMISSIONS);
            return false;
        }
        return true;
    }

    public void showPopup(View view) {
        PopupMenu popupMenu = new PopupMenu(this, view);
        popupMenu.setOnMenuItemClickListener(this);
        popupMenu.inflate(R.menu.popup_menu);
        popupMenu.show();
    }

    @Override
    public boolean onMenuItemClick(MenuItem menuItem) {
        Intent intent;
        switch (menuItem.getItemId()){
            case R.id.item1:
                intent = new Intent(MainActivity.this, MapsActivity.class);
                startActivity(intent);
                return true;
            case R.id.item2:
                intent = new Intent(MainActivity.this, MakeContacts.class);
                startActivity(intent);
                return true;
            case R.id.item3:
                intent = new Intent(MainActivity.this, MakeTimeZone.class);
                startActivity(intent);
                return true;
            default:
                return false;

        }

    }
}
