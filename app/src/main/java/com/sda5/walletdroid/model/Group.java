package com.sda5.walletdroid.model;

import java.util.ArrayList;

public class Group {
    private int id;
    private String name;
    private ArrayList<Account> list;

    public Group(int id, String name, ArrayList<Account> list) {
        this.id = id;
        this.name = name;
        this.list = list;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public ArrayList<Account> getList() {
        return list;
    }

    public void setList(ArrayList<Account> list) {
        this.list = list;
    }
}
