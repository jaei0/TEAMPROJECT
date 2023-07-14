package com.parking.entities;
import org.json.JSONObject;

public class ParkingEntity {
    public static ParkingEntity build() {
        return new ParkingEntity();
    }

    public static ParkingEntity build(JSONObject parkingObject) {


        final String weekStartTime = !parkingObject.isNull("p_wk_st")? parkingObject.getString("p_wk_st") : "";
        final String weekEndTime = !parkingObject.isNull("p_wk_ed")? parkingObject.getString("p_wk_ed") : "";
        final String weekendStartTime = !parkingObject.isNull("p_sat_st")? parkingObject.getString("p_sat_st") : "";
        final String weekendEndTime = !parkingObject.isNull("p_sat_ed")? parkingObject.getString("p_sat_ed") : "";
        final String adminName = !parkingObject.isNull("p_m_nm")? parkingObject.getString("p_m_nm") : "";
        final String telNum = !parkingObject.isNull("tel")? parkingObject.getString("tel") : "";
        final String detail = !parkingObject.isNull("desc")? parkingObject.getString("desc") : "";
        return ParkingEntity.build()
                .setParkId(parkingObject.getString("park_id"))
                .setGuCode(parkingObject.getString("gu_code"))
                .setName(parkingObject.getString("p_name"))
                .setAddress(parkingObject.getString("st_addr"))
                .setAdminName(adminName)
                .setTelNum(telNum)
                .setPay(parkingObject.getString("p_pay_yn"))
                .setOpenDay(parkingObject.getString("p_op_day"))
                .setWeekStartTime(weekStartTime)
                .setWeekEndTime(weekEndTime)
                .setWeekendStartTime(weekendStartTime)
                .setWeekendEndTime(weekendEndTime)
                .setDetail(detail);
    }

    private String parkId;
    private String guCode;
    private String name;
    private String address;
    private String adminName;
    private String telNum;
    private String pay;
    private String openDay;
    private String weekStartTime;
    private String weekEndTime;
    private String weekendStartTime;
    private String weekendEndTime;
    private String detail;

    public String getParkId() {
        return parkId;
    }

    public ParkingEntity setParkId(String parkId) {
        this.parkId = parkId;
        return this;
    }

    public String getGuCode() {
        return guCode;
    }

    public ParkingEntity setGuCode(String guCode) {
        this.guCode = guCode;
        return this;
    }

    public String getName() {
        return name;
    }

    public ParkingEntity setName(String name) {
        this.name = name;
        return this;
    }

    public String getAddress() {
        return address;
    }

    public ParkingEntity setAddress(String address) {
        this.address = address;
        return this;
    }

    public String getAdminName() {
        return adminName;
    }

    public ParkingEntity setAdminName(String adminName) {
        this.adminName = adminName;
        return this;
    }

    public String getTelNum() {
        return telNum;
    }

    public ParkingEntity setTelNum(String telNum) {
        this.telNum = telNum;
        return this;
    }

    public String getPay() {
        return pay;
    }

    public ParkingEntity setPay(String pay) {
        this.pay = pay;
        return this;
    }

    public String getOpenDay() {
        return openDay;
    }

    public ParkingEntity setOpenDay(String openDay) {
        this.openDay = openDay;
        return this;
    }

    public String getWeekStartTime() {
        return weekStartTime;
    }

    public ParkingEntity setWeekStartTime(String weekStartTime) {
        this.weekStartTime = weekStartTime;
        return this;
    }

    public String getWeekEndTime() {
        return weekEndTime;
    }

    public ParkingEntity setWeekEndTime(String weekEndTime) {
        this.weekEndTime = weekEndTime;
        return this;
    }

    public String getWeekendStartTime() {
        return weekendStartTime;
    }

    public ParkingEntity setWeekendStartTime(String weekendStartTime) {
        this.weekendStartTime = weekendStartTime;
        return this;
    }

    public String getWeekendEndTime() {
        return weekendEndTime;
    }

    public ParkingEntity setWeekendEndTime(String weekendEndTime) {
        this.weekendEndTime = weekendEndTime;
        return this;
    }

    public String getDetail() {
        return detail;
    }

    public ParkingEntity setDetail(String detail) {
        this.detail = detail;
        return this;
    }
}
