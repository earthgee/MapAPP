package com.earthgee.mymap.entity;

/**
 * Created by earthgee on 2016/3/7.
 */
public class SearchParams {

    private String startCity;
    private String endCity;
    private String startPosition;
    private String endPosition;

    public SearchParams(String startCity, String endCity, String startPosition, String endPosition) {
        this.startCity = startCity;
        this.endCity = endCity;
        this.startPosition = startPosition;
        this.endPosition = endPosition;
    }

    public String getStartCity() {
        return startCity;
    }

    public void setStartCity(String startCity) {
        this.startCity = startCity;
    }

    public String getEndCity() {
        return endCity;
    }

    public void setEndCity(String endCity) {
        this.endCity = endCity;
    }

    public String getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(String startPosition) {
        this.startPosition = startPosition;
    }

    public String getEndPosition() {
        return endPosition;
    }

    public void setEndPosition(String endPosition) {
        this.endPosition = endPosition;
    }
}
