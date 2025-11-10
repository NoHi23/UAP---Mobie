package com.uap.data.remote.response.student;

import java.util.List;

public class StudentAttendanceResponse {
    private boolean success;
    private List<AttendanceSemester> data;

    public boolean isSuccess() { return success; }
    public List<AttendanceSemester> getData() { return data; }

    // inner
    public static class AttendanceSemester {
        private Semester semester;
        private List<AttendanceSubject> subjects;

        public Semester getSemester() { return semester; }
        public List<AttendanceSubject> getSubjects() { return subjects; }
    }

    public static class Semester {
        private String _id;
        private String semesterName;
        public String getSemesterName() { return semesterName; }
    }

    public static class AttendanceSubject {
        private SubjectId subjectId;
        private String subjectName;
        private String subjectCode;
        private String classId;
        private String className;
        private int totalSlots;
        private int absentSlots;
        private int attendanceRate;
        private boolean isFailed;

        public SubjectId getSubjectId() { return subjectId; }
        public String getSubjectName() { return subjectName; }
        public String getSubjectCode() { return subjectCode; }
        public String getClassName() { return className; }
        public int getTotalSlots() { return totalSlots; }
        public int getAbsentSlots() { return absentSlots; }
        public int getAttendanceRate() { return attendanceRate; }
        public boolean isFailed() { return isFailed; }
    }

    public static class SubjectId {
        private String _id;
        private String subjectName;
        private String subjectCode;
    }
}
