package com.mobitant.bestfood.item;

import java.io.Serializable;

public class OrderCheckItem implements Serializable {

    private  String infoFirstImageFilename;
    private String postNickName; // 구매화면 전환해서 정보를 보여주기위함
    private String postMemberIconFilename;
    private String infoTitle;
    private String infoContent;


    public String getInfoFirstImageFilename() {
        return infoFirstImageFilename;
    }

    public void setInfoFirstImageFilename(String infoFirstImageFilename) {
        this.infoFirstImageFilename = infoFirstImageFilename;
    }

    public String getPostNickName() {
        return postNickName;
    }

    public void setPostNickName(String postNickName) {
        this.postNickName = postNickName;
    }

    public String getPostMemberIconFilename() {
        return postMemberIconFilename;
    }

    public void setPostMemberIconFilename(String postMemberIconFilename) {
        this.postMemberIconFilename = postMemberIconFilename;
    }

    public String getInfoTitle() {
        return infoTitle;
    }

    public void setInfoTitle(String infoTitle) {
        this.infoTitle = infoTitle;
    }

    public String getInfoContent() {
        return infoContent;
    }

    public void setInfoContent(String infoContent) {
        this.infoContent = infoContent;
    }
}
