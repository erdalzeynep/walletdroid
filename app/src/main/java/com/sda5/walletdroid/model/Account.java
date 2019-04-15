package com.sda5.walletdroid.model;

public class Account {
    private boolean isInternalAccount;
    private String ownerName;
    private String email;
    private String phoneNumber;
    private Double monthlyBudget;
    private Double monthlySave;
    private String tokenID;

    public Account(boolean isInternalAccount, String ownerName, String email,
                   String phoneNumber, Double monthlyBudget, Double monthlySave) {
        this.isInternalAccount = isInternalAccount;
        this.ownerName = ownerName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.monthlyBudget = monthlyBudget;
        this.monthlySave = monthlySave;
    }

    public Account(boolean isInternalAccount, String ownerName, String email) {
        this.isInternalAccount = isInternalAccount;
        this.ownerName = ownerName;
        this.email = email;
    }

    public Account( boolean isInternalAccount, String ownerName, String email, String tokenID) {
        this.isInternalAccount = isInternalAccount;
        this.ownerName = ownerName;
        this.email = email;
        this.tokenID = tokenID;
    }

    public String getTokenID() {
        return tokenID;
    }

    public void setTokenID(String tokenID) {
        this.tokenID = tokenID;
    }

    public Account(){}

    public String getOwnerName() {
        return ownerName;
    }

    public void setOwnerName(String ownerName) {
        this.ownerName = ownerName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Double getMonthlyBudget() {
        return monthlyBudget;
    }

    public void setMonthlyBudget(Double monthlyBudget) {
        this.monthlyBudget = monthlyBudget;
    }

    public Double getMonthlySave() {
        return monthlySave;
    }

    public void setMonthlySave(Double monthlySave) {
        this.monthlySave = monthlySave;
    }

    public boolean isInternalAccount() {
        return isInternalAccount;
    }

    public void setInternalAccount(boolean internalAccount) {
        isInternalAccount = internalAccount;
    }
}