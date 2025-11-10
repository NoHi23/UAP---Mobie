package com.uap.data.remote.request.student;

public class SubmitStudentRequest {
    private String requestType;
    private String title;
    private String description;

    public SubmitStudentRequest(String requestType, String title, String description) {
        this.requestType = requestType;
        this.title = title;
        this.description = description;
    }
}
