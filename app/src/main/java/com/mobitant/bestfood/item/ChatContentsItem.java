package com.mobitant.bestfood.item;

import java.util.Date;

public class ChatContentsItem {
    private String room;
    private String user;
    private String chat;
    private Date createdAt;


    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getChat() {
        return chat;
    }

    public void setChat(String chat) {
        this.chat = chat;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "ChatContentsItem{" +
                "room='" + room + '\'' +
                ", user='" + user + '\'' +
                ", chat='" + chat + '\'' +
                ", createdAt=" + createdAt +
                '}';
    }
}
