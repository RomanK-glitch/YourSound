package com.roman.yoursound.models;

import java.util.Date;

public class Track {
    public String name;
    public String image_path;
    public int durationMinutes;
    public int durationSeconds;
    String date;
    public int listening;
    public String path;
    public int id;
    public int likes;
    public String author;
    public Track(String name, String path, String image_path, String date, int listening, String author){
        this.name = name;
        this.path = path;
        this.image_path = image_path;
        this.date = date;
        this.listening = listening;
        this.author = author;
        this.author = author;
    }
}
