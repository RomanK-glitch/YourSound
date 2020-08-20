package com.roman.yoursound.models;

public class Comment {
    public String text;
    public String userImagePath;
    public int userId;

    public Comment(String text, String userImagePath, int userId){
        this.text = text;
        this.userImagePath = userImagePath;
        this.userId = userId;
    }
}
