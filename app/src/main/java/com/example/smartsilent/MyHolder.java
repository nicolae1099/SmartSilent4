package com.example.smartsilent;

import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class MyHolder extends RecyclerView.ViewHolder {

    ImageView mImageView;
    CheckBox checkBox;
    TextView mName, mPhoneNumber;

    public MyHolder(@NonNull View itemView) {
        super(itemView);

        this.mImageView = itemView.findViewById(R.id.image_view);
        this.mName = itemView.findViewById(R.id.name_view);
        this.mPhoneNumber = itemView.findViewById(R.id.phone_number_view);
        checkBox = itemView.findViewById(R.id.checkBox);
    }
}
