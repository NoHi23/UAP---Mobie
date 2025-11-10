package com.uap.data.remote.response.student;


import java.util.List;

public class AnnouncementResponse {
    private Meta meta;
    private List<Announcement> data;

    public Meta getMeta() { return meta; }
    public List<Announcement> getData() { return data; }

    public static class Meta {
        private int total;
        private int page;
        private int limit;
        public int getTotal() { return total; }
        public int getPage() { return page; }
        public int getLimit() { return limit; }
    }

    public static class Announcement {
        private String _id;
        private String title;
        private String content;
        private String postBy;
        private String picture;
        private String createdAt;

        public String get_id() { return _id; }
        public String getTitle() { return title; }
        public String getContent() { return content; }
        public String getPostBy() { return postBy; }
        public String getPicture() { return picture; }
        public String getCreatedAt() { return createdAt; }
    }
}
