package com.example.smartsilent;

import android.database.Cursor;
import android.database.CursorWrapper;
import android.util.Pair;

public class ContactsCursorWrapper extends CursorWrapper {
        public ContactsCursorWrapper(Cursor cursor) {
            super(cursor);
        }
        public Pair<String, String> getContact() {
            String contactNameString = getString(getColumnIndex(ContactsDatabase.Cols.CONTACT_NAME));
            String contactPhoneString = getString(getColumnIndex(ContactsDatabase.Cols.PHONE_NUMBER));

            return new Pair<String, String> (contactNameString, contactPhoneString);
        }
    }

