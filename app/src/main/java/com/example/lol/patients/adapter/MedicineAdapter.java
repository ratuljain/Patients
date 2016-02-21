package com.example.lol.patients.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.lol.patients.R;
import com.example.lol.patients.model.Medicine;

import java.util.ArrayList;

public class MedicineAdapter extends ArrayAdapter<Medicine> {
    // View lookup cache
    private static class ViewHolder {
        TextView name;
        TextView quantity;
    }

    public MedicineAdapter(Context context, ArrayList<Medicine> users) {
        super(context, R.layout.chemist_medicine_view_layout, users);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Medicine user = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag
        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.chemist_medicine_view_layout, parent, false);
            viewHolder.name = (TextView) convertView.findViewById(R.id.medicineName);
            viewHolder.quantity = (TextView) convertView.findViewById(R.id.quantity);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        // Populate the data into the template view using the data object
        viewHolder.name.setText(user.name);
        viewHolder.quantity.setText(user.quantity);
        // Return the completed view to render on screen
        return convertView;
    }
}