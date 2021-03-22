package com.example.smartsilent.Contacts;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartsilent.R;

import java.util.ArrayList;
import java.util.HashMap;

public class MyContactsAdapter extends RecyclerView.Adapter<MyContactsAdapter.MyHolder> {

    Context c;
    ArrayList<ContactModel> models;
    int isSelectedAll = -1;

    public MyContactsAdapter(Context c, ArrayList<ContactModel> models) {
        this.c = c;
        this.models = models;

    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflate the row
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, null);
        return new MyHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyHolder holder, int position) {
        holder.mName.setText(models.get(position).getName());
        holder.mPhoneNumber.setText(models.get(position).getPhone_number());

        holder.bind(position);
    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public void changeSelectAll(){

        if (isSelectedAll == -1) {
            isSelectedAll = 1;

            for (int i = 0; i < models.size(); i++) {
                models.get(i).check();
            }

        } else {
            isSelectedAll = 1 - isSelectedAll;

            for (int i = 0; i < models.size(); i++) {
                if(isSelectedAll == 0) {
                    models.get(i).uncheck();
                } else {
                    models.get(i).check();
                }
            }
        }
        notifyDataSetChanged();
    }

    public class MyHolder extends RecyclerView.ViewHolder  {
        CheckBox checkBox;
        TextView mName, mPhoneNumber;

        public MyHolder(@NonNull View itemView) {
            super(itemView);

            this.mName = itemView.findViewById(R.id.name_view);
            this.mPhoneNumber = itemView.findViewById(R.id.phone_number_view);
            checkBox = itemView.findViewById(R.id.checkBox);

            checkBox.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Log.e("Checked", "de ce eu");
                    int adapterPosition = getAdapterPosition();
                    if (models.get(adapterPosition).getCheck() != 1) {
                        Log.e("Checked", "Checkbox number " + adapterPosition + "is checked.");
                        ((CheckBox) v).setChecked(true);
                        models.get(adapterPosition).check();

                    } else {
                        Log.e("Checked", "Checkbox number " + adapterPosition + "is unchecked.");
                        ((CheckBox) v).setChecked(false);
                        models.get(adapterPosition).uncheck();

                    }
                }
            });
        }

        void bind(int position) {
            // use the sparse boolean array to check
            if (models.get(position).getCheck() != 1) {
                Log.e("Checked", "Checkbox number " + position + "is unchecked.");
                models.get(position).uncheck();
                checkBox.setChecked(false);
            } else {
                Log.e("Checked", "Checkbox number " + position + "is checked.");
                models.get(position).check();
                checkBox.setChecked(true);
            }
        }

    }
}