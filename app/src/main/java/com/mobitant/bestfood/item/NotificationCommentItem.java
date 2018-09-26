package com.mobitant.bestfood.item;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

public class NotificationCommentItem {
    private String contents;
    private String writer;
    private String created_at;
    private String postId;
    private int comment_like;
    @SerializedName("comments") private ArrayList<NotificationCommentItem> commentItems;

    public ArrayList<NotificationCommentItem> getCommentItems() {
        return commentItems;
    }

    public void setCommentItems(ArrayList<NotificationCommentItem> commentItems) {
        this.commentItems = commentItems;
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

    @Override
    public String toString() {
        return "NotificationCommentItem{" +
                "contents='" + contents + '\'' +
                ", writer='" + writer + '\'' +
                ", created_at='" + created_at + '\'' +
                ", postId='" + postId + '\'' +
                '}';
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

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public int getComment_like() {
        return comment_like;
    }

    public void setComment_like(int comment_like) {
        this.comment_like = comment_like;
    }
}
