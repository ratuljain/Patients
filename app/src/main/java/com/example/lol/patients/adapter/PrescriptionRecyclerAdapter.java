package com.example.lol.patients.adapter;

/**
 * Created by lol on 14/02/16.
 */

import android.content.Intent;
import android.os.AsyncTask;
import android.os.StrictMode;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lol.patients.HttpPost;
import com.example.lol.patients.JSON;
import com.example.lol.patients.PatientDetails;
import com.example.lol.patients.R;
import com.example.lol.patients.model.PrescriptionPatient;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;

public class PrescriptionRecyclerAdapter extends RecyclerView.Adapter<PrescriptionRecyclerAdapter.ContactViewHolder> {

    private List<PrescriptionPatient> contactList;
//    static CreateDoctor c;

    public PrescriptionRecyclerAdapter(List<PrescriptionPatient> contactList) {
        this.contactList = contactList;
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    @Override
    public void onBindViewHolder(ContactViewHolder contactViewHolder, int i) {


        PrescriptionPatient ci = contactList.get(i);

        SimpleDateFormat df = new SimpleDateFormat("dd/MM/yyyy hh:mm");

        contactViewHolder.vDate.setText(df.format(ci.date).toString());
        contactViewHolder.vDocName.setText(ci.docname);
        contactViewHolder.vDiagnosis.setText(ci.diagnosis);
        contactViewHolder.vMapJson.setText(ci.mapJson);
    }

    @Override
    public ContactViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.prescription_card_layout, viewGroup, false);

        return new ContactViewHolder(itemView);
    }

    public static class ContactViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

//        public View view;
//        public ClipData.Item currentItem;

        HashMap<String, Object> orderMAP;
        JSONObject amap;
        protected HashMap<String, Integer> list;
        protected TextView vDate;
        protected TextView vDocName;
        protected TextView vDiagnosis;
        protected TextView vMapJson;
        protected TextView vMedicinesButton;
        protected TextView vOrderButton;

        public ContactViewHolder(View v) {
            super(v);
            vMedicinesButton = (TextView) v.findViewById(R.id.medicineButton);
            vOrderButton = (TextView) v.findViewById(R.id.orderButton);
            vMedicinesButton.setOnClickListener(this);
            vOrderButton.setOnClickListener(this);

//            uncomment if you want to click on the card
//            v.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//
//                    Intent myIntent = new Intent(view.getContext(), PatientDetails.class);
//                    String name = vMapJson.getText().toString();
//                    myIntent.putExtra("mapJSON", name); //Optional parameters
//                    Log.v("MainMethod", name);
//                    view.getContext().startActivity(myIntent);
////                    Toast.makeText(view.getContext(), name,
////                            Toast.LENGTH_LONG).show();
//
//                }
//            });

            vDate =  (TextView) v.findViewById(R.id.date);
            vDocName = (TextView)  v.findViewById(R.id.docnameCard);
            vDiagnosis = (TextView)  v.findViewById(R.id.diagnosis);
            vMapJson = (TextView) v.findViewById(R.id.medListJson);
        }

        @Override
        public void onClick(View view) {

            if (view.getId() == vMedicinesButton.getId()){

                Intent myIntent = new Intent(view.getContext(), PatientDetails.class);
                String name = vMapJson.getText().toString();
                myIntent.putExtra("mapJSON", name); //Optional parameters
                Log.v("MainMethod", name);
                view.getContext().startActivity(myIntent);

            } else {
                String name = vMapJson.getText().toString();
                Log.v("MyGcmListenerService", name);

                try{
                    list = JSON.chemistMedicineListHashMap(name.toString());
                    amap = new JSONObject(list);
                    Log.v("MyGcmListenerService", amap.toString());
                }catch (JSONException e){
                    Log.v("MyGcmListenerService", e.getMessage());
                }

                orderMAP = new HashMap<>();

                orderMAP.put("patient_id", 3);
                orderMAP.put("chemist_id", 1);
                orderMAP.put("order", amap);

                JSONObject orderJSON = null;

                try{
                    orderJSON = new JSONObject(orderMAP);
                }catch (Exception e){
                    Log.v("MyGcmListenerService", e.getMessage());
                }


                Log.v("MyGcmListenerService", orderJSON.toString());

                Toast.makeText(view.getContext(), orderJSON.toString(), Toast.LENGTH_SHORT).show();


                StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();

                StrictMode.setThreadPolicy(policy);
                HttpPost.SendHttpPost("http://104.131.46.2:5000/Prescription/api/v1.0/orders", orderJSON);
//                c.execute(orderJSON);

            }
        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }
    }

    public class CreateDoctor extends AsyncTask<JSONObject, Void, Void> {

        @Override
        protected Void doInBackground(JSONObject... params) {

            JSONObject jsonObjRecv = null;
            JSONObject jsonObjectSend = params[0];
            Log.d("jsonObjSend : ", jsonObjectSend.toString());
            jsonObjRecv = HttpPost.SendHttpPost("http://104.131.46.2:5000/Prescription/api/v1.0/orders", jsonObjectSend);

            if(jsonObjRecv != null) {
                Log.d("Received Json : ", jsonObjRecv.toString());
            }else{
                Log.d("Received Json : ", "failed");
            }

            return null;
        }


    }
}
