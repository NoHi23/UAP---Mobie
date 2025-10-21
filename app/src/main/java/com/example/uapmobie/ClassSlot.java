package com.example.uapmobie;

public class ClassSlot {
    private String courseName;
    private String duration;
    private String lecturerName;
    private String room;
    private String building;
    private int startPeriod;
    private int endPeriod;

    public ClassSlot(String courseName, String duration, String lecturerName, String room, String building, int startPeriod, int endPeriod) {
        this.courseName = courseName;
        this.duration = duration;
        this.lecturerName = lecturerName;
        this.room = room;
        this.building = building;
        this.startPeriod = startPeriod;
        this.endPeriod = endPeriod;
    }

    public String getCourseName() { return courseName; }
    public void setCourseName(String courseName) { this.courseName = courseName; }
    public int getStartPeriod() { return startPeriod; }
    public int getEndPeriod() { return endPeriod; }
    public String getLecturerName() { return lecturerName; }
    public String getRoom() { return room; }
    public String getBuilding() { return building; }
}