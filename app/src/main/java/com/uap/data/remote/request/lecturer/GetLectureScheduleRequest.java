package com.uap.data.remote.request.lecturer;

public class GetLectureScheduleRequest {
    private String from;
    private String to;
    public GetLectureScheduleRequest(String f, String t) {
        this.from = f;
        this.to = t;
    }
}
