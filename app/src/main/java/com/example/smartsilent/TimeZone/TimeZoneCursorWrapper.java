package com.example.smartsilent.TimeZone;

import android.database.Cursor;
import android.database.CursorWrapper;
import android.util.Pair;

public class TimeZoneCursorWrapper extends CursorWrapper {
    public TimeZoneCursorWrapper(Cursor cursor) {
        super(cursor);
    }

    public Pair<String, String>  getTimeZone() {
        String dayString = getString(getColumnIndex(TimeZoneDatabase.Cols.DAYS));
        String hoursString = getString(getColumnIndex(TimeZoneDatabase.Cols.HOURS));

        return new Pair<String, String> (dayString, hoursString);
    }
}
