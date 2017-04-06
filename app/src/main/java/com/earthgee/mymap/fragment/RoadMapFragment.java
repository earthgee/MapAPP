package com.earthgee.mymap.fragment;

import android.os.Bundle;
import android.view.View;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.overlayutil.BikingRouteOverlay;
import com.baidu.mapapi.overlayutil.DrivingRouteOverlay;
import com.baidu.mapapi.overlayutil.OverlayManager;
import com.baidu.mapapi.overlayutil.TransitRouteOverlay;
import com.baidu.mapapi.overlayutil.WalkingRouteOverlay;
import com.baidu.mapapi.search.core.RouteLine;
import com.baidu.mapapi.search.route.BikingRouteLine;
import com.baidu.mapapi.search.route.DrivingRouteLine;
import com.baidu.mapapi.search.route.TransitRouteLine;
import com.baidu.mapapi.search.route.WalkingRouteLine;
import com.earthgee.mymap.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by earthgee on 2016/2/13.
 */
public class RoadMapFragment extends AbsBaseFragment{
    @InjectView(R.id.e_map_view)
    MapView mMapView;
    BaiduMap mBaiduMap;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_map_road;
    }

    @Override
    protected void init(Bundle savedInstanceState, View contentView) {
        setFragmentStatus(FRAGMENT_STATUS_SUCCESS);
        ButterKnife.inject(this, contentView);
        mBaiduMap=mMapView.getMap();
        RouteLine routeLine=getArguments().getParcelable("route_line");
        if(routeLine instanceof BikingRouteLine){
            setBikingOverlay(routeLine);
        }else if(routeLine instanceof TransitRouteLine){
            setBusOverlay(routeLine);
        }else if(routeLine instanceof DrivingRouteLine){
            setDrivingOverlay(routeLine);
        }else{
            setWalkingOverlay(routeLine);
        }
    }

    private void setBikingOverlay(RouteLine routeLine){
        BikingRouteOverlay overlay=new BikingRouteOverlay(mBaiduMap);
        overlay.setData((BikingRouteLine) routeLine);
        overlay.addToMap();
        overlay.zoomToSpan();
    }

    private void setBusOverlay(RouteLine routeLine){
        TransitRouteOverlay overlay=new TransitRouteOverlay(mBaiduMap);
        overlay.setData((TransitRouteLine) routeLine);
        overlay.addToMap();
        overlay.zoomToSpan();
    }

    private void setDrivingOverlay(RouteLine routeLine){
        DrivingRouteOverlay overlay=new DrivingRouteOverlay(mBaiduMap);
        overlay.setData((DrivingRouteLine) routeLine);
        overlay.addToMap();
        overlay.zoomToSpan();
    }

    private void setWalkingOverlay(RouteLine routeLine){
        WalkingRouteOverlay overlay=new WalkingRouteOverlay(mBaiduMap);
        overlay.setData((WalkingRouteLine) routeLine);
        overlay.addToMap();
        overlay.zoomToSpan();
    }

}
