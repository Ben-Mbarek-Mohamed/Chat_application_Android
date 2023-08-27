package com.example.chatapp;

public class Ch {
    private String usr1;
    private String usr2;
    private Msg msg;

    public Ch( String usr1, String usr2, Msg msg) {

        this.usr1 = usr1;
        this.usr2 = usr2;
        this.msg = msg;
    }


    public String getUsr1() {
        return usr1;
    }

    public String getUsr2() {
        return usr2;
    }

    public Msg getMsg() {
        return msg;
    }
}
