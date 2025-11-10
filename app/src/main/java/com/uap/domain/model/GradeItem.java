package com.uap.domain.model;

public class GradeItem {
    private String _id;
    private SubjectMini subjectId;
    private ComponentMini componentId;
    private String studentId;
    private double score;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public SubjectMini getSubjectId() {
        return subjectId;
    }

    public void setSubjectId(SubjectMini subjectId) {
        this.subjectId = subjectId;
    }

    public ComponentMini getComponentId() {
        return componentId;
    }

    public void setComponentId(ComponentMini componentId) {
        this.componentId = componentId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
    }
}
