package com.mobitant.bestfood.item;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by wolfsoft5 on 7/5/18.
 */

public class ProductGridModellClass {
    public int hit;
    public int seq;
    @SerializedName("member_seq") public int memberSeq;
    public String title;
    public String tel;
    public String description;
    public String post_nickname;
    Integer image; //R.drawable.iphx 한걸 그냥떄려박아서 인티저인듯 이것을.. 원래하던방식으로 바꿔야할것같다~
    @SerializedName("post_member_icon_filename") public String postMemberIconFilename;
    @SerializedName("reg_date") public String regDate;
    @SerializedName("post_updated_datetime") public String updatedDate;

    @SerializedName("is_keep") public boolean isKeep;
    @SerializedName("image_filename") public String imageFilename;
    @SerializedName("total_image_filename") public ArrayList<ImageItem> totalImageFilename = new ArrayList<>();


    public ProductGridModellClass(Integer image) {
        this.image = image;
    }

    public Integer getImage() {
        return image;
    }

    public void setImage(Integer image) {
        this.image = image;
    }
}
