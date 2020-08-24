package com.example.smartsilent;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;


public class ProfileName extends AppCompatActivity {
    Button mButton;
    EditText mEdit;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.profile_name);

        mEdit   = (EditText)findViewById(R.id.profile_name);
        mEdit.getText().toString();

        mButton = (Button)findViewById(R.id.confirm_button);
        mButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View view) {
                Intent intent = new Intent(ProfileName.this, MakeProfile.class);

                Bundle b = new Bundle();

                b.putString("profile_name", mEdit.getText().toString());

                intent.putExtras(b);
                startActivity(intent);
            }
        });

    }
}
