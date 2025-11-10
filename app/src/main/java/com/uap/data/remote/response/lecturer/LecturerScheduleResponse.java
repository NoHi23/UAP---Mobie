package com.uap.data.remote.response.lecturer;

import com.uap.domain.model.LecturerScheduleItem;
import java.util.List;

public class LecturerScheduleResponse {
    private boolean success;
    private List<LecturerScheduleItem> data;
    private int attendedCount; // nếu server trả kèm

    public boolean isSuccess() {
        return success;
    }

    public List<LecturerScheduleItem> getData() {
        return data;
    }

    public int getAttendedCount() {
        return attendedCount;
    }
}
