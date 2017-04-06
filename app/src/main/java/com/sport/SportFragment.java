package com.sport;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.Overlay;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.Polyline;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.model.LatLngBounds;
import com.baidu.mapapi.utils.DistanceUtil;
import com.earthgee.mymap.R;
import com.earthgee.mymap.fragment.AbsBaseFragment;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by earthgee on 16/4/14.
 */
public class SportFragment extends AbsBaseFragment {

    private double lastLatitude = 0.0;
    private double lastLongitude = 0.0;

    @InjectView(R.id.map)
    TextureMapView mapView;
    @InjectView(R.id.tv_step_num)
    TextView mStep;
    @InjectView(R.id.tv_step_speed)
    TextView mSpeed;

    private BaiduMap mBaiduMap;

    private BroadcastReceiver mOnShowNotification = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            //在前台显示运动轨迹及数据
            double latitude =intent.getDoubleExtra("latitude", 0.0);
            double longitude =intent.getDoubleExtra("longitude", 0.0);
            double veritory=intent.getDoubleExtra("veritory",0.0);

            if (lastLatitude != 0.0f && lastLongitude != 0.0f) {
                    List<LatLng> points = new ArrayList<>();
                    points.add(new LatLng(lastLatitude, lastLongitude));
                    points.add(new LatLng(latitude, longitude));
                    OverlayOptions options = new PolylineOptions().width(5).points(points).customTexture(BitmapDescriptorFactory.fromAsset("icon_road_blue_arrow.png")).dottedLine(true);
                    Polyline overlay= (Polyline) mBaiduMap.addOverlay(options);

                    LatLngBounds.Builder builder=new LatLngBounds.Builder();
                    builder.include(new LatLng(lastLatitude, lastLongitude));
                    builder.include(new LatLng(latitude, longitude));

                    //使地图最大限度适应轨迹
                    mBaiduMap.animateMapStatus(MapStatusUpdateFactory.newLatLngBounds(builder.build()),1000);
            }
            showVeritory(veritory);
            //通知后续接收到的广播
            setResultCode(Activity.RESULT_CANCELED);
        }
    };

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_sport;
    }

    @Override
    protected void init(Bundle savedInstanceState, View contentView) {
        setFragmentStatus(FRAGMENT_STATUS_SUCCESS);
        ButterKnife.inject(this, contentView);
        initMap();
    }

    private void initMap() {
        //不显示缩放控件
        mapView.showZoomControls(false);
        mBaiduMap = mapView.getMap();
    }

    public void updateStep(int step){
        try{
            mStep.setText(step+"步");
        }catch (Exception e){
            System.out.print("error");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        IntentFilter filter = new IntentFilter(SportService.ACTION_DOWN_NOTIFICATION);
        getActivity().registerReceiver(mOnShowNotification, filter, SportService.PERM_PRIVATE, null);
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
        getActivity().unregisterReceiver(mOnShowNotification);
    }

    @Override
    public void onDestroy() {
        mapView.onDestroy();
        super.onDestroy();
    }

    private void showVeritory(double veritory){
        mSpeed.setText(veritory/1000*60+"m/s");
    }

}
