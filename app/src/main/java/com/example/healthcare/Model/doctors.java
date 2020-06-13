package com.example.healthcare.Model;

public class doctors {
    private String email ;
    private String fullname ;
    private int phone ;
    private String password ;
    private String birthdaydate ;
    private String Address;
    private String Specialite ;

    public doctors(String email, String fullname, int phone, String password, String birthdaydate, String Address, String specialite) {
        this.email = email;
        this.fullname = fullname;
        this.phone = phone;
        this.password = password;
        this.birthdaydate = birthdaydate;
        this.Address = Address;
        Specialite = specialite;
    }
    public doctors(){
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }


    public void setPhone(int phone) {
        this.phone = phone;
    }

    public int getPhone (){
        return phone ;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBirthdaydate() {
        return birthdaydate;
    }

    public void setBirthdaydate(String birthdaydate) {
        this.birthdaydate = birthdaydate;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        this.Address = address;
    }

    public String getSpecialite() {
        return Specialite;
    }

    public void setSpecialite(String specialite) {
        Specialite = specialite;
    }




}
