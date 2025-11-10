package com.uap.utils;

public class AppUtils {
    public static String normalizeStatus(String s) {
        if (s == null) return "Not Yet";
        switch (s.trim()) {
            case "NotYet":
            case "Not Yet":
                return "Not Yet";
            case "Present":
                return "Present";
            case "Absent":
                return "Absent";
            case "Excused":
                return "Excused";
            default:
                return s;
        }
    }

}
