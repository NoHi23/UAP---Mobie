package com.uap.data.remote.request.lecturer;


public class UpdateAttendanceRequest {
    private String scheduleId;
    private String studentId;
    private String status;
    private String note;
    private String date;

    public UpdateAttendanceRequest(String scheduleId, String studentId,
                                   String status, String note, String date) {
        this.scheduleId = scheduleId;
        this.studentId = studentId;
        this.status = status;
        this.note = note;
        this.date = date;
    }
}