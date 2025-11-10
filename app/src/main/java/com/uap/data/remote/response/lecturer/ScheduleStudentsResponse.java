package com.uap.data.remote.response.lecturer;

import java.util.List;

public class ScheduleStudentsResponse {
    private boolean success;
    private List<StudentInSchedule> data;

    public boolean isSuccess() { return success; }
    public List<StudentInSchedule> getData() { return data; }

    public static class StudentInSchedule {
        private String _id;
        private String studentCode;
        private String studentAvatar;
        private String firstName;
        private String lastName;
        private String email;
        // danh sách điểm danh cho các buổi – ta chỉ quan tâm tới buổi hiện tại
        private List<AttendanceItem> attendance;

        public String get_id() { return _id; }
        public String getStudentCode() { return studentCode; }
        public String getStudentAvatar() { return studentAvatar; }
        public String getFirstName() { return firstName; }
        public String getLastName() { return lastName; }
        public String getEmail() { return email; }
        public List<AttendanceItem> getAttendance() { return attendance; }

        public static class AttendanceItem {
            private String scheduleId;
            private String status; // "Not Yet" / "Present" / "Absent" ...
            private String note;

            public String getScheduleId() { return scheduleId; }
            public String getStatus() { return status; }
            public String getNote() { return note; }
        }
    }
}
