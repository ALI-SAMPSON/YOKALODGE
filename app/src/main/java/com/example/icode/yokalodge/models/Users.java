package com.example.icode.yokalodge.models;

public class Users {

    //class variables to be used as fields for the database
    private String email_address;
    private String password;
    private String gender;
    private String mobile_number;
   // private String payment_method;
    private String confirm_password;

    //default constructor
    public Users(){
    }

    //constructor with two or more parameters
    public Users(String email_address, String password, String gender, String mobile_number,String confirm_password){
        this.email_address = email_address;
        this.password = password;
        this.gender = gender;
        this.mobile_number = mobile_number;
        this.confirm_password = confirm_password;
    }

    //Getter and Setter method for Email Address
    public void setEmail_address(String email_address){
        this.email_address = email_address;
    }

    public String getEmail_address(){
        return email_address;
    }

    //Getter and Setter method for Password
    public void setPassword(String password){
        this.password = password;
    }
    public String getPassword(){
        return password;
    }

    //Getter and Setter method for Gender
    public void setGender(String gender){
        this.gender = gender;
    }
    public String getGender(){
        return gender;
    }

    //Getter and Setter method for Mobile Number
    public void setMobile_number(String mobile_number){
        this.mobile_number = mobile_number;
    }

    public String getMobile_number(){
        return mobile_number;
    }

    //Getter and Setter method for Confirm Password
    public void setConfirm_password(String confirm_password){
        this.confirm_password = confirm_password;
    }
    public String getConfirm_password(){
        return confirm_password;
    }

}
