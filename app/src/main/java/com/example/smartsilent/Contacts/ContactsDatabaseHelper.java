package com.example.smartsilent.Contacts;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.smartsilent.DatabaseContext;

public class
ContactsDatabaseHelper  extends SQLiteOpenHelper {
    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "contacts.db";

    public ContactsDatabaseHelper(Context context, String profile_name) {
        super(new DatabaseContext(context, profile_name), DATABASE_NAME, null, VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL("create table " + ContactsDatabase.NAME + "(" +
                " _id integer primary key autoincrement, " +
                ContactsDatabase.Cols.CONTACT_NAME + ", " +
                ContactsDatabase.Cols.PHONE_NUMBER +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }


}
