package com.uap.data.remote.request;

public class UpdateProfileRequest {
    private String phone;
    private String address;
    private Boolean gender;
    private String studentAvatar; // <- thêm

    public UpdateProfileRequest(String phone, String address, Boolean gender, String studentAvatar) {
        this.phone = phone;
        this.address = address;
        this.gender = gender;
        this.studentAvatar = studentAvatar;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Boolean getGender() {
        return gender;
    }

    public void setGender(Boolean gender) {
        this.gender = gender;
    }

    public String getStudentAvatar() {
        return studentAvatar;
    }

    public void setStudentAvatar(String studentAvatar) {
        this.studentAvatar = studentAvatar;
    }
}
