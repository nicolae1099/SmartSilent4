package com.example.smartsilent;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.io.File;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;

public class MakeProfile extends AppCompatActivity {

    Button locationButton;
    Button contactsButton;
    Button timezoneButton;
    Button saveProfileButton;

    //private Profile mProfile;
    //private SQLiteDatabase mProfileDatabase;

    private Context mContext;

    // de adaugat verificare prima
    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.make_profile);

        mContext = getApplicationContext();
        //mProfileDatabase = new ProfileDatabaseHelper(mContext).getWritableDatabase();

        Bundle b = this.getIntent().getExtras();
        String profile_name = b.getString("profile_name");

        // check if profiles directory exists <=> if the user created any profile before
        // if he didn't, create a profiles directory
        Path path = FileSystems.getDefault().getPath("profiles");
        if (!Files.exists(path)) {
            File mydir = mContext.getDir("profiles", Context.MODE_PRIVATE);
        }

        // get profile name that the user chose and send it to next activity
        File profileDirName = new File("profiles" , profile_name);

        final Bundle profile = new Bundle();
        profile.putString("profile_name", profile_name);

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
                startActivity(intent);
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

                startActivity(intent);
            }
        });
    }
}
