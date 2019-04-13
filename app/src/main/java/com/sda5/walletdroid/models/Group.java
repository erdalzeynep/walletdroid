package com.sda5.walletdroid.models;

import java.util.ArrayList;

public class Group {
    private String id;
    private String name;
    private ArrayList<String> accountIdList;

    public Group(String id, String name, ArrayList<String> list) {
        this.id = id;
        this.name = name;
        this.accountIdList = list;
    }

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

    public ArrayList<String> getList() {
        return accountIdList;
    }

    public void setList(ArrayList<String> list) {
        this.accountIdList = list;
    }
}
