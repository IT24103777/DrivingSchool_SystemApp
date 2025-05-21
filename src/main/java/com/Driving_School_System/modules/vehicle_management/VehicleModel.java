package com.Driving_School_System.modules.vehicle_management;

import com.Driving_School_System.common.BaseEntity;

public class VehicleModel extends BaseEntity {
    private String number;
    private String type;
    private String status;

    public VehicleModel() {}

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
