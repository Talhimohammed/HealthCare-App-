package com.example.healthcare.Model;

public class fiche {

    private String poid ;
    private String operation ;
    private String groupe_san ;
    private String maladie ;
    private String taille ;
    private String postedby ;

    public fiche(){

    }

    public fiche(String poid, String operation, String groupe_san, String maladie, String taille, String postedby) {
        this.poid = poid;
        this.operation = operation;
        this.groupe_san = groupe_san;
        this.maladie = maladie;
        this.taille = taille;
        this.postedby = postedby;
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
}
