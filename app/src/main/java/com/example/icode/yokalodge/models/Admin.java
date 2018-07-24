package com.example.icode.yokalodge.models;

public class Admin {

    //class variables to be used as fields for the database
    private String user_name;
    private String password;

    //default constructor
    public Admin(){
    }

    //constructor with two or more parameters
    public Admin(String user_name, String passsword){
        this.user_name = user_name;
        this.password = passsword;
    }

    //Getter and Setter method for Username
    public void setUser_name(String user_name){
        this.user_name = user_name;
    }
    public String getUsername(){
        return user_name;
    }

    //Getter and Setter method for Password
    public void setPassword(String password){
        this.password = password;
    }
    public String getPassword(){
        return password;
    }

}
