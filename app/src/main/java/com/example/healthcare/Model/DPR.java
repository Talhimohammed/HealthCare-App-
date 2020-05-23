package com.example.healthcare.Model;

public class DPR {

     //DPR : DOCTOR PATIENT RELATIONSHIP

    public String email_doctor  ;
    public String email_patient ;
    public String date_creation ;
    public String etat_DPR      ;

    public DPR(){ }

    public DPR(String email_doctor, String email_patient, String date_creation, String etat_DPR) {
        this.email_doctor = email_doctor;
        this.email_patient = email_patient;
        this.date_creation = date_creation;
        this.etat_DPR = etat_DPR;
    }

    public void setEmail_doctor(String email_doctor) {
        this.email_doctor = email_doctor;
    }

    public void setEmail_patient(String email_patient) {
        this.email_patient = email_patient;
    }

    public void setDate_creation(String date_creation) {
        this.date_creation = date_creation;
    }

    public void setEtat_DPR(String etat_DPR) {
        this.etat_DPR = etat_DPR;
    }

    public String getEmail_doctor() {
        return email_doctor;
    }

    public String getEmail_patient() {
        return email_patient;
    }

    public String getDate_creation() {
        return date_creation;
    }

    public String getEtat_DPR() {
        return etat_DPR;
    }
}
