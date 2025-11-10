package com.uap.data.remote.request.student;

public class StudentEvaluateRequest {
    private String classId;
    private String criteria;
    private String comment;

    public StudentEvaluateRequest(String classId, String criteria, String comment) {
        this.classId = classId;
        this.criteria = criteria;
        this.comment = comment;
    }
}
