package com.uap.data.remote.request.lecturer;

public class UpdateLecturerProfileRequest {
    private String address;
    private String phone;
    private String dateOfBirth;
    private boolean gender;

    public UpdateLecturerProfileRequest(String address, String phone, String dateOfBirth, boolean gender) {
        this.address = address;
        this.phone = phone;
        this.dateOfBirth = dateOfBirth;
        this.gender = gender;
    }
}
