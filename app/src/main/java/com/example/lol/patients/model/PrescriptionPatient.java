package com.example.lol.patients.model;

/**
 * Created by lol on 14/02/16.
 */

import java.util.Date;


public class PrescriptionPatient {

    public String docname;
    public Date date;
    public String diagnosis;
    public String mapJson;

    public PrescriptionPatient(String name, Date date, String diagnosis, String mapJson) {
        this.docname = name;
        this.date = date;
        this.diagnosis = diagnosis;
        this.mapJson = mapJson;
    }

}
