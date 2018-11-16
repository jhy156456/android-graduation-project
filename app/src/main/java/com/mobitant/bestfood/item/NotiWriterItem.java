package com.mobitant.bestfood.item;

import com.google.gson.annotations.SerializedName;

public class NotiWriterItem {
    @SerializedName("_id") private String writerId;
    private String email;
    private String name;
    private String nickname;


    public String getWriterId() {
        return writerId;
    }

    public void setWriterId(String writerId) {
        this.writerId = writerId;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
