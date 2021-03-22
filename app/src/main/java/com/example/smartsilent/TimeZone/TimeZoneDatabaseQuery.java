package com.example.smartsilent.TimeZone;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Pair;

import java.util.ArrayList;

public class TimeZoneDatabaseQuery {
    private SQLiteDatabase mDatabase;

    public TimeZoneDatabaseQuery(SQLiteDatabase  mDatabase) {
        this.mDatabase = mDatabase;
    }

    public static ContentValues getContentValues(String day, String hours) {
        ContentValues values = new ContentValues();

        values.put(TimeZoneDatabase.Cols.DAYS, day);
        values.put(TimeZoneDatabase.Cols.HOURS, hours);

        return values;
    }

    private TimeZoneCursorWrapper queryTimeZone(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                TimeZoneDatabase.NAME,
                null, // columns - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null  // orderBy
        );

        return new TimeZoneCursorWrapper(cursor);
    }

    public Pair<ArrayList<String>, ArrayList<String>> getTimeZones() {
        Pair<ArrayList<String>, ArrayList<String>> time_zones = new Pair(new ArrayList<String>(), new ArrayList<String>());

        TimeZoneCursorWrapper cursor = queryTimeZone(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                time_zones.first.add(cursor.getTimeZone().first);
                time_zones.second.add(cursor.getTimeZone().second);
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return time_zones;
    }

    public Pair<String, String> getTimeZone(String day) {

        TimeZoneCursorWrapper cursor = queryTimeZone(
                TimeZoneDatabase.Cols.DAYS + " = ?",
                new String[] { day.toString() }
        );

        try {
            if (cursor.getCount() == 0) {
                return null;
            }

            cursor.moveToFirst();
            return cursor.getTimeZone();

        } finally {
            cursor.close();
        }
    }
}
