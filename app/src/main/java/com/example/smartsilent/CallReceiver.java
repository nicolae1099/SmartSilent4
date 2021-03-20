package com.example.smartsilent;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.widget.Toast;

import com.example.smartsilent.Contacts.ContactsDatabaseHelper;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

public class CallReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {

        TelephonyManager telephony = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        PhoneStateListener customPhoneListener = new PhoneStateListener();

        telephony.listen(customPhoneListener, PhoneStateListener.LISTEN_CALL_STATE);

        Bundle bundle = intent.getExtras();
        String phone_number = bundle.getString("incoming_number");
      //  System.out.println("Phone number is: " + phone_number);

        String stateStr = intent.getExtras().getString(TelephonyManager.EXTRA_STATE);
        // String number = intent.getExtras().getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
        int state = 0;
        if(stateStr.equals(TelephonyManager.EXTRA_STATE_IDLE)){
            state = TelephonyManager.CALL_STATE_IDLE;
        }
        else if(stateStr.equals(TelephonyManager.EXTRA_STATE_OFFHOOK)){
            state = TelephonyManager.CALL_STATE_OFFHOOK;
        }
        else if(stateStr.equals(TelephonyManager.EXTRA_STATE_RINGING)){
            state = TelephonyManager.CALL_STATE_RINGING;
        }
        if (phone_number == null || "".equals(phone_number)) {
            return;
        }

      //  Toast.makeText(context, "Phone Number " + phone_number , Toast.LENGTH_SHORT).show();

        String incomingnumber= bundle.getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
        String msg = "";

        String names = getContactDisplayNameByNumber(incomingnumber,context);

       // Toast.makeText(context, "Name " + names , Toast.LENGTH_SHORT).show();

        // Put caller to mute by default
        AudioManager audioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        audioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);


        // database query helper
        DatabaseQuery query;

        SQLiteDatabase database;

        String dirPath = context.getFilesDir() + "/profiles";
        String filePath = dirPath + "/profile_name";
        File profile  = new File(filePath);


        FileReader fr = null;

        // get corresponding database
        database = new ContactsDatabaseHelper(context, "/profile_name").getWritableDatabase();

        query = new DatabaseQuery(database);

        // search phone number in database
        if(query.getContact(names) != null) {
            audioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
            audioManager.adjustVolume(AudioManager.ADJUST_MUTE, 0);

            database.close();
        }

    }

    public String getContactDisplayNameByNumber(String number,Context context) {
        Uri uri = Uri.withAppendedPath(ContactsContract.PhoneLookup.CONTENT_FILTER_URI, Uri.encode(number));
        String name;

        ContentResolver contentResolver = context.getContentResolver();
        Cursor contactLookup = contentResolver.query(uri, null, null, null, null);

        try {
            if (contactLookup != null && contactLookup.getCount() > 0) {
                contactLookup.moveToNext();
                name = contactLookup.getString(contactLookup.getColumnIndex(ContactsContract.Data.DISPLAY_NAME));

            } else{
                name = "Unknown number";
            }
        } finally {
            if (contactLookup != null) {
                contactLookup.close();
            }
        }

        return name;
    }
}

