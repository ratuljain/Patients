package com.example.lol.patients;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import com.example.lol.patients.adapter.PrescriptionRecyclerAdapter;
import com.example.lol.patients.model.PrescriptionPatient;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;

import jp.wasabeef.recyclerview.adapters.AlphaInAnimationAdapter;
import jp.wasabeef.recyclerview.adapters.ScaleInAnimationAdapter;

public class PrescriptionCards extends AppCompatActivity {

    private final String LOG_TAG = PrescriptionCards.class.getSimpleName();
    String receivedJSONString;
    ArrayList<HashMap<String, Object>> mapForList;
    ArrayList<PrescriptionPatient> arrayOfPrescriptions;
    Toast mtoast;
    private CoordinatorLayout coordinatorLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

//        ArrayList<PrescriptionPatient> arrayOfPrescriptions = new ArrayList<>();  // stores objects of prescription class

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_prescription_cards);

        final String s = getIntent().getStringExtra("JSON");

//        coordinatorLayout=(CoordinatorLayout)findViewById(R.id.main_contentPreList);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String name = preferences.getString("patient_id", "");

        CallAPI c = new CallAPI();
        c.execute(name);

    }

    private class CallAPI extends AsyncTask<String, String, String> {

        ProgressDialog pDialog = new ProgressDialog(PrescriptionCards.this);

//        private final String LOG_TAG = CallAPI.class.getSimpleName();

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog.setMessage("Creating Prescription...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }


        @Override
        protected String doInBackground(String... params) {

            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String patientDetails;

            try {

                String ip = "http://104.131.46.2:5000/Prescription/api/v1.0/prescription/all/" + params[0].trim();
//                String ip = "http://10.0.3.2:5000/Prescription/api/v1.0/prescription/" + params[0].trim();

                URL url = new URL(ip);

                Log.v(LOG_TAG, ip);

                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }

                patientDetails = buffer.toString();

//                Log.v(LOG_TAG, patientDetails);

            } catch (Exception e) {

                Log.e(LOG_TAG, e.getMessage());

                return e.getMessage();

            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }

            return patientDetails;

        }

        @Override
        protected void onPostExecute(String v) {

            pDialog.dismiss();

            arrayOfPrescriptions = new ArrayList<>();

            try {
                receivedJSONString = v;
                mapForList = JSON.getHashMapforPrescriptionList(receivedJSONString);

                for (HashMap<String, Object> maps : mapForList) {
                    String id = (String) maps.get("presID");
                    String docName = (String) maps.get("DocName");
                    Date date = (Date) maps.get("presDate");
                    String mapJson = (String) maps.get("jsonFile");
                    String Diagnosis = getResources().getString(R.string.diagnosis);

                    Log.v(LOG_TAG, "Doc name - " + mapJson);

                    arrayOfPrescriptions.add(new PrescriptionPatient(docName, date, Diagnosis, mapJson));
                }

                RecyclerView recList = (RecyclerView) findViewById(R.id.cardList);

                recList.setHasFixedSize(true);
                LinearLayoutManager llm = new LinearLayoutManager(getApplicationContext());
                llm.setOrientation(LinearLayoutManager.VERTICAL);
                recList.setLayoutManager(llm);

                PrescriptionRecyclerAdapter ca = new PrescriptionRecyclerAdapter(arrayOfPrescriptions);

                AlphaInAnimationAdapter alphaAdapter = new AlphaInAnimationAdapter(ca);
                ScaleInAnimationAdapter scaleAdapter = new ScaleInAnimationAdapter(alphaAdapter);

                recList.setAdapter(scaleAdapter);

            } catch (Exception ei) {
                Toast.makeText(getApplicationContext(), "Unable to connect to the server",
                        Toast.LENGTH_LONG).show();
                ei.printStackTrace();

            }
        }
    }
}
