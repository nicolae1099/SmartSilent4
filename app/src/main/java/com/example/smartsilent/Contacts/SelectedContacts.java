package com.example.smartsilent.Contacts;

import android.os.Bundle;
import android.util.Pair;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.smartsilent.R;

import java.util.ArrayList;

public class SelectedContacts extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Bundle b = this.getIntent().getExtras();
        ArrayList<String> contacts_name = b.getStringArrayList("contacts_name");
        ArrayList<String> contacts_number = b.getStringArrayList("contacts_number");

        if(contacts_name.size() == 0) {
            System.out.println("DC EU");
        }
        setContentView(R.layout.selected_contacts);

        try {
            ArrayAdapter adapter = new ArrayAdapter<String>(this,
                    R.layout.list, contacts_name);
            ListView listView = (ListView) findViewById(R.id.mobile_list);
            listView.setAdapter(adapter);

        } catch (Exception ex) {
            ex.printStackTrace();

        }


    }
}
