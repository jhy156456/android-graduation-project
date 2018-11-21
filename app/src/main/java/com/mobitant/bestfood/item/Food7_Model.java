package com.mobitant.bestfood.item;

public class Food7_Model {
 String Restaurant_Id,Address_Id,Reviews_Id,Date_Id,Dollar_Id;
    Integer FoodImage_Id,History_Id;



    public String getRestaurant_Id() {
        return Restaurant_Id;
    }

    public void setRestaurant_Id(String restaurant_Id) {
        Restaurant_Id = restaurant_Id;
    }

    public String getAddress_Id() {
        return Address_Id;
    }

    public void setAddress_Id(String address_Id) {
        Address_Id = address_Id;
    }

    public String getReviews_Id() {
        return Reviews_Id;
    }

    public void setReviews_Id(String reviews_Id) {
        Reviews_Id = reviews_Id;
    }

    public String getDate_Id() {
        return Date_Id;
    }

    public void setDate_Id(String date_Id) {
        Date_Id = date_Id;
    }

    public String getDollar_Id() {
        return Dollar_Id;
    }

    public void setDollar_Id(String dollar_Id) {
        Dollar_Id = dollar_Id;
    }

    public Integer getFoodImage_Id() {
        return FoodImage_Id;
    }

    public void setFoodImage_Id(Integer foodImage_Id) {
        FoodImage_Id = foodImage_Id;
    }



    public Integer getHistory_Id() {
        return History_Id;
    }

    public void setHistory_Id(Integer history_Id) {
        History_Id = history_Id;
    }

    public Food7_Model(String restaurant_Id, String address_Id, String reviews_Id, String date_Id, String dollar_Id, Integer foodImage_Id, Integer history_Id) {
        Restaurant_Id = restaurant_Id;
        Address_Id = address_Id;
        Reviews_Id = reviews_Id;
        Date_Id = date_Id;
        Dollar_Id = dollar_Id;
        FoodImage_Id = foodImage_Id;

        History_Id = history_Id;
    }


}
