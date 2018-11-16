package com.mobitant.bestfood.item;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class NotificationCommentItem {
    private String contents;
    private String writer;
    @SerializedName("created_at") private String createdAt;
    private String postId;
    private int comment_like;
    @SerializedName("_id") private String id;
    @SerializedName("comment_writer_icon_filename") private String memberIconFileName;

    public String getMemberIconFileName() {
        return memberIconFileName;
    }

    public void setMemberIconFileName(String memberIconFileName) {
        this.memberIconFileName = memberIconFileName;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getContents() {
        return contents;
    }

    public void setContents(String contents) {
        this.contents = contents;
    }

    public String getWriter() {
        return writer;
    }

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public void setWriter(String writer) {
        this.writer = writer;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public int getComment_like() {
        return comment_like;
    }

    public void setComment_like(int comment_like) {
        this.comment_like = comment_like;
    }

    @Override
    public String toString() {
        return "NotificationCommentItem{" +
                "contents='" + contents + '\'' +
                ", writer='" + writer + '\'' +

                ", postId='" + postId + '\'' +
                ", comment_like=" + comment_like +
                ", id='" + id + '\'' +
                ", memberIconFileName='" + memberIconFileName + '\'' +
                '}';
    }
}
