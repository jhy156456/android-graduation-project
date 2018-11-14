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
    private boolean participant_is_exit;
    private boolean owner_is_exit;
    private String nowAdapterRoomNickName;
    private String last_chat_contents;
    @SerializedName("participant_member_icon_file_name")
    private String participantMemberIconFileName;
    @SerializedName("owner_member_icon_file_name")
    private String ownerMemberIconFileName;
private String whoAmI;

    public String getWhoAmI() {
        return whoAmI;
    }

    public void setWhoAmI(String whoAmI) {
        this.whoAmI = whoAmI;
    }

    public String getParticipantMemberIconFileName() {
        return participantMemberIconFileName;
    }

    public void setParticipantMemberIconFileName(String participantMemberIconFileName) {
        this.participantMemberIconFileName = participantMemberIconFileName;
    }

    public String getOwnerMemberIconFileName() {
        return ownerMemberIconFileName;
    }

    public void setOwnerMemberIconFileName(String ownerMemberIconFileName) {
        this.ownerMemberIconFileName = ownerMemberIconFileName;
    }

    public String getLast_chat_contents() {
        return last_chat_contents;
    }

    public void setLast_chat_contents(String last_chat_contents) {
        this.last_chat_contents = last_chat_contents;
    }

    public String getNowAdapterRoomNickName() {
        return nowAdapterRoomNickName;
    }

    public void setNowAdapterRoomNickName(String nowAdapterRoomNickName) {
        this.nowAdapterRoomNickName = nowAdapterRoomNickName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public boolean isParticipant_is_exit() {
        return participant_is_exit;
    }

    public void setParticipant_is_exit(boolean participant_is_exit) {
        this.participant_is_exit = participant_is_exit;
    }

    public boolean isOwner_is_exit() {
        return owner_is_exit;
    }

    public void setOwner_is_exit(boolean owner_is_exit) {
        this.owner_is_exit = owner_is_exit;
    }


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
