package com.example.smartsilent;

import androidx.appcompat.app.AppCompatActivity;

import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import static android.media.AudioManager.ADJUST_RAISE;

public class MainActivity extends AppCompatActivity{

    ImageButton addProfile;
    AudioManager audioManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addProfile = findViewById(R.id.add_new_profile_button);
        audioManager = (AudioManager) getSystemService(Context.AUDIO_SERVICE);
        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

            if (!notificationManager.isNotificationPolicyAccessGranted()) {

                Intent intent = new Intent(
                        android.provider.Settings
                                .ACTION_NOTIFICATION_POLICY_ACCESS_SETTINGS);

                startActivity(intent);
            }

        addProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
                //audioManager.adjustVolume(AudioManager.ADJUST_MUTE, 0);
                //audioManager.adjustStreamVolume(AudioManager.STREAM_VOICE_CALL, AudioManager.ADJUST_LOWER, 0);
                Intent intent = new Intent(MainActivity.this, MakeProfile.class);
                startActivity(intent);
            }
        });


    }
}