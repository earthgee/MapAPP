package com.sport.entity;

import java.util.Date;

/**
 * Created by earthgee on 2016/4/16.
 */
public class Location {

    private Date time;
    private Double latitude;
    private Double longitude;
    private long sportId;

    public Location() {
        this.time = new Date();
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public long getSportId() {
        return sportId;
    }

    public void setSportId(long sportId) {
        this.sportId = sportId;
    }
}
