package com.example.admin.godzillaandroid.dao;

/**
 * Created by admin on 2019/4/3.
 */
public class Task {
    private String id;
    private String name;
    private String date;
    private int imageId;
    public Task( String idd,String name,String date, int imageId) {
        this.id = idd;
        this.name = name;
        this.imageId = imageId;
        this.date = date;
    }
    public String getName() {
        return name;
    }

    public int getImageId() {
        return imageId;
    }

    public String getId() {
        return id;
    }

    public String getDate(){
        return date;
    }

}
