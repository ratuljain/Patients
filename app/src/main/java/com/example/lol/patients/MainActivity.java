package com.example.lol.patients;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.lol.patients.activity.MainAlarmActivity;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.mikepenz.fontawesome_typeface_library.FontAwesome;
import com.mikepenz.material_design_iconic_typeface_library.MaterialDesignIconic;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;

import java.util.ArrayList;
import java.util.HashMap;

public class MainActivity extends AppCompatActivity implements
        GoogleApiClient.OnConnectionFailedListener,
        View.OnClickListener {

    String receivedJSONString;
    ArrayList<HashMap<String, Object>> mapForList;
    private AccountHeader headerResult = null;
    EditText PatientName;
    public GoogleApiClient mGoogleApiClient;
    private final String LOG_TAG = MainActivity.class.getSimpleName();
    public SharedPreferences userData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarmain);
        setSupportActionBar(toolbar);

        toolbar.setNavigationIcon(R.drawable.ic_action_menu);

        getSupportActionBar().setDisplayShowTitleEnabled(false);

        CollapsingToolbarLayout collapsingToolbarLayout = (CollapsingToolbarLayout) findViewById(R.id.collapsing_toolbar_main);
        collapsingToolbarLayout.setTitle("K");
        collapsingToolbarLayout.setTitleEnabled(false);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .requestProfile()
                .requestIdToken("123945801611-k2sqmjukigvua9t2nhsb6a0lfluqneth.apps.googleusercontent.com")
                .build();
        // [END configure_signin]

        // [START build_client]
        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();


        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String email = preferences.getString("email", "");
        String first_name = preferences.getString("first_name", "");
        String last_name = preferences.getString("last_name", "");

        final IProfile profile = new ProfileDrawerItem().withName("Hi, " + first_name).withEmail(email)
                .withIcon("https://avatars3.githubusercontent.com/u/1476232?v=3&s=460").withIdentifier(100);

        headerResult = new AccountHeaderBuilder()
                .withActivity(this)
                .addProfiles(profile)
                .withTranslucentStatusBar(true)
                .withHeaderBackground(R.drawable.headerorange)
                .withSavedInstance(savedInstanceState)
                .build();


        SecondaryDrawerItem item1 = new SecondaryDrawerItem().withName("Profile").withIcon(MaterialDesignIconic.Icon.gmi_account).withIdentifier(1);
        SecondaryDrawerItem item2 = new SecondaryDrawerItem().withName("Your Prescriptions").withIcon(FontAwesome.Icon.faw_heart).withIdentifier(2);
        SecondaryDrawerItem item3 = new SecondaryDrawerItem().withName("Reminders").withIcon(FontAwesome.Icon.faw_clock_o).withIdentifier(3);
        SecondaryDrawerItem item4 = new SecondaryDrawerItem().withName("Transition").withIcon(FontAwesome.Icon.faw_paint_brush).withIdentifier(4);
        SecondaryDrawerItem item5 = new SecondaryDrawerItem().withName("Log Out").withIcon(FontAwesome.Icon.faw_power_off).withIdentifier(5);

        //create the drawer and remember the `Drawer` result object
        Drawer result = new DrawerBuilder()
                .withActivity(this)
                .withActionBarDrawerToggleAnimated(true)
                .withTranslucentNavigationBar(true)
                .withShowDrawerOnFirstLaunch(true)
                .withActionBarDrawerToggleAnimated(true)
                .withAccountHeader(headerResult)
                .addDrawerItems(
                        item1,
                        item2,
                        item3,
                        item4,
                        item5
                )
                .withOnDrawerItemClickListener(new Drawer.OnDrawerItemClickListener() {
                    @Override
                    public boolean onItemClick(View view, int position, IDrawerItem drawerItem) {
                        // do something with the clicked item :D


                        if (drawerItem.getIdentifier() == 1) {
                            Intent myIntent = new Intent(getApplicationContext(), Profile.class);
                            startActivity(myIntent);
                        } else if (drawerItem.getIdentifier() == 2) {
                            Intent myIntent = new Intent(getApplicationContext(), PrescriptionCards.class);
                            startActivity(myIntent);
                        } else if(drawerItem.getIdentifier() == 3){
                            Intent myIntent = new Intent(getApplicationContext(), MainAlarmActivity.class);
                            startActivity(myIntent);
                        } else if(drawerItem.getIdentifier() == 4){
                            Intent myIntent = new Intent(getApplicationContext(), RevealAnimation.class);
                            startActivity(myIntent);
                        } else if(drawerItem.getIdentifier() == 5){
//                            Toast.makeText(getApplicationContext(), "your orders :P", Toast.LENGTH_LONG).show();
//                            Intent myIntent = new Intent(getApplicationContext(), MainAlarmActivity.class);
//                            startActivity(myIntent);
                            signOut();
                        }

                        return true;
                    }
                })
                .build();


        Button prescriptionView = (Button) findViewById(R.id.submit);

        prescriptionView.setOnClickListener(new Button.OnClickListener() {
            public void onClick(View v) {

                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                String name = preferences.getString("patient_id", "");
                Log.v(LOG_TAG, name );

                Intent myIntent = new Intent(getApplicationContext(), PrescriptionCards.class);

                myIntent.putExtra("JSON", name); //Optional parameters
                Log.v("MainMethod", name);
                startActivity(myIntent);


            }
        });

    }

    public void onBackPressed() {
        Intent startMain = new Intent(Intent.ACTION_MAIN);
        startMain.addCategory(Intent.CATEGORY_HOME);
        startMain.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(startMain);
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        // An unresolvable error has occurred and Google APIs (including Sign-In) will not
        // be available.
        Log.d(LOG_TAG, "onConnectionFailed:" + connectionResult);
    }

    // [START signOut]
    public void signOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        // [START_EXCLUDE]
                        // [END_EXCLUDE]
                        Toast.makeText(getApplicationContext(), "Logged out", Toast.LENGTH_LONG).show();
                        userData = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        userData.edit().clear().apply();

                        Intent i = new Intent(getApplicationContext(), SignInActivity.class);
                        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                        // Add new Flag to start new Activity
                        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(i);
                        finish();
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
