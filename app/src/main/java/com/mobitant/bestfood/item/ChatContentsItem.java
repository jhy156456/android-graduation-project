package com.mobitant.bestfood.item;

import com.mobitant.bestfood.MyApp;

import java.util.Date;

public class ChatContentsItem {
    //type은.. 1이면 보낸것 2이면 받는것으로해보쟝
    public static final int TYPE_MESSAGE = 0;
    public static final int TYPE_RECEIVE = 1;
    public static final int TYPE_ACTION = 2;

    private String room;
    private String user;
    private String chat;
    private String sender;
    private String receiver;
    private Date createdAt;
    private int type;

    public ChatContentsItem() {
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getReceiver() {
        return receiver;
    }

    public void setReceiver(String receiver) {
        this.receiver = receiver;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

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
    public static class Builder {
        private final int mType;
        private String mSender;
        private String mReceiver;
        private String mMessage;

        public Builder(int type) {
            mType = type;
        }

        public Builder sender(String username) {
            mSender = username;
            return this;
        }
        public Builder receiver(String username) {
            mReceiver = username;
            return this;
        }

        public Builder message(String message) {
            mMessage = message;
            return this;
        }

        public ChatContentsItem build() {
            ChatContentsItem message = new ChatContentsItem();
            message.type = mType;
            message.sender = mSender;
            message.receiver = mReceiver;
            message.chat = mMessage;
            return message;
        }
    }
}
