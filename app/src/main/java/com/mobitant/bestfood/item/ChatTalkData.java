package com.mobitant.bestfood.item;

/**
 * Created by wolfsoft1 on 3/3/17.
 */

public class ChatTalkData {


    private String name;

    private String description;

    public ChatTalkData(String name, String description) {
        this.name = name;
        this.description = description;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
