package com.sda5.walletdroid.models;

import com.google.firebase.auth.FirebaseAuth;

import java.util.List;
import java.util.UUID;

public class Group {

    private FirebaseAuth mAuth;
    private String adminUserId;
    private String id;
    private String name;
    private List<String> accountIdList;
    private String adminAccountId;

    public Group(){}

    public Group(String name, List<String> list , String adminAccountId) {
        mAuth = FirebaseAuth.getInstance();
        this.name = name;
        this.accountIdList = list;
        this.adminUserId = mAuth.getCurrentUser().getUid();
        this.id = UUID.randomUUID().toString();
        this.adminAccountId = adminAccountId;
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

    public String getAdminUserId() {
        return adminUserId;
    }

    public String getAdminAccountId() {
        return adminAccountId;
    }

    public void setAdminAccountId(String adminAccountId) {
        this.adminAccountId = adminAccountId;
    }

    @Override
    public String toString() {
        return name;
    }
}
