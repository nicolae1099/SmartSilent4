package com.example.smartsilent;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DisplayContacts extends AppCompatActivity {

    Switch selectAll;
    TextView confirm_selection;
    TextView cancel_selection;
    RecyclerView mRecyclerView;
    MyContactsAdapter myAdapter;
    ArrayList<Model> models = new ArrayList<>();
    Profile contacts = new Profile();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_list);

        confirm_selection = findViewById(R.id.confirm);
        confirm_selection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                for(int i = 0; i < models.size(); i++) {
                    if(!(models.get(i).getCheck())) {
                        contacts.addContactName(models.get(i).getName());
                        contacts.addContactNumber(models.get(i).getPhone_number());
                    }
                }

                Intent returnIntent = new Intent();
                returnIntent.putExtra("contacts", contacts);
                setResult(Activity.RESULT_OK,returnIntent);
                finish();

            }
        });

        cancel_selection = findViewById(R.id.cancel);
        cancel_selection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent returnIntent = new Intent();
                setResult(Activity.RESULT_CANCELED, returnIntent);
                finish();
            }
        });

        selectAll = findViewById(R.id.select_all);
        selectAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myAdapter.changeSelectAll();
            }
        });

        getContactsList();

        try {
            mRecyclerView = findViewById(R.id.recyclerView);
            mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

            myAdapter = new MyContactsAdapter(DisplayContacts.this, models);
            mRecyclerView.setAdapter(myAdapter);

        } catch (Exception ex) {
            ex.printStackTrace();

        }
    }


   private void getContactsList() {
        ContentResolver resolver;
        resolver = getContentResolver();
        Map<Long, List<String>> phones = new HashMap<>();
        Cursor getContactsCursor = resolver.query(
                ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{
                        ContactsContract.CommonDataKinds.Phone.CONTACT_ID,
                        ContactsContract.CommonDataKinds.Phone.NUMBER},
                null, null, null);

        if (getContactsCursor != null) {
            while (getContactsCursor.moveToNext()) {
                long contactId = getContactsCursor.getLong(0);
                String phone = getContactsCursor.getString(1);
                List<String> list;
                if (phones.containsKey(contactId)) {
                    list = phones.get(contactId);
                } else {
                    list = new ArrayList<>();
                    phones.put(contactId, list);
                }
                list.add(phone);
            }
            getContactsCursor.close();
        }
        getContactsCursor = resolver.query(
                ContactsContract.Contacts.CONTENT_URI,
                new String[]{
                        ContactsContract.Contacts._ID,
                        ContactsContract.Contacts.DISPLAY_NAME,
                        ContactsContract.CommonDataKinds.Phone.PHOTO_URI},
                null, null, null);
        while (getContactsCursor != null &&
                getContactsCursor.moveToNext()) {
            long contactId = getContactsCursor.getLong(0);
            String name = getContactsCursor.getString(1);
            String photo = getContactsCursor.getString(2);
            List<String> contactPhones = phones.get(contactId);
            if (contactPhones != null) {
                for (String phone :
                        contactPhones) {
                    models.add(new Model(name, phone, 0));
                }
            }
        }
    }


    static class Contact {
        String name;
        String phone_number;

        public Contact(String name, String phone) {
            this.name = name;
            this.phone_number = phone;
        }
    }
}
