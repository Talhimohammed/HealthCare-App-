package com.example.healthcare.Model;

public class fiche {

    private String poid ;
    private String operation ;
    private String groupe_san ;
    private String maladie ;
    private String taille ;
    private String postedby ;
    private String patient_email;

    public fiche(){ }

    public fiche(String poid, String operation, String groupe_san, String maladie, String taille, String postedby ,String patient_email) {
        this.poid = poid;
        this.operation = operation;
        this.groupe_san = groupe_san;
        this.maladie = maladie;
        this.taille = taille;
        this.postedby = postedby;
        this.setPatient_email(patient_email);
    }

    public void setPoid(String poid) {
        this.poid = poid;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public void setGroupe_san(String groupe_san) {
        this.groupe_san = groupe_san;
    }

    public void setMaladie(String maladie) {
        this.maladie = maladie;
    }

    public void setTaille(String taille) {
        this.taille = taille;
    }

    public void setPostedby(String postedby) {
        this.postedby = postedby;
    }

    public void setPatient_email(String patient_email) { this.patient_email = patient_email;}



    public String getPoid() {
        return poid;
    }

    public String getOperation() {
        return operation;
    }

    public String getGroupe_san() {
        return groupe_san;
    }

    public String getMaladie() {
        return maladie;
    }

    public String getTaille() {
        return taille;
    }

    public String getPostedby() {
        return postedby;
    }

    public String getPatient_email() { return patient_email; }


}
