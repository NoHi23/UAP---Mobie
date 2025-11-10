package com.uap.domain.model;

import java.io.Serializable;

public class ExamItem implements Serializable {
    private String _id;
    private ExamSchedule examSchedule;
    private String attendStatus;

    public String get_id() { return _id; }
    public ExamSchedule getExamSchedule() { return examSchedule; }
    public String getAttendStatus() { return attendStatus; }
}