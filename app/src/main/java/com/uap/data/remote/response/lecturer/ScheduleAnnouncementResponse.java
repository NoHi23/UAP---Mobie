package com.uap.data.remote.response.lecturer;

import java.util.List;

public class ScheduleAnnouncementResponse {
    private boolean success;
    private int count;
    private List<ScheduleAnnouncement> data;

    public boolean isSuccess() { return success; }
    public int getCount() { return count; }
    public List<ScheduleAnnouncement> getData() { return data; }

    public static class ScheduleAnnouncement {
        private String _id;
        private String title;
        private String content;
        private String createdAt;

        public String get_id() { return _id; }
        public String getTitle() { return title; }
        public String getContent() { return content; }
        public String getCreatedAt() { return createdAt; }
    }
}