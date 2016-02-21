package com.example.lol.patients;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity {


    String receivedJSONString;
    ArrayList<HashMap<String, Object>> mapForList;
    EditText PatientName;
    private final String LOG_TAG = MainActivity.class.getSimpleName();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        PatientName = (EditText) findViewById(R.id.patientName);
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(PatientName, InputMethodManager.SHOW_IMPLICIT);

        Button prescriptionView = (Button) findViewById(R.id.submit);

        prescriptionView.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {

                Intent myIntent = new Intent(getApplicationContext(), PrescriptionCards.class);
                String name = PatientName.getText().toString();
                myIntent.putExtra("JSON", name); //Optional parameters
                Log.v("MainMethod", name);
                startActivity(myIntent);

            }
        });

    }


//    private class CallAPI extends AsyncTask<String, String, String> {
//
////        private final String LOG_TAG = CallAPI.class.getSimpleName();
//
//
//        @Override
//        protected String doInBackground(String... params) {
//
//            HttpURLConnection urlConnection = null;
//            BufferedReader reader = null;
//            String patientDetails;
//
//            try {
//
//                String ip = "http://104.131.46.2:5000/Prescription/api/v1.0/prescription/all/" + params[0].trim();
////                String ip = "http://10.0.3.2:5000/Prescription/api/v1.0/prescription/" + params[0].trim();
//
//                URL url = new URL(ip);
//
//                Log.v(LOG_TAG, ip);
//
//                urlConnection = (HttpURLConnection) url.openConnection();
//                urlConnection.setRequestMethod("GET");
//                urlConnection.connect();
//
//                InputStream inputStream = urlConnection.getInputStream();
//                StringBuffer buffer = new StringBuffer();
//                if (inputStream == null) {
//                    // Nothing to do.
//                    return null;
//                }
//                reader = new BufferedReader(new InputStreamReader(inputStream));
//
//                String line;
//                while ((line = reader.readLine()) != null) {
//                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
//                    // But it does make debugging a *lot* easier if you print out the completed
//                    // buffer for debugging.
//                    buffer.append(line + "\n");
//                }
//
//                if (buffer.length() == 0) {
//                    // Stream was empty.  No point in parsing.
//                    return null;
//                }
//
//                patientDetails = buffer.toString();
//
////                Log.v(LOG_TAG, patientDetails);
//
//            } catch (Exception e) {
//
//                Log.e(LOG_TAG, e.getMessage());
//
//                return e.getMessage();
//
//            } finally {
//                if (urlConnection != null) {
//                    urlConnection.disconnect();
//                }
//                if (reader != null) {
//                    try {
//                        reader.close();
//                    } catch (final IOException e) {
//                        Log.e(LOG_TAG, "Error closing stream", e);
//                    }
//                }
//            }
//
//            return patientDetails;
//
//        }
//
////        @Override
////        protected void onPostExecute(String s) {
////            Toast.makeText(getActivity(), s,
////                    Toast.LENGTH_LONG).show();
////        }
//
//
//    }


}
