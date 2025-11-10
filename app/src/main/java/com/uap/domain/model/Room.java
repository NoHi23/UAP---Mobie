package com.uap.domain.model;

import java.io.Serializable;

public class Room implements Serializable {
    private String _id;
    private String roomName;

    public String get_id() {
        return _id;
    }

    public void set_id(String _id) {
        this._id = _id;
    }

    public String getRoomName() {
        return roomName;
    }

    public void setRoomName(String roomName) {
        this.roomName = roomName;
    }
}