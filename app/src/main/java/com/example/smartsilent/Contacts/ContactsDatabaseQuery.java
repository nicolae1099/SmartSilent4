package com.example.smartsilent.Contacts;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Pair;

import com.example.smartsilent.Contacts.ContactsCursorWrapper;
import com.example.smartsilent.Contacts.ContactsDatabase;

import java.util.ArrayList;

/**
 * This class will contain methods to query all the databases: contacts,
 * timezones and locations
 */
public class ContactsDatabaseQuery {

    private SQLiteDatabase  mDatabase;

    public ContactsDatabaseQuery(SQLiteDatabase  mDatabase) {
        this.mDatabase = mDatabase;
    }

    public static ContentValues getContentValues(String name, String cNumber) {
        ContentValues values = new ContentValues();
        values.put(ContactsDatabase.Cols.CONTACT_NAME, name);
        values.put(ContactsDatabase.Cols.PHONE_NUMBER, cNumber);

        return values;
    }

    private ContactsCursorWrapper queryContacts(String whereClause, String[] whereArgs) {
        Cursor cursor = mDatabase.query(
                ContactsDatabase.NAME,
                null, // columns - null selects all columns
                whereClause,
                whereArgs,
                null, // groupBy
                null, // having
                null  // orderBy
        );

        return new ContactsCursorWrapper(cursor);
    }

    public Pair<ArrayList<String>, ArrayList<String>> getContacts() {
        Pair<ArrayList<String>, ArrayList<String>> contacts = new Pair(new ArrayList<String>(), new ArrayList<String>());

        ContactsCursorWrapper cursor = queryContacts(null, null);

        try {
            cursor.moveToFirst();
            while (!cursor.isAfterLast()) {
                contacts.first.add(cursor.getContact().first);
                contacts.second.add(cursor.getContact().second);
                cursor.moveToNext();
            }
        } finally {
            cursor.close();
        }

        return contacts;
    }

    public Pair<String, String> getContact(String contact_name, String phone_number) {
        ContactsCursorWrapper cursor;

        cursor = queryContacts(
                ContactsDatabase.Cols.CONTACT_NAME + " = ?",
                new String[] { contact_name.toString() }
        );

        try {
            if (cursor.getCount() == 0) {
                cursor = queryContacts(
                        ContactsDatabase.Cols.PHONE_NUMBER+ " = ?",
                        new String[] { phone_number.toString() }
                );
            }

            if (cursor.getCount() == 0) {
                return null;
            }

            cursor.moveToFirst();
            return cursor.getContact();
        } finally {
            cursor.close();
        }

    }
}
