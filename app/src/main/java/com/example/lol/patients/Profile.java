package com.example.lol.patients;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.regex.Pattern;

public class Profile extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText inputPhone, inputAddress, inputBgrp;
    private TextInputLayout inputLayoutName, inputLayoutEmail, inputLayoutPassword;
    private Button btnSignUp;
    private CoordinatorLayout coordinatorLayout;
    private GoogleApiClient mGoogleApiClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_profile);


//        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//                .requestEmail()
//                .requestProfile()
//                .requestIdToken("123945801611-k2sqmjukigvua9t2nhsb6a0lfluqneth.apps.googleusercontent.com")
//                .build();
        // [END configure_signin]

        // [START build_client]
        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
//        mGoogleApiClient.




//        toolbar = (Toolbar) findViewById(R.id.collapsing_toolbar_main);
//        setSupportActionBar(toolbar);
//
//        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_main);
//        collapsingToolbarLayout.setTitle("K");
//        collapsingToolbarLayout.setTitleEnabled(false);
        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_main);
        collapsingToolbarLayout.setTitle("K");
        collapsingToolbarLayout.setTitleEnabled(false);


        inputLayoutName = (TextInputLayout) findViewById(R.id.input_layout_name);
        inputLayoutEmail = (TextInputLayout) findViewById(R.id.input_layout_email);
        inputLayoutPassword = (TextInputLayout) findViewById(R.id.input_layout_password);

        Typeface type = Typeface.createFromAsset(getAssets(),"fonts/Roboto-Light.ttf");

        inputLayoutName.setTypeface(type);
        inputLayoutEmail.setTypeface(type);
        inputLayoutPassword.setTypeface(type);


        inputPhone = (EditText) findViewById(R.id.input_name);
        inputAddress = (EditText) findViewById(R.id.input_email);
        inputBgrp = (EditText) findViewById(R.id.input_password);
        btnSignUp = (Button) findViewById(R.id.btn_signup);

        inputPhone.addTextChangedListener(new MyTextWatcher(inputPhone));
        inputAddress.addTextChangedListener(new MyTextWatcher(inputAddress));
        inputBgrp.addTextChangedListener(new MyTextWatcher(inputBgrp));

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String phone = inputPhone.getText().toString();
                String address = inputAddress.getText().toString();
                String blood_grp = inputBgrp.getText().toString();

                HashMap<String, String> res = new HashMap<String, String>();
                res.put("phone", phone);
                res.put("address", address);
                res.put("blood_grp", blood_grp);

                Log.v("Profile", res.toString());

                CreateDoctor c = new CreateDoctor();
                c.execute(new JSONObject(res));

                submitForm();

                SharedPreferences userData = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = userData.edit();
                editor.putBoolean("profileCompleted", true);
                editor.apply();
            }
        });
    }

    /**
     * Validating form
     */
    private void submitForm() {
        if (!validatePhone()) {
            return;
        }

        if (!validateEmail()) {
            return;
        }

        if (!validateBloodGrp()) {
            return;
        }

        Toast.makeText(getApplicationContext(), "Thank You!", Toast.LENGTH_SHORT).show();
    }

    private boolean validatePhone() {
        String number = inputPhone.getText().toString().trim();

        if (number.isEmpty() || !isValidPhoneNumber(number)) {
            inputLayoutName.setError(getString(R.string.err_msg_name));
            requestFocus(inputPhone);
            return false;
        } else {
            inputLayoutName.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateEmail() {
        String email = inputAddress.getText().toString().trim();

        if (email.length() < 10) {
            inputLayoutEmail.setError(getString(R.string.err_msg_email));
            requestFocus(inputAddress);
            return false;
        } else {
            inputLayoutEmail.setErrorEnabled(false);
        }

        return true;
    }

    private boolean validateBloodGrp() {
        String bGrp = inputBgrp.getText().toString().trim();

        if (!isValidBloodGroup(bGrp)) {
            inputLayoutPassword.setError(getString(R.string.err_msg_password));
            requestFocus(inputBgrp);
            return false;
        } else {
            inputLayoutPassword.setErrorEnabled(false);
        }

        return true;
    }

    private static boolean isValidEmail(String email) {
        return !TextUtils.isEmpty(email) && android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private static boolean isValidPhoneNumber(String number) {
        return !TextUtils.isEmpty(number) && Patterns.PHONE.matcher(number).matches() && number.length() == 10;
    }

    private static boolean isValidBloodGroup(String bg) {
        Pattern sPattern = Pattern.compile("(A|B|AB|O)[+-]");

        return !TextUtils.isEmpty(bg) && sPattern.matcher(bg).matches();
    }

    private void requestFocus(View view) {
        if (view.requestFocus()) {
            getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_VISIBLE);
        }
    }

    private class MyTextWatcher implements TextWatcher {

        private View view;

        private MyTextWatcher(View view) {
            this.view = view;
        }

        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        }

        public void afterTextChanged(Editable editable) {
            switch (view.getId()) {
                case R.id.input_name:
                    validatePhone();
                    break;
                case R.id.input_email:
                    validateEmail();
                    break;
                case R.id.input_password:
                    validateBloodGrp();
                    break;
            }
        }
    }

    class CreateDoctor extends AsyncTask<JSONObject, Void, Void> {

        ProgressDialog pDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(Profile.this);
            pDialog.setMessage("Creating Prescription...");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }

        @Override
        protected Void doInBackground(JSONObject... params) {

            JSONObject jsonObjRecv = null;
            JSONObject jsonObjectSend = params[0];
            Log.d("jsonObjSend : ", jsonObjectSend.toString());

            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
            String name = preferences.getString("patient_id", "");


            String url = "http://104.131.46.2:5000/Prescription/api/v1.0/patients/" + name;
            Log.v("Profile", url);

            jsonObjRecv = HttpPost.SendHttpPost(url, jsonObjectSend);

            if(jsonObjRecv != null) {
                Log.d("Received Json : ", jsonObjRecv.toString());
            }else{
                Log.d("Received Json : ", "failed");
            }

            return null;
        }

        @Override
        protected void onPostExecute(Void v) {
            pDialog.dismiss();
        }
    }
}