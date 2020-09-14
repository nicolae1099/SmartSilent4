package com.example.smartsilent;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.Arrays;

public class MyProfiles extends AppCompatActivity implements MyAdapter.ItemClickListener{

    private MyAdapter mAdapter;
    private RecyclerView.LayoutManager layoutManager;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_profiles_laylout);


        String[] pathnames;
        File f = new File(getApplicationContext().getFilesDir()  + "/profiles");
        pathnames = f.list();

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerview_layout);

        recyclerView.setHasFixedSize(true);

        // use a linear layout manager
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        // specify an adapter (see also next example)
        mAdapter = new MyAdapter(this, Arrays.asList(pathnames));
        recyclerView.setAdapter(mAdapter);

        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(),
                DividerItemDecoration.VERTICAL);
        recyclerView.addItemDecoration(dividerItemDecoration);

    }

    @Override
    public void onItemClick(View view, int position) {
        Toast.makeText(this, "You clicked " + mAdapter.getItem(position) + " on row number " + position, Toast.LENGTH_SHORT).show();
    }
}
