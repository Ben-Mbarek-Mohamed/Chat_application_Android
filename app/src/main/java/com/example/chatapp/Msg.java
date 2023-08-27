package com.example.chatapp;

public class Msg {
    private int curntTime;
    private String msg;
    private String sender;

    public Msg(int curntTime, String msg, String sender) {
        this.curntTime = curntTime;
        this.msg = msg;
        this.sender = sender;
    }

    public int getCurntTime() {
        return curntTime;
    }

    public String getMsg() {
        return msg;
    }

    public String getSender() {
        return sender;
    }
}
