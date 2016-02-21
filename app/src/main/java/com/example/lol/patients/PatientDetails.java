package com.example.lol.patients;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ListView;

import com.example.lol.patients.adapter.MedicineAdapter;
import com.example.lol.patients.model.Medicine;
import com.nhaarman.listviewanimations.appearance.simple.AlphaInAnimationAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class PatientDetails extends AppCompatActivity {

    private final String LOG_TAG = PatientDetails.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        ArrayList<Medicine> arrayOfUsers = new ArrayList<>();
        final HashMap<String, Integer> list;

//        TextView temp1 = (TextView) findViewById(R.id.docNamePatientDetails);
//        temp1.setText("My Awesome Text");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_details);

        final String s = getIntent().getStringExtra("mapJSON");

        try {
            list = JSON.chemistMedicineListHashMap(s);

            for (Map.Entry<String, Integer> entry : list.entrySet()){
                String name = entry.getKey();
                String quantity = Integer.toString(entry.getValue());

                arrayOfUsers.add(new Medicine(name, quantity));

//                Log.v(LOG_TAG, name + " - " + quantity);
            }

            // Create the adapter to convert the array to views

            MedicineAdapter adapter = new MedicineAdapter(this, arrayOfUsers);
            // Attach the adapter to a ListView
            AlphaInAnimationAdapter animationAdapter = new AlphaInAnimationAdapter(adapter);
            ListView listView = (ListView) findViewById(R.id.activity_list_view_drag_and_drop_dynamic_listview);
            animationAdapter.setAbsListView(listView);
            listView.setAdapter(animationAdapter);

        } catch (Exception e) {
            Log.e(LOG_TAG, "Something is wrong with JSON", e);
        }
    }

//            @Override
//        protected void onPostExecute(String s) {
////            Toast.makeText(getActivity(), s,
////                    Toast.LENGTH_LONG).show();
//        }
}
