package com.lixi.shoolcard.pages.model;

import java.io.Serializable;

/**
 * 电控充值记录（按房间查询）
 */
public class RechardRecord implements Serializable {
    private String building;
    private String room;
    private String electricBox;
    private String amount;
    private String name;
    private String grade;
    private String time;

    public String getBuilding() {
        return building;
    }

    public void setBuilding(String building) {
        this.building = building;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public String getElectricBox() {
        return electricBox;
    }

    public void setElectricBox(String electricBox) {
        this.electricBox = electricBox;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
