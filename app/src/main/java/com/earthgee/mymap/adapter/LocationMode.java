package com.earthgee.mymap.adapter;

/**
 * Created by earthgee on 2016/2/10.
 * 切换定位模式后增加更多ui展示
 */
public enum LocationMode {
    NORMAL("普通"),FOLLOW("跟随"),DIRECTION("罗盘");

    private String mLocationMode;

    LocationMode(String mLocationMode){
        this.mLocationMode=mLocationMode;
    }

    public String getmLocationMode() {
        return mLocationMode;
    }

    public void setmLocationMode(String mLocationMode) {
        this.mLocationMode = mLocationMode;
    }
}
