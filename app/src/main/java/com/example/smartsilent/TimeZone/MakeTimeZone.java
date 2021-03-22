package com.example.smartsilent.TimeZone;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smartsilent.R;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MakeTimeZone extends AppCompatActivity implements View.OnClickListener {

    // asta vom returna in activitatea precedenta
    TimeZoneData data;

    // butonul zilei apasat
    private int selectedDay;

    private List<MaterialButton> buttons;
    private static final int[] BUTTON_IDS = {
            R.id.button_day_1,
            R.id.button_day_2,
            R.id.button_day_3,
            R.id.button_day_4,
            R.id.button_day_5,
            R.id.button_day_6,
            R.id.button_day_7,
    };

    // pentru a stii indexul zilei pe baza id-ului butonului de zi
    private static final HashMap<Integer, Integer> BUTTON_ID_INDEX = new HashMap<Integer, Integer>(){{
        put(R.id.button_day_1, 0);  put(R.id.button_day_2, 1);  put(R.id.button_day_3, 2);  put(R.id.button_day_4, 3);
                put(R.id.button_day_5, 4);  put(R.id.button_day_6, 5);  put(R.id.button_day_7, 6);
    }};

    private static final int WEEK_DAYS_NUM = 7;
    private static final int HOURS_IN_A_DAY = 24;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.make_time_zone);
         buttons = new ArrayList<>(BUTTON_IDS.length);
        for(int id : BUTTON_IDS) {
            MaterialButton button = findViewById(id);
            button.setOnClickListener(this);
            buttons.add(button);
        }

        TimeZoneData data = this.getIntent().getParcelableExtra("time_zone");

    }


    @Override
    public void onClick(View view) {
        MaterialButton b = (MaterialButton) view;
        String buttonText = b.getText().toString();

        Log.e("TAG", "Value " + buttonText);

        /*

        // daca s-a apasat un buton corespunzator unei zile, marcam ziua selectata
        // Putem sa punem zi default "Sunday", iar cand apasa pe altceva se schimba.
        // Idem putem pune by default "AM".
        // si eventual coloram butonul zilei
        Integer index = BUTTON_ID_INDEX.get(b.getId());
        if(index != null) {
            selectedDay = index;
        }

        // DACA APASA PE UN BUTON DE ORA
        // iei tu valoarea orei
        int valoare_ora = 0;

        // transformi din 11PM in 23 PM
        //if(PM) {
            valoare_ora += 12;
      //  }
            */

        // facem toggle la valoarea de selectie a orei
        int hour = BUTTON_ID_INDEX.get(b.getId());
        boolean is_selected = data.getData().get(selectedDay)[hour];
        data.getData().get(selectedDay)[hour] = !is_selected;

        // DACA UNDEVA PUI UN BUTON DE SAVE, SI APASA PE EL
        Intent returnIntent = new Intent();
        returnIntent.putExtra("time_zone", data);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();
    }
}
