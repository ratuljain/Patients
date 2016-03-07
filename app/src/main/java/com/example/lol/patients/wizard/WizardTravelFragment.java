package com.example.lol.patients.wizard;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lol.patients.R;


public class WizardTravelFragment extends Fragment {

    private static final String ARG_POSITION = "position";

    private int position;
    private ImageView image;
    private TextView title;

    public static WizardTravelFragment newInstance(int position) {
        WizardTravelFragment f = new WizardTravelFragment();
        Bundle b = new Bundle();
        b.putInt(ARG_POSITION, position);
        f.setArguments(b);
        return f;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        position = getArguments().getInt(ARG_POSITION);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_wizard_travel,
                container, false);
        image = (ImageView) rootView
                .findViewById(R.id.fragment_wizard_travel_image);

        if (position == 0) {
            image.setImageResource(R.drawable.seamless_bg_med);

//            ImageUtil.displayImage(image, "http://pengaja.com/uiapptemplate/newphotos/listviews/swipetodissmiss/travel/0.jpg", null);
        } else if (position == 1) {
            image.setImageResource(R.drawable.clock_flat);
//            ImageUtil.displayImage(image, "http://pengaja.com/uiapptemplate/newphotos/listviews/swipetodissmiss/travel/1.jpg", null);
        } else {
            image.setImageResource(R.drawable.healthmainact);
//            ImageUtil.displayImage(image, "http://pengaja.com/uiapptemplate/newphotos/listviews/swipetodissmiss/travel/2.jpg", null);
        }

        ViewCompat.setElevation(rootView, 50);
        return rootView;
    }

}