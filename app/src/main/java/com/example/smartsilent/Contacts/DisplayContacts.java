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
        HashMap<String, String> contactMap = new HashMap<>();
        List<Pair<String, String>> namePhoneMap = new ArrayList<>();
       //Map<String, String> namePhoneMap = new HashMap<String, String>();
       Cursor phones = getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI, null, null, null, null);

       // Loop Through All The Numbers
       while (phones.moveToNext()) {
           String name = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.DISPLAY_NAME));
           String phoneNumber = phones.getString(phones.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
           // Cleanup the phone number
           phoneNumber = phoneNumber.replaceAll("[()\\s-]+", "");

           if(contactMap.get(name) != null) {
                continue;
           }
           // Enter Into Hash Map
           namePhoneMap.add(Pair.create(phoneNumber, name));
           contactMap.put(name,phoneNumber);
           //namePhoneMap.put(phoneNumber, name);
       }

       Collections.sort(namePhoneMap, new Comparator<Pair<String, String>>() {
           @Override
           public int compare(final Pair<String, String> o1, final Pair<String, String> o2) {
               return o1.second.compareTo(o2.second);
           }
       });

       // Get The Contents of Hash Map in Log
       for (Pair<String, String> entry : namePhoneMap) {
           String phoneNumber = entry.first;
           String name = entry.second;
           full_contact_list.add(new ContactModel(name, phoneNumber));

           if(inDB.get(name) != null) {
               full_contact_list.get(full_contact_list.size() - 1).check();
           }
       }
       phones.close();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);

        //SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.search_bar).getActionView();

        //searchView.setSearchableInfo(searchManager.getSearchableInfo(getComponentName()));
       // searchView.setIconifiedByDefault(false); // Do not iconify the widget; expand it by default

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
