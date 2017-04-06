package com.sport.entity;

import java.util.Date;

/**
 * Created by earthgee on 2016/4/16.
 * 保存运动数据的类
 */
public class Sport {

    private long id;
    private Date startDate;
    private Date endDate;
    private int step;
    private String imgPosition;

    public Sport(){
        id=-1;
        startDate=new Date();
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date date) {
        this.startDate = date;
    }

    public Date getEndDate() {
        return endDate;
    }

    public void setEndDate(Date endDate) {
        this.endDate = endDate;
    }

    public int getStep() {
        return step;
    }

    public void setStep(int step) {
        this.step = step;
    }

    public String getImgPosition() {
        return imgPosition;
    }

    public void setImgPosition(String imgPosition) {
        this.imgPosition = imgPosition;
    }
}
