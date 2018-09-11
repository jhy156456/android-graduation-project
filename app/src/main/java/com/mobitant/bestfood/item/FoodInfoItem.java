package com.mobitant.bestfood.item;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

/**
 * 게시글 정보를 저장하는 객체
 */
@org.parceler.Parcel
public class FoodInfoItem {
    public int seq;
    @SerializedName("member_seq") public int memberSeq;
    public String name;
    public String tel;
    public String description;
    public String post_nickname;
    public String os;
    public int reply;
    public int commentCount;
    public int hit;
    public int like,dislike,blame,del,imageNumber;
    public String postMemberIconFilename;
    @SerializedName("reg_date") public String regDate;
    @SerializedName("post_updated_datetime") public String updatedDate;
    @SerializedName("user_distance_meter") public double userDistanceMeter;
    @SerializedName("is_keep") public boolean isKeep;
    @SerializedName("image_filename") public String imageFilename;
    @SerializedName("total_image_filename") public ArrayList<ImageItem> totalImageFilename = new ArrayList<>();



    @Override
    public String toString() {

        return "FoodInfoItem{" +
                "seq=" + seq +
                ", memberSeq=" + memberSeq +
                ", name='" + name + '\'' +
                ", tel='" + tel + '\'' +
                ", description='" + description + '\'' +
                ", regDate='" + regDate + '\'' +
                ", modDate='" + updatedDate + '\'' +
                ", userDistanceMeter=" + userDistanceMeter +
                ", isKeep=" + isKeep +
                ", imageFilename='" + imageFilename + '\'' +
                ", totalImageFilename ='" + totalImageFilename.toString() + '\'' +
                '}';
    }
}
