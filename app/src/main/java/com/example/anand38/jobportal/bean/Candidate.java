package com.example.anand38.jobportal.bean;

/**
 * Created by anand38 on 7/6/17.
 */

public class Candidate {
    private static String email;
    private  String password;

   
    public static String getEmail() {
        return email;
    }

    public  void setEmail(String email) {
        this.email = email;
    }

    public  String getPassword() {
        return password;
    }

    public  void setPassword(String password) {
        this.password = password;
    }
}
