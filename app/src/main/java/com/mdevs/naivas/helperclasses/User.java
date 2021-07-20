package com.mdevs.naivas.helperclasses;

public class User {
    private int id;
    private String firstname;
    private String lastname;
    private String gender;
    private String email;
    private String phonenumber;
    private String usertype;

    public User(int id, String firstname, String lastname, String gender, String email, String phonenumber, String usertype) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.gender = gender;
        this.email = email;
        this.phonenumber = phonenumber;
        this.usertype = usertype;
    }

    public User(String firstname, String lastname, String gender/*, String email, String phonenumber*/) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.gender = gender;
        /*this.email = email;
        this.phonenumber = phonenumber;*/
    }

    public User() {
    }

    public User(String firstname, String lastname, String email, String phonenumber) {
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.phonenumber = phonenumber;
    }

    public int getId() {
        return id;
    }

    public String getFirstname() {
        return firstname;
    }

    public String getLastname() {
        return lastname;
    }

    public String getGender() {
        return gender;
    }


    public String getEmail() {
        return email;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public String getUsertype() {
        return usertype;
    }
}
