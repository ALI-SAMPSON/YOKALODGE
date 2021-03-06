package com.example.icode.yokalodge.models;

public class Payments {

    //database fields for the database
    private String user_name;
    private String room_number;
    private String price;
    private String payment_method;
    private String mobile_number;


    //default constructor
    public Payments(){}

    //constructor with two or more parameters
    public Payments(String user_name, String room_number,String price,String mobile_number){
        this.user_name = user_name;
        this.room_number = room_number;
        this.price = price;
        this.mobile_number = mobile_number;
    }

    //Getter and Setter method for Username
    public void setUser_name(String user_name){
        this.user_name = user_name;
    }
    public String getUser_name() {
        return user_name;
    }

    //Getter and Setter method for Room number
    public void setRoom_number(String room_number)
    {
        this.room_number = room_number;
    }
    public String getRoom_number(){
        return room_number;
    }

    //Getter and Setter method for price
    public void setPrice(String price){
        this.price = price;
    }
    public String getPrice(){
        return price;
    }

    //Getter and Setter method for Payment method
    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }
    public String getPayment_method() {
        return payment_method;
    }

    //Getter and Setter method for mobile number
    public void setMobile_number(String mobile_number) {
        this.mobile_number = mobile_number;
    }
    public String getMobile_number() {
        return mobile_number;
    }
}
