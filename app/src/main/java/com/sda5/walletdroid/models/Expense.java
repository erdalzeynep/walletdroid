package com.sda5.walletdroid.models;

import java.util.ArrayList;
import java.util.UUID;

public class Expense {
    private String id;
    private String title;
    private Double amount;
    private Category category;
    private String payerAccountId;
    private String groupId;
    private String date;
    private ArrayList<String> expenseUsersIds;
    private boolean isRecursive;


    public Expense(String title, Double amount, Category category, String payerAccountId, String groupId, String date, ArrayList<String> expenseUsersIds, boolean isRecursive) {
        this.title = title;
        this.amount = amount;
        this.category = category;
        this.payerAccountId = payerAccountId;
        this.groupId = groupId;
        this.date = date;
        this.expenseUsersIds = expenseUsersIds;
        this.isRecursive = isRecursive;
        this.id = UUID.randomUUID().toString();
    }

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

    public String getPayerAccountId() {
        return payerAccountId;
    }

    public void setPayerAccountId(String payerAccountId) {
        this.payerAccountId = payerAccountId;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public ArrayList<String> getExpenseUsersIds() {
        return expenseUsersIds;
    }

    public void setExpenseUsersIds(ArrayList<String> expenseUsersIds) {
        this.expenseUsersIds = expenseUsersIds;
    }

    public boolean isRecursive() {
        return isRecursive;
    }

    public void setRecursive(boolean recursive) {
        isRecursive = recursive;
    }
}
