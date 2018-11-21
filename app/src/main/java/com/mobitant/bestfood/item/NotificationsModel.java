package com.mobitant.bestfood.item;

/**
 * Created by wolfsoft3 on 17/7/18.
 */

public class NotificationsModel {
    Integer foodimg1;
    String  foodtext1,foodtext2,foodtext3;
    String title;
    String contents;
    String created_at;


    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public Integer getFoodimg1() {
        return foodimg1;
    }

    public void setFoodimg1(Integer foodimg1) {
        this.foodimg1 = foodimg1;
    }

    public String getFoodtext1() {
        return foodtext1;
    }

    public void setFoodtext1(String foodtext1) {
        this.foodtext1 = foodtext1;
    }

    public String getFoodtext2() {
        return foodtext2;
    }

    public void setFoodtext2(String foodtext2) {
        this.foodtext2 = foodtext2;
    }

    public String getFoodtext3() {
        return foodtext3;
    }

    public void setFoodtext3(String foodtext3) {
        this.foodtext3 = foodtext3;
    }

    public NotificationsModel(Integer foodimg1, String foodtext1, String foodtext2, String foodtext3) {
        this.foodimg1 = foodimg1;
        this.foodtext1 = foodtext1;
        this.foodtext2 = foodtext2;
        this.foodtext3 = foodtext3;
    }
}
