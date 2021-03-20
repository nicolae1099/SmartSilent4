package com.example.smartsilent;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.Arrays;
/**Momentan nici asta nu face nimic. Aici o sa schimbam si vom afisa eventual contactele puse pe silent,
 * intervalele orare si locatiile. Si sa adaugam posibilitatea de a si sterge. */

public class MyProfiles extends AppCompatActivity{

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
}
