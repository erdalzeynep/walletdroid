package com.sda5.walletdroid.model;

import java.util.UUID;

public class User {
    private String userID;
    private String email;
    private String displayName;
    private String accountID;
    private String token;

    public User(String userID, String email, String displayName) {
        this.userID = userID;
        this.email = email;
        this.displayName = displayName;
        this.accountID = UUID.randomUUID().toString();
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getAccountID() {
        return accountID;
    }

    public void setAccountID(String accountID) {
        this.accountID = accountID;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}