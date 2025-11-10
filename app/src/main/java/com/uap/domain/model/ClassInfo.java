package com.uap.domain.model;

import java.io.Serializable;

public class ClassInfo implements Serializable {
    private String _id;
    private String className;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }
}
