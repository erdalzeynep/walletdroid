package io.sudutech.walletdroid.data;

import android.icu.util.LocaleData;

import java.util.ArrayList;

public class Expense {
    private int id;
    private String title;
    private int amount;
    private Category category;
    private AppUser buyer;
    private ArrayList<AppUser> expenseUsers;
    private LocaleData date;
    private boolean type;


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

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public AppUser getBuyer() {
        return buyer;
    }

    public void setBuyer(AppUser buyer) {
        this.buyer = buyer;
    }

    public ArrayList<AppUser> getExpenseUsers() {
        return expenseUsers;
    }

    public void setExpenseUsers(ArrayList<AppUser> expenseUsers) {
        this.expenseUsers = expenseUsers;
    }

    public LocaleData getDate() {
        return date;
    }

    public void setDate(LocaleData date) {
        this.date = date;
    }

    public boolean isRecursive() {
        return type;
    }

    public void setRecursive(boolean recursive) {
        this.type = recursive;
    }
}
