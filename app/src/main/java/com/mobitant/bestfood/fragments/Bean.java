package com.mobitant.bestfood.fragments;

/**
 * Created by apple on 15/03/16.
 */
public class Bean {

    public String imageFileName;
    private int image;
    private String title;
    public String nickName;
    public String realName;
    public String memberIconFileName;
    public String phoneNumber;
    public String cardNumber;
    public String cardHolder;


    private String discription;
    private String date;

    public Bean(String imageFileName, String title, String discription, String date) {
        this.imageFileName = imageFileName;
        this.title = title;
        this.discription = discription;
        this.date = date;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDiscription() {
        return discription;
    }

    public void setDiscription(String discription) {
        this.discription = discription;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
