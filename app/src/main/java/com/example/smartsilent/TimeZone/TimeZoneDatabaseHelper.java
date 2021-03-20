package com.example.smartsilent.TimeZone;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.smartsilent.Contacts.ContactsDatabase;
import com.example.smartsilent.DatabaseContext;

public class TimeZoneDatabaseHelper extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "time_zone.db";

    public TimeZoneDatabaseHelper (Context context, String profile_name) {
        super(new DatabaseContext(context, profile_name), DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + TimeZoneDatabase.NAME + "(" +
                " _id integer primary key autoincrement, " +
                TimeZoneDatabase.Cols.DAYS + ", " +
                TimeZoneDatabase.Cols.HOURS +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
