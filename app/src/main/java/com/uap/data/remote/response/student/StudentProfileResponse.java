package com.uap.data.remote.response.student;

import com.uap.domain.model.StudentProfile;

public class StudentProfileResponse {
    private boolean success;
    private StudentProfile data;

    public boolean isSuccess() { return success; }
    public StudentProfile getData() { return data; }
}