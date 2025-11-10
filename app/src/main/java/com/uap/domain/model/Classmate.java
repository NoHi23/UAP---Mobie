package com.uap.domain.model;

import com.google.gson.annotations.SerializedName;
import java.io.Serializable;

public class Classmate implements Serializable {

    @SerializedName("_id")
    private String _id;

    @SerializedName("studentCode")
    private String studentCode;

    @SerializedName("studentAvatar")
    private String studentAvatar;

    @SerializedName("firstName")
    private String firstName;

    @SerializedName("lastName")
    private String lastName;

    public String get_id() { return _id; }
    public String getStudentCode() { return studentCode; }
    public String getStudentAvatar() { return studentAvatar; }
    public String getFirstName() { return firstName; }
    public String getLastName() { return lastName; }
}
