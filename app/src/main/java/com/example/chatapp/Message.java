package com.example.chatapp;

public class Message {
    private String message;
    private String senderId;
    private String receuverId;
    private Long messageTime;
    private String currentUser;

    public Message(String message, String senderId, String receuverId,Long messageTime,String currentUser) {
        this.message = message;
        this.senderId = senderId;
        this.receuverId = receuverId;
        this.messageTime = messageTime;
        this.currentUser=currentUser;
    }
    public Message(){}

    public void setCurrentUser(String currentUser) {
        this.currentUser = currentUser;
    }

    public String getCurrentUser() {
        return currentUser;
    }

    public String getMessage() {
        return message;
    }

    public String getSenderId() {
        return senderId;
    }

    public String getReceuverId() {
        return receuverId;
    }

    public Long getMessageTime() {
        return messageTime;
    }
}
