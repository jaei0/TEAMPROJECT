package com.subway.entities;
import org.json.JSONObject;

import java.util.Objects;

public class SubwayEntity {
    public static SubwayEntity build() {
        return new SubwayEntity();
    }

    public static SubwayEntity build(JSONObject subwayObject) {
        final String detail = !subwayObject.isNull("상세")? subwayObject.getString("상세") : "";
        return SubwayEntity.build()
                .setLine(subwayObject.getString("호선"))
                .setStation(subwayObject.getString("역명"))
                .setCategory(subwayObject.getString("구분"))
                .setName(subwayObject.getString("상호"))
                .setDetail(detail)
                .setTime(subwayObject.getInt("시간(분)"));
    }

    private String line;
    private String station;
    private String name;
    private String category;
    private String detail;
    private int time;


    public String getLine() {
        return line;
    }

    public SubwayEntity setLine(String line) {
        this.line = line;
        return this;
    }

    public String getStation() {
        return station;
    }

    public SubwayEntity setStation(String station) {
        this.station = station;
        return this;
    }

    public String getName() {
        return name;
    }

    public SubwayEntity setName(String name) {
        this.name = name;
        return this;
    }

    public String getCategory() {
        return category;
    }

    public SubwayEntity setCategory(String category) {
        this.category = category;
        return this;
    }

    public String getDetail() {
        return detail;
    }

    public SubwayEntity setDetail(String detail) {
        this.detail = detail;
        return this;
    }

    public int getTime() {
        return time;
    }

    public SubwayEntity setTime(int time) {
        this.time = time;
        return this;
    }


}
