package com.wpam.noheads.Model;

/**
 * Created by monikas on 30.04.18.
 */

public class User {
    private String name, pushId, userId;

    public User() {
    }

    public User(String name) {
        this.name = name;
    }

    public User(String userId, String name, String pushId) {
        this.userId = userId;
        this.name = name;
        this.pushId = pushId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setUserId(String userId) {this.userId = userId;}

    public String getPushId() {
        return pushId;
    }



    public void setPushId(String pushId) {
        this.pushId = pushId;
    }


}
