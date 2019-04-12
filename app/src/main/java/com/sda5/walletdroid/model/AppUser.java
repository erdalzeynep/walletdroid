package com.sda5.walletdroid.model;

public class AppUser {
    private int id;
    private String name;
    private String number;
    private String email;
    private int monthlyBudget;
    private int monthlySave;


    public AppUser(String name, String number, String email) {
        this.name = name;
        this.number = number;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public int getMonthlyBudget() {
        return monthlyBudget;
    }

    public void setMonthlyBudget(int monthlyBudget) {
        this.monthlyBudget = monthlyBudget;
    }

    public int getMonthlySave() {
        return monthlySave;
    }

    public void setMonthlySave(int monthlySave) {
        this.monthlySave = monthlySave;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
