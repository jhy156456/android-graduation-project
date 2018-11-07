package com.mobitant.bestfood.item;

import com.google.gson.annotations.SerializedName;

import java.util.Date;

/**
 * Created by wolfsoft1 on 3/3/17.
 */
@org.parceler.Parcel
public class ChatTalkData {

    @SerializedName("_id")
    public String id;
    private String title;

    private String owner;
    private String participant;
    private Date createAt;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getParticipant() {
        return participant;
    }

    public void setParticipant(String participant) {
        this.participant = participant;
    }

    public Date getCreateAt() {
        return createAt;
    }

    public void setCreateAt(Date createAt) {
        this.createAt = createAt;
    }
}
