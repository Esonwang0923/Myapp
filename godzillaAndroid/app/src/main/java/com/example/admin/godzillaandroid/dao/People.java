package com.example.admin.godzillaandroid.dao;

import java.io.Serializable;

/**
 * Created by admin on 2019/4/2.
 */
public class People implements Serializable{
    private String id;
    private String name;
    private String password;
    private String count;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCount() {
        return count;
    }

    public void setCount(String count) {
        this.count = count;
    }
}
