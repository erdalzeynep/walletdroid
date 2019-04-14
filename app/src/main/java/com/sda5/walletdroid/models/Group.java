package com.sda5.walletdroid.models;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;
import java.util.UUID;

public class Group {
    private FirebaseAuth mAuth;
    private String userID;
    private String id;
    private String name;
    private List<String> accountIdList;

    public Group(){}

    public Group(String name, List<String> list) {
        mAuth = FirebaseAuth.getInstance();
        this.name = name;
        this.accountIdList = list;
        this.id = UUID.randomUUID().toString();
        this.userID = mAuth.getCurrentUser().getUid();
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<String> getAccountIdList() {
        return accountIdList;
    }

    public void setAccountIdList(List<String> accountIdList) {
        this.accountIdList = accountIdList;
    }

    public String getUserID() {
        return userID;
    }
}
