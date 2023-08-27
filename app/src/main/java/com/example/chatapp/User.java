package com.example.chatapp;

public class User {
     String name;
     String pic;
     String userId;
     String currentUserId;

    public User(String name, String pic, String userId, String currentUserId) {
        this.name = name;
        this.pic = pic;
        this.userId = userId;
        this.currentUserId = currentUserId;
    }

    public String getName() {
        return name;
    }

    public String getPic() {
        return pic;
    }

    public String getUserId() {
        return userId;
    }

    public String getCurrentUserId() {
        return currentUserId;
    }
}
