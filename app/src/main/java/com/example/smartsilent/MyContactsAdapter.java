package com.example.smartsilent;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyContactsAdapter extends RecyclerView.Adapter<MyHolder> {

    Context c;
    ArrayList<Model> models;
    boolean isSelectedAll = false;
    boolean changedSelected = false;

    public MyContactsAdapter(Context c, ArrayList<Model> models) {
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
        holder.mImageView.setImageResource(models.get(position).getImg());

        if(isSelectedAll) {
            Log.e("IsChecked", "checked");
        } else {
            Log.e("IsChecked", "unchecked");
        }

        if (isSelectedAll){
            holder.checkBox.setChecked(true);
            models.get(position).check();
        } else if (!holder.checkBox.isChecked()) {
            holder.checkBox.setChecked(false);
            models.get(position).uncheck();
        } else {
            holder.checkBox.setChecked(false);
            models.get(position).uncheck();
        }

    }

    @Override
    public int getItemCount() {
        return models.size();
    }

    public void changeSelectAll(){

        isSelectedAll=!isSelectedAll;
        notifyDataSetChanged();
    }

    public ArrayList<Model> getModels() {
        return models;
    }

}