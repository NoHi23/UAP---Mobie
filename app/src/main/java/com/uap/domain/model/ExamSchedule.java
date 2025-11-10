package com.uap.domain.model;

import java.io.Serializable;

public class ExamSchedule implements Serializable {
    private String _id;
    private String courseName;
    private String examDate;
    private String time;
    private String room;
    private String note;

    public String get_id() { return _id; }
    public String getCourseName() { return courseName; }
    public String getExamDate() { return examDate; }
    public String getTime() { return time; }
    public String getRoom() { return room; }
    public String getNote() { return note; }
}
