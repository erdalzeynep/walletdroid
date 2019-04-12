package io.sudutech.walletdroid.data;

import com.google.firebase.database.Exclude;

public class Category {
    private int id;
    private String title;
    private int maxBudjet;


    public Category(String title, int maxBudjet) {
        this.title = title;
        this.maxBudjet = maxBudjet;
    }

    @Exclude
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

    public int getMaxBudjet() {
        return maxBudjet;
    }

    public void setMaxBudjet(int maxBudjet) {
        this.maxBudjet = maxBudjet;
    }
}
