package com.uap.data.remote.response.lecturer;

import com.google.gson.annotations.SerializedName;
import java.util.List;

public class LecturerClassesResponse {

    @SerializedName("success")
    private boolean success;

    @SerializedName("data")
    private List<ClassInfo> data;

    public boolean isSuccess() { return success; }
    public List<ClassInfo> getData() { return data; }

    public static class ClassInfo {
        @SerializedName("classId")
        private String classId;

        @SerializedName("className")
        private String className;

        @SerializedName("subjectName")
        private String subjectName;

        @SerializedName("subjectCode")
        private String subjectCode;

        @SerializedName("studentCount")
        private int studentCount;

        @SerializedName("schedules")
        private List<Schedule> schedules;

        public String getClassId() { return classId; }
        public String getClassName() { return className; }
        public String getSubjectName() { return subjectName; }
        public String getSubjectCode() { return subjectCode; }
        public int getStudentCount() { return studentCount; }
        public List<Schedule> getSchedules() { return schedules; }
    }

    public static class Schedule {
        @SerializedName("scheduleId")
        private String scheduleId;
        @SerializedName("date")
        private String date;
        @SerializedName("slot")
        private int slot;
        @SerializedName("startTime")
        private String startTime;
        @SerializedName("endTime")
        private String endTime;
        @SerializedName("room")
        private String room;

        public String getDate() { return date; }
        public String getStartTime() { return startTime; }
        public String getEndTime() { return endTime; }
        public String getRoom() { return room; }
    }
}
