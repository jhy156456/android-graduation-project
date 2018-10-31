package com.mobitant.bestfood.item;

import com.google.gson.annotations.SerializedName;

/**
 * Created by user on 2016-08-10.
 */
public class SingerItem {

    String name;
    String mobile;
    int age;
    int resId;
    String id;
    public String memberIconFileName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public SingerItem(String name, String mobile) {
        this.name = name;
        this.mobile = mobile;
    }

    public SingerItem(String name, String mobile, int age, String memberIconFileName,String id) {
        this.name = name;
        this.mobile = mobile;
        this.age = age;
        this.memberIconFileName = memberIconFileName;
        this.id = id;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "SingerItem{" +
                "name='" + name + '\'' +
                ", mobile='" + mobile + '\'' +
                ", age=" + age +
                ", resId=" + resId +
                ", id='" + id + '\'' +
                ", memberIconFileName='" + memberIconFileName + '\'' +
                '}';
    }
}

