package com.example.icode.yokalodge.models;

public class CurrentAdmin {

    //class variables to be used as fields for the database
    private String current_user_name;
    private String current_password;

    //default constructor
    public CurrentAdmin(){
    }

    //constructor with two or more parameters
    public CurrentAdmin(String current_user_name, String current_password){
        this.current_user_name = current_user_name;
        this.current_password = current_password;
    }

    //Getter and Setter method for Username
    public void setCurrent_user_name(String current_user_name){
        this.current_user_name = current_user_name;
    }
    public String getCurrent_user_name(){
        return current_user_name;
    }

    //Getter and Setter method for Password
    public void setCurrent_password(String current_password){
        this.current_password = current_password;
    }
    public String getCurrent_password(){
        return current_password;
    }

}
