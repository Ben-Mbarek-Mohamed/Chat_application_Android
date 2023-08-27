package com.example.chatapp;

public class ChatList {
    private String senderId,user1,user2,message,date,time;

    public ChatList(String senderId, String user1, String user2, String message, String date, String time) {
        this.senderId = senderId;
        this.user1 = user1;
        this.user2 = user2;
        this.message = message;
        this.date = date;
        this.time = time;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getUser1() {
        return user1;
    }

    public String getUser2() {
        return user2;
    }

    public String getMessage() {
        return message;
    }

    public String getDate() {
        return date;
    }

    public String getTime() {
        return time;
    }
}
