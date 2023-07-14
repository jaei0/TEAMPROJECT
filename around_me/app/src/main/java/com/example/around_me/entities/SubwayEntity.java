package com.example.around_me.entities;

import com.google.gson.annotations.SerializedName;


public class SubwayEntity {
    @SerializedName("index")
    private int index;
    @SerializedName("호선")
    private String line;
    @SerializedName("역명")
    private String station;
    @SerializedName("상호")
    private String name;
    @SerializedName("구분")
    private String category;
    @SerializedName("상세")
    private String detail;
    @SerializedName("시간(분)")
    private String time;

    public SubwayEntity(String line, String station, String category, String name, String detail, String time) {
        this.line = line;
        this.station = station;
        this.category = category;
        this.name = name;
        this.detail = detail;
        this.time = time;
    }

    public int getIndex() {
        return index;
    }

    public String getLine() {
        return line;
    }

    public String getStation() {
        return station;
    }

    public String getName() {
        return name;
    }

    public String getCategory() {
        return category;
    }

    public String getDetail() {
        return detail;
    }

    public String getTime() {
        return time;
    }

}
