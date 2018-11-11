package com.mobitant.bestfood.item;


import com.google.gson.annotations.SerializedName;

public class User {
    //응답받는 json객체값이 순서대로 저장되는건지??
    public String name;
    private String email;
    private String password;
    private String created_at;
    private String newPassword;
    private String token;
    @SerializedName("user_type")
    private String userType;
    @SerializedName("_id")
    public String id;
    @SerializedName("nickname")
    public String nickname;
    public int seq;
    public String phone;
    public String sextype;
    public String birthday;
    @SerializedName("member_icon_filename")
    public String memberIconFilename;
    @SerializedName("reg_date")
    public String regDate;
    private String chatParticipantOrOwner;


    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getChatParticipantOrOwner() {
        return chatParticipantOrOwner;
    }

    public void setChatParticipantOrOwner(String chatParticipantOrOwner) {
        this.chatParticipantOrOwner = chatParticipantOrOwner;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", created_at='" + created_at + '\'' +
                ", newPassword='" + newPassword + '\'' +
                ", token='" + token + '\'' +
                ", id='" + id + '\'' +
                ", nickname='" + nickname + '\'' +
                ", seq=" + seq +
                ", phone='" + phone + '\'' +
                ", sextype='" + sextype + '\'' +
                ", birthday='" + birthday + '\'' +
                ", memberIconFilename='" + memberIconFilename + '\'' +
                ", regDate='" + regDate + '\'' +
                '}';
    }

    public void setSextype(String sex) {
        this.sextype = sex;
    }

    public void setBirthday(String birth) {
        this.birthday = birth;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public void setNickName(String nickname) {
        this.nickname = nickname;
    }
}
