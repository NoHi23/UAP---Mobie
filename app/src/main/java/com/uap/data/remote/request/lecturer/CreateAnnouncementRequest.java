package com.uap.data.remote.request.lecturer;

public class CreateAnnouncementRequest {
    private String scheduleId;
    private String title;
    private String content;

    public CreateAnnouncementRequest(String scheduleId ,String title, String content) {
        this.title = title;
        this.content = content;
        this.scheduleId = scheduleId;
    }
}