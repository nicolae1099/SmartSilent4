package com.example.smartsilent;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;


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
            @RequiresApi(api = Build.VERSION_CODES.O)
            public void onClick(View view) {

                Path path = FileSystems.getDefault().getPath(getApplicationContext().getFilesDir()+ "/profiles/" + mEdit.getText());
                if(Files.exists(path)) {
                    CharSequence text = "Profile with this name already exists!";
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(getApplicationContext(), text, duration);
                    toast.show();

                    mEdit.setText("", TextView.BufferType.NORMAL);
                } else {
                    Intent intent = new Intent(ProfileName.this, MakeProfile.class);

                    Bundle b = new Bundle();

                    b.putString("profile_name", mEdit.getText().toString());

                    intent.putExtras(b);
                    startActivity(intent);
                }

            }
        });

    }
}
