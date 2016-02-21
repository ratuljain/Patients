package com.example.lol.patients.model;

import android.util.Log;

/**
 * Created by lol on 14/02/16.
 */
public class Medicine {

    public String name;
    public String quantity;

    public Medicine(String name, String quantity) {
        Log.v("MedicineClass", name + quantity);
        this.name = name;
        this.quantity = quantity;
    }

}
