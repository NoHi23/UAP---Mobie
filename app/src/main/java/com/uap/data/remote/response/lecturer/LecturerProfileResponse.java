package com.uap.data.remote.response.lecturer;

import com.google.gson.annotations.SerializedName;

public class LecturerProfileResponse {

    @SerializedName("success")
    private boolean success;

    @SerializedName("data")
    private Data data;

    public boolean isSuccess() {
        return success;
    }

    public Data getData() {
        return data;
    }

    // ====================== INNER CLASSES ======================

    public static class Data {
        @SerializedName("_id")
        private String _id;

        @SerializedName("lecturerCode")
        private String lecturerCode;

        @SerializedName("citizenID")
        private long citizenID;

        @SerializedName("firstName")
        private String firstName;

        @SerializedName("lastName")
        private String lastName;

        @SerializedName("gender")
        private boolean gender;

        @SerializedName("phone")
        private String phone;

        @SerializedName("address")
        private String address;

        @SerializedName("dateOfBirth")
        private String dateOfBirth;

        @SerializedName("semester")
        private String semester;

        @SerializedName("semesterNo")
        private int semesterNo;

        @SerializedName("curriculumId")
        private String curriculumId;

        @SerializedName("accountId")
        private String accountId;

        @SerializedName("majorId")
        private Major majorId;

        @SerializedName("account")
        private Account account;

        public String get_id() {
            return _id;
        }

        public String getLecturerCode() {
            return lecturerCode;
        }

        public long getCitizenID() {
            return citizenID;
        }

        public String getFirstName() {
            return firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public boolean isGender() {
            return gender;
        }

        public String getPhone() {
            return phone;
        }

        public String getAddress() {
            return address;
        }

        public String getDateOfBirth() {
            return dateOfBirth;
        }

        public String getSemester() {
            return semester;
        }

        public int getSemesterNo() {
            return semesterNo;
        }

        public String getCurriculumId() {
            return curriculumId;
        }

        public String getAccountId() {
            return accountId;
        }

        public Major getMajorId() {
            return majorId;
        }

        public Account getAccount() {
            return account;
        }
    }

    public static class Major {
        @SerializedName("_id")
        private String _id;

        @SerializedName("majorName")
        private String majorName;

        @SerializedName("majorCode")
        private String majorCode;

        public String get_id() {
            return _id;
        }

        public String getMajorName() {
            return majorName;
        }

        public String getMajorCode() {
            return majorCode;
        }
    }

    public static class Account {
        @SerializedName("_id")
        private String _id;

        @SerializedName("email")
        private String email;

        @SerializedName("role")
        private String role;

        @SerializedName("status")
        private boolean status;

        public String get_id() {
            return _id;
        }

        public String getEmail() {
            return email;
        }

        public String getRole() {
            return role;
        }

        public boolean isStatus() {
            return status;
        }
    }
}
