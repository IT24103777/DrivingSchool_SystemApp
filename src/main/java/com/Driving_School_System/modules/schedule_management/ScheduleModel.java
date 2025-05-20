package com.Driving_School_System.modules.schedule_management;

import com.Driving_School_System.common.BaseEntity;

public class ScheduleModel extends BaseEntity {
    private Long instructorId;
    private String date;
    private String time;
    private String vehicle;

    public ScheduleModel() {}

    public Long getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(Long instructorId) {
        this.instructorId = instructorId;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getVehicle() {
        return vehicle;
    }

    public void setVehicle(String vehicle) {
        this.vehicle = vehicle;
    }
}