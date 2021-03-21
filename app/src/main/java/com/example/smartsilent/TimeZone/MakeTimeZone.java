package com.example.smartsilent.TimeZone;

import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.smartsilent.R;
import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

public class MakeTimeZone extends AppCompatActivity implements View.OnClickListener {

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
    }


    @Override
    public void onClick(View view) {
        MaterialButton b = (MaterialButton) view;
        String buttonText = b.getText().toString();
        Log.e("TAG", "Value " + buttonText);
    }
}
