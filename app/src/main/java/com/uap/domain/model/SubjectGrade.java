package com.uap.domain.model;

import java.util.List;

public class SubjectGrade {
    private String subjectId;
    private String subjectName;
    private String subjectCode;
    private List<GradeItem> components;
    private double finalScore; // đã tính

    public SubjectGrade(String subjectId, String subjectName, String subjectCode,
                        List<GradeItem> components, double finalScore) {
        this.subjectId = subjectId;
        this.subjectName = subjectName;
        this.subjectCode = subjectCode;
        this.components = components;
        this.finalScore = finalScore;
    }

    public String getSubjectId() { return subjectId; }
    public String getSubjectName() { return subjectName; }
    public String getSubjectCode() { return subjectCode; }
    public List<GradeItem> getComponents() { return components; }
    public double getFinalScore() { return finalScore; }
}
