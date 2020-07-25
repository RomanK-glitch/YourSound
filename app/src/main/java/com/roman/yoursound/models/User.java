package com.roman.yoursound.models;

import java.security.MessageDigest;

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

    //hash password
    public static String hashPassword (String password){
        String hashPassword = null;
        try {

            MessageDigest md = MessageDigest.getInstance("SHA-512");
            md.update(password.getBytes());
            byte[] bytes = md.digest();

            StringBuilder sb = new StringBuilder();
            for(int i = 0; i < bytes.length; i++){
                sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
            }
            hashPassword = sb.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return hashPassword;
    }
}
