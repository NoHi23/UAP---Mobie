package com.uap.data.remote.response.student;

import com.uap.domain.model.TimetableItem;

import java.util.List;

public class StudentTimetableResponse {
    private boolean success;
    private List<TimetableItem> data;

    public boolean isSuccess() {
        return success;
    }

    public List<TimetableItem> getData() {
        return data;
    }
}
