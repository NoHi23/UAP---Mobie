package com.uap.domain.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class NotificationItem implements Serializable {

    @SerializedName("_id")
    private String _id;

    @SerializedName("scheduleId")
    private String scheduleId;

    @SerializedName("title")
    private String title;

    @SerializedName("content")
    private String content;

    @SerializedName("createdAt")
    private String createdAt;

    @SerializedName("senderId")
    private Sender senderId;

    public String get_id() { return _id; }
    public String getScheduleId() { return scheduleId; }
    public String getTitle() { return title; }
    public String getContent() { return content; }
    public String getCreatedAt() { return createdAt; }
    public Sender getSenderId() { return senderId; }

    public static class Sender implements Serializable {
        @SerializedName("_id")
        private String _id;
        @SerializedName("email")
        private String email;
        @SerializedName("role")
        private String role;

        public String get_id() { return _id; }
        public String getEmail() { return email; }
        public String getRole() { return role; }
    }
}
