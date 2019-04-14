package com.sda5.walletdroid.models;

import java.util.List;

public class Group {

    private String name;
    private List<String> accountIdList;

    public Group(String name, List<String> list) {
        this.name = name;
        this.accountIdList = list;
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
}
