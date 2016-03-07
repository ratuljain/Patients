package com.example.lol.patients.wizard;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.support.v7.app.ActionBarActivity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.lol.patients.R;
import com.example.lol.patients.SignInActivity;

public class WizardTravelActivity extends ActionBarActivity {

    private MyPagerAdapter adapter;
    private ViewPager pager;
    private TextView title;
    private TextView text;
    private TextView navigator;
    private TextView button;
    private int currentItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wizard_travel);

        WizardTravelActivity.this.getWindow().
                setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);


        currentItem = 0;

        pager = (ViewPager) findViewById(R.id.activity_wizard_travel_pager);
        title = (TextView) findViewById(R.id.activity_wizard_travel_title);
        text = (TextView) findViewById(R.id.activity_wizard_travel_text);
        navigator = (TextView) findViewById(R.id.activity_wizard_travel_possition);
        button = (TextView) findViewById(R.id.activity_wizard_travel_button);

        adapter = new MyPagerAdapter(getSupportFragmentManager());
        pager.setAdapter(adapter);
        pager.setCurrentItem(currentItem);

        setNavigator();
        //	title.setText("Fragment Example " + (currentItem + 1));
        //	text.setText("Text for Fragment Example " + (currentItem + 1) + " " + getString(R.string.lorem_ipsum_short));

        pager.setOnPageChangeListener(new OnPageChangeListener() {

            @Override
            public void onPageSelected(int position) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageScrolled(int arg0, float arg1, int arg2) {
                // TODO Auto-generated method stub

            }

            @Override
            public void onPageScrollStateChanged(int position) {
                // TODO Auto-generated method stub
                setNavigator();
            }
        });

        button.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                SharedPreferences userData = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                SharedPreferences.Editor editor = userData.edit();

                editor.putBoolean("wizardCompleted", true);
                editor.apply();

                Intent myIntent = new Intent(getApplicationContext(), SignInActivity.class);
                startActivity(myIntent);
            }
        });

    }

    public void setNavigator() {
        String navigation = "";
        for (int i = 0; i < adapter.getCount(); i++) {
            if (i == pager.getCurrentItem()) {
                navigation += getString(R.string.material_icon_point_full)
                        + "  ";
            } else {
                navigation += getString(R.string.material_icon_point_empty)
                        + "  ";
            }
        }
        navigator.setText(navigation);
        if(pager.getCurrentItem() == 0) {
            title.setText("Digital Prescriptions");
            text.setText("Never loose you Prescriptions. Your Prescriptions stay with you all the time and so does your Medical History");
        } else if(pager.getCurrentItem() == 1){
            title.setText("Medicine Reminders");
            text.setText("Forgot to take your medicine today? Don't worry we got your back." +
                    "We will remind you when it's time for your medicine without you ever having to set up anything.");
        }else if(pager.getCurrentItem() == 2){
            title.setText("Order Medicines Online");
            text.setText("Order your prescription medicines online with just a touch of a button. " +
                    "No need to send the the picture of your prescription" +
                    "Just click on Order and that's it");
        }
    }

    public void setCurrentSlidePosition(int position) {
        this.currentItem = position;
    }

    public int getCurrentSlidePosition() {
        return this.currentItem;
    }

    @Override
    public void onBackPressed() {
        moveTaskToBack(true);
    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
    }

    public class MyPagerAdapter extends FragmentPagerAdapter {

        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Override
        public Fragment getItem(int position) {
            if (position == 0) {
                return WizardTravelFragment.newInstance(position);
            } else if (position == 1) {
                return WizardTravelFragment.newInstance(position);
            } else {
                return WizardTravelFragment.newInstance(position);
            }
        }
    }
}