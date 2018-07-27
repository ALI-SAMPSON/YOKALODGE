package com.example.icode.yokalodge.models;

public class Payments {

    //database fields for the database
    private String user_name;
    private String mobile_number;
    private String payments;

    //default constructor
    public Payments(){}

    //constructor with two or more parameters
    public Payments(String user_name, String payments){
        this.user_name = user_name;
        this.payments = payments;
    }

    //Getter and Setter method for Username
    public void setUser_name(String user_name){
        this.user_name = user_name;
    }
    public String getUser_name(){
        return user_name;
    }

    //Getter and Setter method for payments
    public void setPayments(String payments){
        this.payments = payments;
    }
    public String getPayments(){
        return payments;
    }

    //Getter and Setter method for mobile number
    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
    }
    public String getMobile_number() {
        return mobile_number;
    }
}
