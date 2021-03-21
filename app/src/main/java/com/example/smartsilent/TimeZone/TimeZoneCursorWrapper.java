package com.example.smartsilent.TimeZone;

import android.database.Cursor;
import android.database.CursorWrapper;
import android.util.Pair;

/**Nota: Nu sterge. Este model pentru baza de date pentru intervalele orare.
 * Spre ex:
 * @dayString: Monday / Tuesday / ... / Sunday
 * @hourString: string cu successiuni de intervale orare: 8:30-10:00,12:30-14:00,16:00-20:00
 * */

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
