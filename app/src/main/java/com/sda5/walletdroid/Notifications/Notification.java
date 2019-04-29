package com.sda5.walletdroid.Notifications;

public class Notification {


    String From="",Message="", Group="";

    public Notification(){

    }

    public Notification(String From, String Message, String Group) {
        this.From = From;
        this.Message = Message;
        this.Group = Group;
    }

    public String getFrom() {
        return From;
    }

    public void setFrom(String from) {
        this.From = from;
    }

    public String getMessage() {
        return Message;
    }

    public void setMessage(String message) {
        this.Message = message;
    }

    public String getGroup() {
        return Group;
    }

    public void setGroup(String group) {
        this.Group = group;
    }

}
