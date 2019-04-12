package com.sda5.walletdroid.model;

public class Category {
    private int id;
    private String title;
    private int maxBudget;


    public Category(String title, int maxBudjet) {
        this.title = title;
        this.maxBudget = maxBudjet;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public int getMaxBudget() {
        return maxBudget;
    }

    public void setMaxBudget(int maxBudget) {
        this.maxBudget = maxBudget;
    }
}
