package com.example.smartsilent;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.media.AudioManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.IBinder;
import android.provider.ContactsContract;
import android.telephony.PhoneStateListener;
import android.telephony.TelephonyManager;
import android.util.Log;

import com.example.smartsilent.Contacts.ContactsDatabase;
import com.example.smartsilent.Contacts.ContactsDatabaseHelper;
import com.example.smartsilent.Contacts.ContactsDatabaseQuery;
import com.example.smartsilent.TimeZone.TimeZoneDatabaseHelper;
import com.example.smartsilent.TimeZone.TimeZoneDatabaseQuery;

import java.io.File;
import java.io.FileReader;
import java.util.Calendar;
import java.util.HashMap;
import java.util.TimeZone;

public class CallReceiver extends BroadcastReceiver {

    AudioManager mAudioManager;
    String contactNumber;

    @Override
    public void onReceive(Context context, Intent intent) {

        TelephonyManager telephony = (TelephonyManager)context.getSystemService(Context.TELEPHONY_SERVICE);
        PhoneStateListener customPhoneListener = new PhoneStateListener();

        telephony.listen(customPhoneListener, PhoneStateListener.LISTEN_CALL_STATE);

        Bundle bundle = intent.getExtras();
        String phone_number = bundle.getString("incoming_number");

        String stateStr = intent.getExtras().getString(TelephonyManager.EXTRA_STATE);

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

        String incoming_number= bundle.getString(TelephonyManager.EXTRA_INCOMING_NUMBER);
        contactNumber = incoming_number;
        String msg = "";

        String contact_name = getContactDisplayNameByNumber(incoming_number,context);


        // unsilence phone
        mAudioManager = (AudioManager) context.getSystemService(Context.AUDIO_SERVICE);
        mAudioManager.setRingerMode(AudioManager.RINGER_MODE_NORMAL);
        mAudioManager.setStreamVolume(AudioManager.STREAM_VOICE_CALL, mAudioManager.getStreamMaxVolume(AudioManager.STREAM_VOICE_CALL), 0);

        if(!checkTimeInterval(context)) {
            return;
        }

        checkContactName(context, contact_name);
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

    HashMap<Integer, String> week_days = new HashMap<Integer, String>(){{ put(1, "Sunday"); put(2, "Monday");
        put(3, "Thursday"); put(4, "Wednesday"); put(5, "Thursday"); put(6, "Friday"); put(7, "Saturday");}};


    public boolean checkTimeInterval(Context context) {
        Calendar rightNow = Calendar.getInstance();
        rightNow.setTimeZone(TimeZone.getTimeZone("UTC"));
        int currentHourIn24Format = rightNow.get(Calendar.HOUR_OF_DAY) + 2; // return the hour in 24 hrs format (ranging from 0-23)

        int day_int = rightNow.get(Calendar.DAY_OF_WEEK);
        String day = week_days.get(day_int);

        int minute = rightNow.get(Calendar.MINUTE);

        // database query helper
        TimeZoneDatabaseQuery query;

        SQLiteDatabase database;

        // get corresponding database
        database = new TimeZoneDatabaseHelper(context, "/profile_name").getWritableDatabase();
        query = new TimeZoneDatabaseQuery(database);

        // search day in database
        if(query.getTimeZone(day) != null) {
            String[] hours = query.getTimeZone(day).second.split(",");
            for (int i = 0; i < hours.length; i++) {
                if(!hours[i].equals("") && Integer.parseInt(hours[i]) == currentHourIn24Format) {
                    database.close();
                    return true;
                }

            }
        }

        database.close();
        return false;
    }

    public void checkContactName(Context context, String name) {
        // database query helper
        ContactsDatabaseQuery query;

        SQLiteDatabase database;

        // get corresponding database
        database = new ContactsDatabaseHelper(context, "/profile_name").getWritableDatabase();

        query = new ContactsDatabaseQuery(database);

        // search phone number in database
        if(query.getContact(name, contactNumber) != null) {
            mAudioManager.setRingerMode(AudioManager.RINGER_MODE_SILENT);
            mAudioManager.adjustVolume(AudioManager.ADJUST_MUTE, 0);

        }

        database.close();
    }
}

