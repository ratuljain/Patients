package com.example.lol.patients.adapter;

/**
 * Created by lol on 14/02/16.
 */

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
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

    public static ProgressDialog dialog;
    private List<PrescriptionPatient> contactList;
//    static CreateDoctor c;

    public PrescriptionRecyclerAdapter() {
        contactList = null;
    }

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

            vDate = (TextView) v.findViewById(R.id.date);
            vDocName = (TextView) v.findViewById(R.id.docnameCard);
            vDiagnosis = (TextView) v.findViewById(R.id.diagnosis);
            vMapJson = (TextView) v.findViewById(R.id.medListJson);

            dialog = new ProgressDialog(v.getContext());
            dialog.setMessage("Please Wait..");
        }

        @Override
        public void onClick(final View view) {

            if (view.getId() == vMedicinesButton.getId()) {

                Intent myIntent = new Intent(view.getContext(), PatientDetails.class);
                String name = vMapJson.getText().toString();
                myIntent.putExtra("mapJSON", name); //Optional parameters
                Log.v("MainMethod", name);
                view.getContext().startActivity(myIntent);

            } else {

                new AlertDialog.Builder(view.getContext())
                        .setIcon(R.mipmap.ic_report)
                        .setTitle("Order Confirmation")
                        .setMessage("Do you want to place the order?")
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                String name = vMapJson.getText().toString();
                                Log.v("MyGcmListenerService", name);

                                try {
                                    list = JSON.chemistMedicineListHashMap(name.toString());
                                    amap = new JSONObject(list);
                                    Log.v("MyGcmListenerService", amap.toString());
                                } catch (JSONException e) {
                                    Log.v("MyGcmListenerService", e.getMessage());
                                }

                                orderMAP = new HashMap<>();

                                orderMAP.put("patient_id", 1);
                                orderMAP.put("chemist_id", 1);
                                orderMAP.put("order", amap);
                                orderMAP.put("json", name);

                                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(view.getContext());
                                String patientID = preferences.getString("personId", "");
                                Log.v("MyGcmListenerService", patientID);

                                JSONObject orderJSON = null;

                                try {
                                    orderJSON = new JSONObject(orderMAP);
                                } catch (Exception e) {
                                    Log.v("MyGcmListenerService", e.getMessage());
                                }

                                Log.v("MyGcmListenerService", orderJSON.toString());

                                Toast.makeText(view.getContext(), "Order Placed", Toast.LENGTH_SHORT).show();

                                PrescriptionRecyclerAdapter outerclass = new PrescriptionRecyclerAdapter();
                                CreateDoctor c = outerclass.new CreateDoctor();

                                c.execute(orderJSON);
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();

            }
        }

        @Override
        public boolean onLongClick(View v) {
            return false;
        }
    }

    public class CreateDoctor extends AsyncTask<JSONObject, Void, Void> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            dialog.setIndeterminate(false);
            dialog.setCancelable(true);
            dialog.show();
        }


        @Override
        protected Void doInBackground(JSONObject... params) {

            JSONObject jsonObjRecv = null;
            JSONObject jsonObjectSend = params[0];
            Log.d("jsonObjSend : ", jsonObjectSend.toString());
            jsonObjRecv = HttpPost.SendHttpPost("http://104.131.46.2:5000/Prescription/api/v1.0/orders", jsonObjectSend);

            if (jsonObjRecv != null) {
                Log.d("Received Json : ", jsonObjRecv.toString());
            } else {
                Log.d("Received Json : ", "failed");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            dialog.dismiss();
        }

    }
}
