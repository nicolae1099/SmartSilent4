package com.example.smartsilent.TimeZone;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smartsilent.MakeProfile;
import com.example.smartsilent.R;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MakeTimeZone extends AppCompatActivity implements View.OnClickListener {

    // asta vom returna in activitatea precedenta
    // hours[0] = "8:00-12:00,14:00-15:00" <=> Duminica sunt selectate aceste intervale orare
    private ArrayList<String> hours;

    // selected_hours_per_day[0] <=> orele selectate duminica
    // ex: <0, <12:00, true>>
    //  <0, <13:00, true>>
    //  <0, <14:00, false>>
    //  <0, <15:00, true>>
    //  <0, <16:00, true>>
    //  <0, <17:00, true>>
    // => interval 1: 12-14
    // => interval 2: 15-17
    private HashMap<Integer, HashMap<Integer, Boolean>> selected_hours_per_day;

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

        hours = new ArrayList<>();

        for(int i = 0; i < WEEK_DAYS_NUM; i++) {
            hours.add("");
        }

        selected_hours_per_day = new HashMap<>();
        for(int i = 0; i < WEEK_DAYS_NUM; i++) {
            selected_hours_per_day.put(i, new HashMap<Integer, Boolean>());
            for(int j = 0; j < HOURS_IN_A_DAY; j++) {
                selected_hours_per_day.get(i).put(j, false);
            }
        }
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

        // facem toggle la valoarea de selectie a orei
        Boolean is_selected = selected_hours_per_day.get(selectedDay).get(valoare_ora);
        selected_hours_per_day.get(selectedDay).put(valoare_ora, !is_selected);

        // DACA UNDEVA PUI UN BUTON DE SAVE, SI APASA PE EL
        // pentru fiecare zi a saptamanii, salvam intervalele orare
        for(int i = 0; i < WEEK_DAYS_NUM; i++) {
            StringBuilder sb = new StringBuilder();

            // orele selectate din zi
            HashMap<Integer, Boolean> hours_in_day = selected_hours_per_day.get(i);

            int min = 0, max = 0;

            // adaugam intervalele orare din zi
            for(int j = 0; j < HOURS_IN_A_DAY; j++) {

                boolean selected =  hours_in_day.get(j);

                if(selected) {
                    max++;

                } else {

                    if(min != max) {

                        if(sb.length() > 0) {
                            sb.append(",");
                        }

                        sb.append(min);
                        sb.append("-");
                        sb.append(max);
                    }

                    min = j + 1;
                    max = j + 1;

                }
            }
            hours.add(sb.toString());
        }

       //  Acum, punem un extra
        Intent intent = new Intent(MakeTimeZone.this, MakeProfile.class);
        intent.putStringArrayListExtra("time_zone", hours);
        */

    }
}
