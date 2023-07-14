package com.example.around_me.entities;

import com.google.gson.annotations.SerializedName;

public class ParkingEntity {
    @SerializedName("index")
    private int index;
    @SerializedName("주차장ID")
    private String parkId;
    @SerializedName("구군코드")
    private String guCode;
    @SerializedName("주차장명")
    private String name;
    @SerializedName("주소")
    private String address;
    @SerializedName("기관명")
    private String adminName;
    @SerializedName("전화번호")
    private String telNum;
    @SerializedName("유무료구분")
    private String pay;
    @SerializedName("오픈요일")
    private String openDay;
    @SerializedName("평일오픈시간")
    private String weekStartTime;
    @SerializedName("평일마감시간")
    private String weekEndTime;
    @SerializedName("주말오픈시간")
    private String weekendStartTime;
    @SerializedName("주말마감시간")
    private String weekendEndTime;
    @SerializedName("상세")
    private String detail;

    public int getIndex() {
        return index;
    }

    public String getParkId() {
        return parkId;
    }

    public String getGuCode() {
        return guCode;
    }

    public String getName() {
        return name;
    }

    public String getAddress() {
        return address;
    }

    public String getAdminName() {
        return adminName;
    }

    public String getTelNum() {
        return telNum;
    }

    public String getPay() {
        return pay;
    }

    public String getOpenDay() {
        return openDay;
    }

    public String getWeekStartTime() {
        return weekStartTime;
    }

    public String getWeekEndTime() {
        return weekEndTime;
    }

    public String getWeekendStartTime() {
        return weekendStartTime;
    }

    public String getWeekendEndTime() {
        return weekendEndTime;
    }

    public String getDetail() {
        return detail;
    }
}
