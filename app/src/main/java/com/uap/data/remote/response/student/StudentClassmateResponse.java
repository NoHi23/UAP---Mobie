package com.uap.data.remote.response.student;

import com.google.gson.annotations.SerializedName;
import com.uap.domain.model.Classmate;
import java.util.List;

public class StudentClassmateResponse {

    @SerializedName("success")
    private boolean success;

    @SerializedName("count")
    private int count;

    @SerializedName("className")
    private String className;

    @SerializedName("data")
    private List<Classmate> data;

    public boolean isSuccess() {
        return success;
    }

    public int getCount() {
        return count;
    }

    public String getClassName() {
        return className;
    }

    public List<Classmate> getData() {
        return data;
    }
}
