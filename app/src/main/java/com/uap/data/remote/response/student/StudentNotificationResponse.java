package com.uap.data.remote.response.student;

import com.google.gson.annotations.SerializedName;
import com.uap.domain.model.NotificationItem;
import java.util.List;

public class StudentNotificationResponse {

    @SerializedName("success")
    private boolean success;

    @SerializedName("count")
    private int count;

    @SerializedName("data")
    private List<NotificationItem> data;

    public boolean isSuccess() {
        return success;
    }

    public int getCount() {
        return count;
    }

    public List<NotificationItem> getData() {
        return data;
    }
}
