package com.example.smartsilent.Contacts;

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

import com.example.smartsilent.Profile;
import com.example.smartsilent.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DisplayContacts extends AppCompatActivity {

    private Switch selectAll;
    private TextView confirm_selection;
    private TextView cancel_selection;
    private RecyclerView mRecyclerView;
    private MyContactsAdapter myAdapter;
    private ArrayList<ContactModel> models = new ArrayList<>();
    private Profile contacts = new Profile();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_list);

        confirm_selection = findViewById(R.id.confirm);
        confirm_selection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                for(ContactModel model : models) {
                    if((model.getCheck() == 1)) {
                        contacts.addContactName(model.getName());
                        contacts.addContactNumber(model.getPhone_number());
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
            List<String> contactPhones = phones.get(contactId);
            if (contactPhones != null) {
                for (String phone :
                        contactPhones) {
                    models.add(new ContactModel(name, phone));
                }
            }
        }
    }
}
