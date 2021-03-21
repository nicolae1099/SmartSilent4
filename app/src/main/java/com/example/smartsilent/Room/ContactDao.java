package com.example.smartsilent.Room;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ContactDao {

    @Insert
    void insert(Contact profile);

    @Update
    void update(Contact profile);

    @Delete
    void delete(Contact profile);

    @Query("DELETE FROM contact_table")
    void deleteAllContacts();

    @Query("SELECT * FROM contact_table")
    LiveData<List<Contact>> getAllContacts();


}
