package com.roman.yoursound.models;

import java.util.Date;

public class Track {

    public String name;
    public String image_path;
    String date;
    public int listening;
    public String path;
    public int id;
    public int likes;
    public String author;
    public String duration;

    public Track(int id, String name, String path, String image_path, String date, int listening, String author, String duration){
        this.id = id;
        this.name = name;
        this.path = path;
        this.image_path = image_path;
        this.date = date;
        this.listening = listening;
        this.author = author;
        this.author = author;
        this.duration = duration;
    }
}
