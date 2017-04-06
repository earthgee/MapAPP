package com.earthgee.mymap.fragment;

import android.Manifest;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.poi.OnGetPoiSearchResultListener;
import com.baidu.mapapi.search.poi.PoiCitySearchOption;
import com.baidu.mapapi.search.poi.PoiDetailResult;
import com.baidu.mapapi.search.poi.PoiResult;
import com.baidu.mapapi.search.poi.PoiSearch;
import com.earthgee.mymap.ChoosePositionActivity;
import com.earthgee.mymap.R;
import com.earthgee.mymap.SearchPoiActivity;
import com.earthgee.mymap.util.LocationPermissionListener;
import com.earthgee.mymap.util.MapManager;
import com.getbase.floatingactionbutton.FloatingActionButton;
import com.getbase.floatingactionbutton.FloatingActionsMenu;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.listener.single.CompositePermissionListener;
import com.karumi.dexter.listener.single.PermissionListener;
import com.karumi.dexter.listener.single.SnackbarOnDeniedPermissionListener;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by earthgee on 2016/2/7.
 */
public class MapFragment extends AbsBaseFragment implements OnGetPoiSearchResultListener,FloatingActionsMenu.OnFloatingActionsMenuUpdateListener,BaiduMap.OnMyLocationClickListener,BaiduMap.OnMapLongClickListener,BaiduMap.OnMapTouchListener,BaiduMap.OnMarkerClickListener{

    @InjectView(R.id.e_map_view)
    MapView mMapView;

    @InjectView(R.id.root)
    ViewGroup rootView;
    @InjectView(R.id.map_shadow)
    View shadow;

    @InjectView(R.id.change_map_show_state)
    FloatingActionsMenu mChangeMapStatus;
    @InjectView(R.id.map_status_3d)
    FloatingActionButton mMapStatus3d;
    @InjectView(R.id.map_status_2d)
    FloatingActionButton mMapStatus2d;

    @InjectView(R.id.iv_location_mode)
    ImageView mLocationMode;

    private LocationClient mLocationClient;
    private BaiduMap mBaiduMap;
    private MapManager mMapManager;
    private PoiSearch mPoiSearch;

    private boolean isFirstLoc=true;

    //搜索地区的requestCode
    private int searchRequestCode = 1;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_map;
    }

    @Override
    protected void init(Bundle savedInstanceState, View contentView) {
        ButterKnife.inject(this, contentView);
        mMapView.showZoomControls(false);
        mBaiduMap = mMapView.getMap();
        mMapManager = new MapManager(mBaiduMap,getActivity());
        mMapManager.setLocationState(true);
        mChangeMapStatus.setOnFloatingActionsMenuUpdateListener(this);
        mBaiduMap.setOnMyLocationClickListener(this);
        mBaiduMap.setOnMapLongClickListener(this);
        mBaiduMap.setOnMapTouchListener(this);
        mBaiduMap.setOnMarkerClickListener(this);
        //请求定位权限
        requestLocationPermission();
        initListener();
    }

    private void requestLocationPermission() {
        PermissionListener listener = new LocationPermissionListener(this);
        PermissionListener locationListener = new CompositePermissionListener(listener, SnackbarOnDeniedPermissionListener.Builder.with(rootView, "请允许应用获取定位权限").withOpenSettingsButton("进入设置").build());
        Dexter.checkPermission(locationListener, Manifest.permission.ACCESS_COARSE_LOCATION);
    }

    public void startLocation(boolean isGrant) {
        startLocation();
    }

    //定位
    private void startLocation() {
        mLocationClient = new LocationClient(getActivity());
        mLocationClient.registerLocationListener(new OnLocationListener());
        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true);
        option.setCoorType("bd09ll");
        option.setScanSpan(10000);
        mLocationClient.setLocOption(option);
        mLocationClient.start();
    }

    private void initListener() {
        mMapStatus2d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMapManager.changeMapTo2D();
                mChangeMapStatus.toggle();
            }
        });

        mMapStatus3d.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMapManager.changeMapTo3D();
                mChangeMapStatus.toggle();
            }
        });

        shadow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mChangeMapStatus.toggle();
            }
        });

        mLocationMode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mMapManager.changeLocationMode(mLocationMode);
            }
        });
    }

    @Override
    public void onGetPoiResult(PoiResult poiResult) {
        if (poiResult == null || poiResult.error == SearchResult.ERRORNO.RESULT_NOT_FOUND) {
            Toast.makeText(getActivity(), "未找到结果", Toast.LENGTH_SHORT);
            return;
        }

        if (poiResult.error == SearchResult.ERRORNO.NO_ERROR) {
            mMapManager.setSearchOverLay(poiResult);
        }
    }

    public void searchOnMap(String keyword){
        PoiSearch search=PoiSearch.newInstance();
        search.setOnGetPoiSearchResultListener(this);
        search.searchInCity((new PoiCitySearchOption()).city("").keyword(keyword).pageNum(0));
    }

    @Override
    public void onGetPoiDetailResult(PoiDetailResult poiDetailResult) {
        if (poiDetailResult.error != SearchResult.ERRORNO.NO_ERROR) {
            Toast.makeText(getActivity(), "没有找到结果", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(getActivity(), poiDetailResult.getName() + ": " + poiDetailResult.getAddress(), Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onMenuExpanded() {
        shadow.setVisibility(View.VISIBLE);
    }

    @Override
    public void onMenuCollapsed() {
        shadow.setVisibility(View.GONE);
    }

    @Override
    public boolean onMyLocationClick() {
        Intent intent=new Intent(getActivity(), ChoosePositionActivity.class);
        intent.putExtra("location",mMapManager.getmLocation());
        intent.putExtra("isCurrent",true);
        startActivity(intent);
        return true;
    }

    @Override
    public void onMapLongClick(LatLng latLng) {
        Intent intent=new Intent(getActivity(),ChoosePositionActivity.class);
        intent.putExtra("location",latLng);
        intent.putExtra("isCurrent",false);
        startActivity(intent);
    }

    @Override
    public void onTouch(MotionEvent motionEvent) {
        mMapManager.changeLocationModeToNormal(mLocationMode);
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        Intent intent=new Intent(getActivity(),ChoosePositionActivity.class);
        intent.putExtra("location",marker.getPosition());
        intent.putExtra("isCurrent",false);
        startActivity(intent);
        return true;
    }

    //定位的监听器
    private class OnLocationListener implements BDLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            if (bdLocation == null || mMapView == null) {
                setFragmentStatus(FRAGMENT_STATUC_ERROR);
                return;
            }

            int type = bdLocation.getLocType();

            MyLocationData locData = new MyLocationData.Builder().accuracy(bdLocation.getRadius()).direction(100).latitude(bdLocation.getLatitude()).longitude(bdLocation.getLongitude()).build();
            mMapManager.setLocationData(locData);

            LatLng ll = new LatLng(bdLocation.getLatitude(), bdLocation.getLongitude());
            if(isFirstLoc){
                mMapManager.init(ll);
                isFirstLoc=false;
            }
                mMapManager.saveLocationInfo(ll);

            setFragmentStatus(FRAGMENT_STATUS_SUCCESS);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapManager.setLocationState(false);
        mLocationClient.stop();
        mMapView.onDestroy();
        mMapView = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == getActivity().RESULT_OK && requestCode == searchRequestCode) {
            String position = data.getStringExtra("position");
            mPoiSearch.searchInCity((new PoiCitySearchOption()).city("").keyword(position).pageNum(0));
        }
    }
}
