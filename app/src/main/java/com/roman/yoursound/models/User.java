package com.roman.yoursound.models;

public class User {
    public int id;
    public String userName;
    public String password;
    public String eMail;
    public String about;
    public String imagePath;

    public User(int id, String userName, String imagePath){
        this.id = id;
        this.userName = userName;
        this.imagePath = imagePath;
    }

    public User(String userName, String password, String eMail){
        this.userName = userName;
        this.password = password;
        this.eMail = eMail;
        this.imagePath = "";
        this.about = "";
    }

    public User(String userName, String password, String eMail, String about, String imagePath){
        this.userName = userName;
        this.password = password;
        this.eMail = eMail;
        this.imagePath = imagePath;
        this.about = about;
    }

    public User(int id, String userName, String password, String eMail, String about, String imagePath){
        this.id = id;
        this.userName = userName;
        this.password = password;
        this.eMail = eMail;
        this.imagePath = imagePath;
        this.about = about;
    }
}
