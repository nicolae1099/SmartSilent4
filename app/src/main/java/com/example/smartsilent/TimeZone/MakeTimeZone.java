package com.example.smartsilent.TimeZone;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.smartsilent.R;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MakeTimeZone extends AppCompatActivity implements View.OnClickListener {

    // asta vom returna in activitatea precedenta
    TimeZoneData data;

    // butonul zilei apasat
    private int selectedDay;
    private int previousSelectedDayId = 0;

    private List<MaterialButton> buttons_days;
    private MaterialButton selectAM;
    private MaterialButton selectPM;

    private List<MaterialButton> buttons_hours;

    private static final Set<Integer> BUTTON_DAY_IDS = Collections.unmodifiableSet(
            new HashSet<>(Arrays.asList(
            R.id.button_day_1,
            R.id.button_day_2,
            R.id.button_day_3,
            R.id.button_day_4,
            R.id.button_day_5,
            R.id.button_day_6,
            R.id.button_day_7
            )));

    private static final Set<Integer> BUTTON_HOUR_IDS = Collections.unmodifiableSet(
            new HashSet<>(Arrays.asList(
                    R.id.button_hour_1,
                    R.id.button_hour_2,
                    R.id.button_hour_3,
                    R.id.button_hour_4,
                    R.id.button_hour_5,
                    R.id.button_hour_6,
                    R.id.button_hour_7,
                    R.id.button_hour_8,
                    R.id.button_hour_9,
                    R.id.button_hour_10,
                    R.id.button_hour_11,
                    R.id.button_hour_12
            )));

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
        buttons_days = new ArrayList<>(BUTTON_DAY_IDS.size());
        buttons_hours = new ArrayList<>(BUTTON_HOUR_IDS.size());

        for(int id : BUTTON_DAY_IDS) {
            MaterialButton button = findViewById(id);
            button.setOnClickListener(this);
            button.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.button_time_zone));
            buttons_days.add(button);
        }

        for(int id : BUTTON_HOUR_IDS) {
            MaterialButton button = findViewById(id);
            button.setOnClickListener(this);
            button.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.button_time_zone));
            buttons_hours.add(button);
        }

        selectAM = findViewById(R.id.button_select_am);
        selectPM = findViewById(R.id.button_select_pm);
        selectAM.setOnClickListener(this);
        selectAM.setOnClickListener(this);
        selectAM.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.button_time_zone_selected));


        data = this.getIntent().getParcelableExtra("time_zone");
    }

    @Override
    public void onClick(View view) {
        MaterialButton button = (MaterialButton) view;
        String buttonText = button.getText().toString();

        Log.e("TAG", "Value " + buttonText);

        if (BUTTON_DAY_IDS.contains(view.getId())) {

            if (button.getBackgroundTintList() == ContextCompat.getColorStateList(this, R.color.button_time_zone)) {
                buttons_days.get(previousSelectedDayId)
                        .setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.button_time_zone));

                if (BUTTON_ID_INDEX.get(button.getId()) != null) {
                    previousSelectedDayId = BUTTON_ID_INDEX.get(button.getId());
                }

                button.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.button_time_zone_selected));
            } else {
                button.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.button_time_zone));
            }

        } else if (BUTTON_HOUR_IDS.contains(view.getId())) {
            if (button.getBackgroundTintList() == ContextCompat.getColorStateList(this, R.color.button_time_zone)) {
                button.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.button_time_zone_selected));
            } else {
                button.setBackgroundTintList(ContextCompat.getColorStateList(this, R.color.button_time_zone));
            }
        } else if (view.getId() == R.id.button_select_am) {

        } else if (view.getId() == R.id.button_select_pm) {

        }




/*
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
        /*int hour = BUTTON_ID_INDEX.get(button.getId());
        boolean is_selected = data.getData().get(selectedDay)[hour];
        data.getData().get(selectedDay)[hour] = !is_selected;

        // DACA UNDEVA PUI UN BUTON DE SAVE, SI APASA PE EL
        Intent returnIntent = new Intent();
        returnIntent.putExtra("time_zone", data);
        setResult(Activity.RESULT_OK,returnIntent);
        finish();

         */
    }
}
