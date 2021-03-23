package com.example.smartsilent.Contacts;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartsilent.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MyContactsAdapter extends RecyclerView.Adapter<MyContactsAdapter.MyHolder> implements Filterable {

    private Context c;
    private List<ContactModel> models;
    private final ArrayList<ContactModel> contacts;
    private HashMap<String, Integer> index_in_contacts;
    private int isSelectedAll = -1;

    public MyContactsAdapter(Context c, ArrayList<ContactModel> contacts) {
        this.c = c;
        this.models = contacts;
        this.contacts = contacts;
        
        index_in_contacts = new HashMap<>();

        for(int i = 0; i < models.size(); i++) {
            index_in_contacts.put(models.get(i).getName(), i);
        }
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    models = contacts;
                } else {
                    List<ContactModel> filteredList = new ArrayList<>();
                    for (ContactModel row : contacts) {

                        // name match condition. this might differ depending on your requirement
                        // here we are looking for name or phone number match
                        if (row.getName().toLowerCase().contains(charString.toLowerCase()) || row.getPhone_number().contains(charSequence)) {
                            filteredList.add(row);
                        }
                    }
                    models = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = models;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                models = (ArrayList<ContactModel>) filterResults.values;
                // refresh the list with filtered data
                notifyDataSetChanged();
            }
        };
    }

    @NonNull
    @Override
    public MyHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // inflate the row
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row, parent, false);
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
                contacts.get(index_in_contacts.get(models.get(i).getName())).check();
            }

        } else {
            isSelectedAll = 1 - isSelectedAll;

            for (int i = 0; i < models.size(); i++) {
                if(isSelectedAll == 0) {
                    models.get(i).uncheck();
                    contacts.get(index_in_contacts.get(models.get(i).getName())).uncheck();
                } else {
                    models.get(i).check();
                    contacts.get(index_in_contacts.get(models.get(i).getName())).check();
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
                        contacts.get(index_in_contacts.get(models.get(adapterPosition).getName())).check();

                    } else {
                        Log.e("Checked", "Checkbox number " + adapterPosition + "is unchecked.");
                        ((CheckBox) v).setChecked(false);
                        models.get(adapterPosition).uncheck();
                        contacts.get(index_in_contacts.get(models.get(adapterPosition).getName())).uncheck();

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
                contacts.get(index_in_contacts.get(models.get(position).getName())).uncheck();
            } else {
                Log.e("Checked", "Checkbox number " + position + "is checked.");
                models.get(position).check();
                contacts.get(index_in_contacts.get(models.get(position).getName())).check();
                checkBox.setChecked(true);
            }
        }

    }
}