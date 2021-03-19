package com.example.smartsilent;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Switch;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    private List<String> mData;
    private LayoutInflater mInflater;

    // data is passed into the constructor
    MyAdapter(Context context, List<String> data) {
        this.mInflater = LayoutInflater.from(context);
        this.mData = data;
    }

    // inflates the row layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = mInflater.inflate(R.layout.recyclerview_row, parent, false);
        return new ViewHolder(view);
    }

    // binds the data to the TextView in each row
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.myTextView.setText(mData.get(position));
    }

    // total number of rows
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // stores and recycles views as they are scrolled off screen
    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView myTextView;
        Switch switchProfile;
        ImageButton trash_button;
        ViewHolder(View itemView) {
            super(itemView);
            myTextView = itemView.findViewById(R.id.profile_name_recyclerview);
            switchProfile = itemView.findViewById(R.id.switch_profile);
            trash_button = itemView.findViewById(R.id.trash_button);

            switchProfile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Boolean switchState = switchProfile.isChecked();
                    if (switchState == true) {
                        switchProfile.setText("Activ");
                    } else {
                        switchProfile.setText("Inactiv");
                    }
                }
            });
        }
    }

    // convenience method for getting data at click position
    String getItem(int id) {
        return mData.get(id);
    }

}