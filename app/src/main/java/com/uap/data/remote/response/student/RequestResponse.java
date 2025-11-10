package com.uap.data.remote.response.student;

import java.util.List;

public class RequestResponse {
    private boolean success;
    private int count;
    private List<StudentRequest> data;

    public boolean isSuccess() { return success; }
    public int getCount() { return count; }
    public List<StudentRequest> getData() { return data; }

    public static class StudentRequest {
        private String _id;
        private String studentId;
        private String requestType;
        private String title;
        private String description;
        private String status;
        private String createdAt;

        public String get_id() { return _id; }
        public String getStudentId() { return studentId; }
        public String getRequestType() { return requestType; }
        public String getTitle() { return title; }
        public String getDescription() { return description; }
        public String getStatus() { return status; }
        public String getCreatedAt() { return createdAt; }
    }
}
