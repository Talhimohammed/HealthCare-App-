package com.example.healthcare.Model;

public class appointement {
     private int app_code ;
     private String doctor_ref ;
     private String hour     ;
     private String date     ;
     private String postedby ;

    public void setApp_code(int app_code) {
        this.app_code = app_code;
    }

    public void setDoctor_ref(String doctor_ref) {
        this.doctor_ref = doctor_ref;
    }

    public void setHour(String hour) {
        this.hour = hour;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public void setPostedby(String postedby) {
        this.postedby = postedby;
    }

    public appointement(int app_code, String doctor_ref, String hour, String date, String postedby) {
        this.app_code = app_code;
        this.doctor_ref = doctor_ref;
        this.hour = hour;
        this.date = date;
        this.postedby = postedby;
    }

    public int getApp_code() {
        return app_code;
    }

    public String getDoctor_ref() {
        return doctor_ref;
    }

    public String getHour() {
        return hour;
    }

    public String getDate() {
        return date;
    }

    public String getPostedby() {
        return postedby;
    }
}
