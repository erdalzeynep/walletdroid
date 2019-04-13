package com.sda5.walletdroid.models;

import android.icu.util.LocaleData;

public class Expense {
    private String id;
    private String title;
    private Double amount;
    private Category category;
    private String payerAccountId;
    private String groupId;
    private LocaleData date;
    private boolean isRecursive;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Double getAmount() {
        return amount;
    }

    public void setAmount(Double amount) {
        this.amount = amount;
    }

    public Category getCategory() {
        return category;
    }

    public void setCategory(Category category) {
        this.category = category;
    }

    public String getpayerAccountId() {
        return payerAccountId;
    }

    public void setpayerAccountId(String payerAccountId) {
        this.payerAccountId = payerAccountId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public LocaleData getDate() {
        return date;
    }

    public void setDate(LocaleData date) {
        this.date = date;
    }

    public boolean isRecursive() {
        return isRecursive;
    }

    public void setRecursive(boolean recursive) {
        this.isRecursive = recursive;
    }
}
