package com.example.smartsilent.Room;

import android.content.Context;
import android.os.AsyncTask;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.sqlite.db.SupportSQLiteDatabase;

@Database(entities = {Contact.class}, version = 1)
public abstract class SilencePreferencesDatabase extends RoomDatabase {

    private static SilencePreferencesDatabase instance;

    public abstract ContactDao contactDao();

    public static synchronized SilencePreferencesDatabase getInstance(Context context) {
        if (instance == null) {
            instance = Room.databaseBuilder(context.getApplicationContext(),
                    SilencePreferencesDatabase.class, "profile_database")
                    .fallbackToDestructiveMigration()
                    .addCallback(roomCallback)
                    .build();
        }
        return instance;
    }

    private static RoomDatabase.Callback roomCallback = new RoomDatabase.Callback() {
        @Override
        public void onCreate(@NonNull SupportSQLiteDatabase db) {
            super.onCreate(db);
            new PopulateDbAsyncTask(instance).execute();
        }
    };

    private static class PopulateDbAsyncTask extends AsyncTask<Void, Void, Void> {
        private ContactDao contactDao;

        private PopulateDbAsyncTask(SilencePreferencesDatabase db) {
            contactDao = db.contactDao();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            contactDao.insert(new Contact("0727377232", "Nic"));
            contactDao.insert(new Contact("23323", "Rux"));
            return null;
        }
    }
}
