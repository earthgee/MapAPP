package com.earthgee.mymap.adapter;

/**
 * Created by earthgee on 2016/2/10.
 */
public enum MapMode {
    NORMAL("正常"),GPS("卫星");

    private String mode;

    MapMode(String mode){
        this.mode=mode;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}
