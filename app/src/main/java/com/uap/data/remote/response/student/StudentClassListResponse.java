package com.uap.data.remote.response.student;

import java.util.List;

public class StudentClassListResponse {
    private boolean success;
    private int count;
    private List<StudentClassItem> data;

    public boolean isSuccess() { return success; }
    public int getCount() { return count; }
    public List<StudentClassItem> getData() { return data; }

    public static class StudentClassItem {
        private String _id;
        private String className;
        private boolean status;
        private Subject subjectId;
        private Lecturer lecturerId;

        public String get_id() { return _id; }
        public String getClassName() { return className; }
        public Subject getSubjectId() { return subjectId; }
        public Lecturer getLecturerId() { return lecturerId; }

        public static class Subject {
            private String _id;
            private String subjectName;
            private String subjectCode;

            public String getSubjectName() { return subjectName; }
            public String getSubjectCode() { return subjectCode; }
        }

        public static class Lecturer {
            private String _id;
            private String firstName;
            private String lastName;
            public String getFullName() {
                return (lastName != null ? lastName : "") + " " + (firstName != null ? firstName : "");
            }
        }
    }
}
