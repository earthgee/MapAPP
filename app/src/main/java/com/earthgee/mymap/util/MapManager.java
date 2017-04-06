package com.earthgee.mymap.util;

import android.content.Context;
import android.location.Location;
import android.media.Image;
import android.widget.ImageView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.overlayutil.PoiOverlay;
import com.baidu.mapapi.search.core.PoiInfo;
import com.baidu.mapapi.search.poi.PoiDetailSearchOption;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.earthgee.mymap.R;
import com.earthgee.mymap.adapter.LocationMode;
import com.earthgee.mymap.adapter.MapMode;

import static com.earthgee.mymap.adapter.MapMode.*;

/**
 * Created by earthgee on 2016/2/10.
 */
public class MapManager {
    private BaiduMap mBaiduMap;
    private MapMode mMapMode= NORMAL;
    private LocationMode mLocationMode=LocationMode.NORMAL;
    private PoiSearch mPoiSearch;
    private Context mContext;

    private LatLng mLocation;

    public MapManager(BaiduMap baiduMap,Context context) {
        this.mBaiduMap = baiduMap;
        mBaiduMap.setBuildingsEnabled(true);
        this.mContext=context;
    }

    public LatLng getmLocation() {
        return mLocation;
    }

    public void setmPoiSearch(PoiSearch mPoiSearch) {
        this.mPoiSearch = mPoiSearch;
    }

    //开启定位图层
    public void setLocationState(boolean state) {
        mBaiduMap.setMyLocationEnabled(state);
    }

    //设置定位数据
    public void setLocationData(MyLocationData data) {
        mBaiduMap.setMyLocationData(data);
    }

    //初始化显示的地图
    public void init(LatLng location) {
        MapStatus ms=generateMapStatus(location);
        mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(ms));
    }

    //保存位置信息
    public void saveLocationInfo(LatLng location){
        this.mLocation=location;
    }

    public MapStatus generateMapStatus(LatLng location){
        return new MapStatus.Builder().overlook(0).zoom(18).rotate(0).target(location).build();
    }

    //切换地图模式
    public String changeMapMode(){
        String ans=null;
        switch (mMapMode){
            case NORMAL:
                mMapMode=GPS;
                mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
                break;
            case GPS:
                mMapMode=NORMAL;
                mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
                break;
        }
        ans=mMapMode.getMode();
        return ans;
    }

    //切换地图到卫星图模式
    public void changeMapTo3D(){
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
    }

    //切换地图到普通模式
    public void changeMapTo2D(){
        mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
    }

    //切换定位模式
    public String changeLocationMode(ImageView locationIcon){
        String ans=null;
        switch (mLocationMode){
            //正常模式下,地图可以移动,底部不显示任何内容,此时点击切换到跟随模式
            case NORMAL:
                mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(MyLocationConfiguration.LocationMode.FOLLOWING,true,null));
                mLocationMode=LocationMode.FOLLOW;
                locationIcon.setImageResource(R.mipmap.main_icon_follow);
                locationIcon.setPadding(0,0,0,0);
                break;
            //跟随模式下，点击进入罗盘模式
            case FOLLOW:
                mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(MyLocationConfiguration.LocationMode.COMPASS,true,null));
                mLocationMode=LocationMode.DIRECTION;
                locationIcon.setImageResource(R.mipmap.main_icon_compass);
                locationIcon.setPadding(0,0,0,0);
                break;
            //罗盘模式下,点击进入跟随模式
            case DIRECTION:
                mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(MyLocationConfiguration.LocationMode.FOLLOWING,true,null));
                mLocationMode=LocationMode.FOLLOW;
                locationIcon.setImageResource(R.mipmap.main_icon_follow);
                locationIcon.setPadding(0, 0, 0, 0);
                mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(generateMapStatus(mLocation)));
                break;
        }
        ans=mLocationMode.getmLocationMode();
        return ans;
    }

    public void changeLocationModeToNormal(ImageView locationIcon){
        mBaiduMap.setMyLocationConfigeration(new MyLocationConfiguration(MyLocationConfiguration.LocationMode.NORMAL,true,null));
        mLocationMode=LocationMode.NORMAL;
        locationIcon.setImageResource(R.mipmap.main_icon_location);
        int padding=(int) (6*mContext.getResources().getDisplayMetrics().density);
        locationIcon.setPadding(padding,padding,padding,padding);
    }

    //poi搜索覆盖物
    public void setSearchOverLay(PoiResult result){
        mBaiduMap.clear();
        PoiOverlay overLay=new MyPoiOverlay(mBaiduMap);
        mBaiduMap.setOnMarkerClickListener(overLay);
        overLay.setData(result);
        overLay.addToMap();
        overLay.zoomToSpan();
    }

    private class MyPoiOverlay extends PoiOverlay{

        /**
         * 构造函数
         *
         * @param baiduMap 该 PoiOverlay 引用的 BaiduMap 对象
         */
        public MyPoiOverlay(BaiduMap baiduMap) {
            super(baiduMap);
        }

        @Override
        public boolean onPoiClick(int i) {
            super.onPoiClick(i);
           // PoiInfo poi=getPoiResult().getAllPoi().get(i);
           // mPoiSearch.searchPoiDetail((new PoiDetailSearchOption()).poiUid(poi.uid));
            return true;
        }
    }

}





