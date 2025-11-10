package com.uap.domain.model;

public class LecturerScheduleItem {

    private String _id;
    private String semesterId;
    private Subject subjectId;
    private ClassInfo classId;
    private Room roomId;
    private String lecturerId;
    private String date;     // "2025-11-10T00:00:00.000Z"
    private int slot;
    private String startTime;
    private String endTime;
    private boolean taught; // server có gửi
    private LectureAttendance lectureAttendance;

    public String get_id() { return _id; }
    public String getSemesterId() { return semesterId; }
    public Subject getSubjectId() { return subjectId; }
    public ClassInfo getClassId() { return classId; }
    public Room getRoomId() { return roomId; }
    public String getLecturerId() { return lecturerId; }
    public String getDate() { return date; }
    public int getSlot() { return slot; }
    public String getStartTime() { return startTime; }
    public String getEndTime() { return endTime; }
    public boolean isTaught() { return taught; }
    public LectureAttendance getLectureAttendance() { return lectureAttendance; }

    // nested:
    public static class Subject {
        private String _id;
        private String subjectName;
        private String subjectCode;

        public String get_id() { return _id; }
        public String getSubjectName() { return subjectName; }
        public String getSubjectCode() { return subjectCode; }
    }

    public static class ClassInfo {
        private String _id;
        private String className;

        public String get_id() { return _id; }
        public String getClassName() { return className; }
    }

    public static class Room {
        private String _id;
        private String roomCode;
        private String roomName;

        public String get_id() { return _id; }
        public String getRoomCode() { return roomCode; }
        public String getRoomName() { return roomName; }
    }

    public static class LectureAttendance {
        private String _id;
        private boolean attendance;

        public String get_id() { return _id; }
        public boolean isAttendance() { return attendance; }
    }
}
