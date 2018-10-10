package com.mobitant.bestfood.item;

import java.io.Serializable;

public class OrderCheckItem implements Serializable {

    private  String infoFirstImageFilename;
    private String postNickName; // 구매화면 전환해서 정보를 보여주기위함
    private String postMemberIconFilename;
    private String infoTitle;
    private String infoContent;
private String postRegisterDate;
private String postPhoneNumber;
private String postRealName;

    @Override
    public String toString() {
        return "OrderCheckItem{" +
                "infoFirstImageFilename='" + infoFirstImageFilename + '\'' +
                ", postNickName='" + postNickName + '\'' +
                ", postMemberIconFilename='" + postMemberIconFilename + '\'' +
                ", infoTitle='" + infoTitle + '\'' +
                ", infoContent='" + infoContent + '\'' +
                ", postRegisterDate='" + postRegisterDate + '\'' +
                ", postPhoneNumber='" + postPhoneNumber + '\'' +
                ", postRealName='" + postRealName + '\'' +
                '}';
    }

    public String getPostRealName() {
        return postRealName;
    }

    public void setPostRealName(String postRealName) {
        this.postRealName = postRealName;
    }

    public String getPostPhoneNumber() {
        return postPhoneNumber;
    }

    public void setPostPhoneNumber(String postPhoneNumber) {
        this.postPhoneNumber = postPhoneNumber;
    }

    public String getPostRegisterDate() {
        return postRegisterDate;
    }

    public void setPostRegisterDate(String postRegisterDate) {
        this.postRegisterDate = postRegisterDate;
    }

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
