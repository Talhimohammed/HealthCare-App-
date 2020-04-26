package com.example.healthcare.Model;



public class patient {

     private String email ;
     private String fullname ;
     private String type ;
     private int phone ;
     private String password ;
     private String birthdaydate ;

    public patient(String email, String fullname, String type, int phone, String password, String birthdaydate) {
        this.email = email;
        this.fullname = fullname;
        this.type = type;
        this.phone = phone;
        this.password = password;
        this.birthdaydate = birthdaydate;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getPhone() {
        return phone;
    }

    public void setPhone(int phone) {
        this.phone = phone;
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


}
