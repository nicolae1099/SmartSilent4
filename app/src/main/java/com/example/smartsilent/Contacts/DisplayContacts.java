package com.example.smartsilent.Contacts;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
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
    private Button confirm_selection;
    private Button cancel_selection;
    private RecyclerView mRecyclerView;
    private MyContactsAdapter myAdapter;
    private ArrayList<ContactModel> models = new ArrayList<>();

    private ContactsData contacts;
    private ContactsData selected_contacts;
    private ContactsData unselected_contacts;
    HashMap<String, Boolean> inDB;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.contact_list);

        contacts = this.getIntent().getParcelableExtra("contacts");
        selected_contacts = new ContactsData();
        unselected_contacts = new ContactsData();

        inDB = new HashMap<>();
        for(int i = 0; i < contacts.getContactsName().size(); i++) {
            inDB.put(contacts.getContactsName().get(i), true);
        }

        confirm_selection = findViewById(R.id.button_confirm);
        confirm_selection.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                for(int i = 0; i < models.size(); i++) {
                    if((models.get(i).getCheck() == 1 && inDB.get(models.get(i).getName()) == null)) {
                        selected_contacts.getContactsName().add(models.get(i).getName());
                        selected_contacts.getPhoneNumbers().add(models.get(i).getPhone_number());
                    } else if (models.get(i).getCheck() == 0 && inDB.get(models.get(i).getName()) != null) {
                        unselected_contacts.getContactsName().add(models.get(i).getName());
                        unselected_contacts.getPhoneNumbers().add(models.get(i).getPhone_number());
                    }
                }

                Intent returnIntent = new Intent();
                returnIntent.putExtra("selected_contacts", selected_contacts);
                returnIntent.putExtra("unselected_contacts", unselected_contacts);
                setResult(Activity.RESULT_OK, returnIntent);
                finish();
            }
        });

        cancel_selection = findViewById(R.id.button_cancel);
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
       Map<String, String> namePhoneMap = new HashMap<String, String>();
       Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);

       // Loop Through All The Numbers
       while (phones.moveToNext()) {
           String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
           String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
           // Cleanup the phone number
           phoneNumber = phoneNumber.replaceAll("[()\\s-]+", "");
           // Enter Into Hash Map
           namePhoneMap.put(phoneNumber, name);
       }

       // Get The Contents of Hash Map in Log
       for (Map.Entry<String, String> entry : namePhoneMap.entrySet()) {
           String phoneNumber = entry.getKey();
           String name = entry.getValue();
           models.add(new ContactModel(name, phoneNumber));

           if(inDB.get(name) != null) {
               models.get(models.size() - 1).check();
           }
       }
       phones.close();
    }
}
