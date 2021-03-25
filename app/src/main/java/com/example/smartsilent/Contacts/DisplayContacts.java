package com.example.smartsilent.Contacts;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Pair;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Filter;
import android.widget.SearchView;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.MenuItemCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartsilent.R;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class DisplayContacts extends AppCompatActivity {

    private Switch selectAll;
    private RecyclerView mRecyclerView;
    private MyContactsAdapter myAdapter;
    private ArrayList<ContactModel> full_contact_list = new ArrayList<>();

    private ContactsData contacts;
    private ContactsData selected_contacts;
    private ContactsData unselected_contacts;
    private Filter filter;
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

            myAdapter = new MyContactsAdapter(DisplayContacts.this, full_contact_list);
            mRecyclerView.setAdapter(myAdapter);

        } catch (Exception ex) {
            ex.printStackTrace();
        }
        filter = myAdapter.getFilter();

    }

   private void getContactsList() {
        Set<Pair<String, String>> namePhoneMap = new TreeSet<>(new Comparator<Pair<String, String>>() {
            @Override
            public int compare(Pair<String, String> p1, Pair<String, String> p2) {
                return p1.second.compareTo(p2.second);
            }
        });

        Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);

       // Loop Through All The Numbers
       while (phones.moveToNext()) {
           String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
           String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
           // Cleanup the phone number
           phoneNumber = phoneNumber.replaceAll("[()\\s-]+", "");

           // Enter Into Hash Map
           namePhoneMap.add(Pair.create(phoneNumber, name));
       }
       phones.close();

       // Get The Contents of Hash Map in Log
       for (Pair<String, String> entry : namePhoneMap) {
           String phoneNumber = entry.first;
           String name = entry.second;
           full_contact_list.add(new ContactModel(name, phoneNumber));

           if(inDB.get(name) != null) {
               full_contact_list.get(full_contact_list.size() - 1).check();
           }
       }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        SearchView searchView = (SearchView) menu.findItem(R.id.search_bar).getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                filter.filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filter.filter(newText);
                return false;
            }
        });

        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onBackPressed() {
        for(int i = 0; i < full_contact_list.size(); i++) {
            if((full_contact_list.get(i).getCheck() == 1 && inDB.get(full_contact_list.get(i).getName()) == null)) {
                selected_contacts.getContactsName().add(full_contact_list.get(i).getName());
                selected_contacts.getPhoneNumbers().add(full_contact_list.get(i).getPhone_number());
            } else if (full_contact_list.get(i).getCheck() == 0 && inDB.get(full_contact_list.get(i).getName()) != null) {
                unselected_contacts.getContactsName().add(full_contact_list.get(i).getName());
                unselected_contacts.getPhoneNumbers().add(full_contact_list.get(i).getPhone_number());
            }
        }

        Intent returnIntent = new Intent();
        returnIntent.putExtra("selected_contacts", selected_contacts);
        returnIntent.putExtra("unselected_contacts", unselected_contacts);
        setResult(Activity.RESULT_OK, returnIntent);

        super.onBackPressed();
    }
}
