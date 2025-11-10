package com.uap.data.remote.response.student;

import com.google.gson.annotations.SerializedName;
import com.uap.domain.model.ExamItem;

import java.util.List;

public class StudentExamResponse {
    @SerializedName("examSchedule")
    private List<ExamItem> examSchedule;

    public List<ExamItem> getExamSchedule() {
        return examSchedule;
    }
}
