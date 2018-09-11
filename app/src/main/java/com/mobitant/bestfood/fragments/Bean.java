package com.mobitant.bestfood.fragments;

/**
 * Created by apple on 15/03/16.
 */
public class Bean {

    private int image;
    private String title;
    private String discription;
    private String date;

    public Bean(int image, String title, String discription, String date) {
        this.image = image;
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
