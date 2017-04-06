package com.earthgee.mymap;

import android.Manifest;
import android.content.Intent;
import android.location.Geocoder;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.Marker;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.TextureMapView;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.geocode.GeoCodeResult;
import com.baidu.mapapi.search.geocode.GeoCoder;
import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeOption;
import com.baidu.mapapi.search.geocode.ReverseGeoCodeResult;
import com.earthgee.mymap.fragment.MapFragment;
import com.earthgee.mymap.fragment.searchnearby.BusSearchFragment;
import com.earthgee.mymap.fragment.searchnearby.FoodSearchFragment;
import com.earthgee.mymap.util.SlidingTabStrip;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.listener.single.EmptyPermissionListener;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.w3c.dom.Text;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by earthgee on 16/3/23.
 */
public class ChoosePositionActivity extends AbsBaseActivity implements OnGetGeoCoderResultListener{

    @InjectView(R.id.content)
    SlidingUpPanelLayout mSlidingUpPanelLayout;
    @InjectView(R.id.map_view)
    TextureMapView mMapView;
    @InjectView(R.id.location_detail)
    TextView mLocationDetail;
    @InjectView(R.id.tab_strip)
    SlidingTabStrip slidingTabStrip;
    @InjectView(R.id.viewpager)
    ViewPager mViewPager;
    @InjectView(R.id.arrow)
    ImageView arrow;

    private LatLng mCurrentLocation;
    //点击的是否是定位的点
    private boolean isLocation;

    private BaiduMap baiduMap;
    private Marker mCurrentMarker;

    private GeoCoder mSearch;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_choose_position;
    }

    public LatLng getmCurrentLocation() {
        return mCurrentLocation;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);

        mCurrentLocation=getIntent().getParcelableExtra("location");
        isLocation=getIntent().getBooleanExtra("isCurrent",false);
        showLocationOnMap();
        searchLocationGeo();
        setListener();
        initViewPager();
    }

    private void showLocationOnMap(){
        baiduMap=mMapView.getMap();
        MapStatus ms=generateMapStatus(mCurrentLocation);
        baiduMap.animateMapStatus(MapStatusUpdateFactory.newMapStatus(ms));
        generateOverlay();
    }

    //获取点击位置地址信息
    private void searchLocationGeo(){
        if(mSearch==null){
            mSearch= GeoCoder.newInstance();
        }
        mSearch.setOnGetGeoCodeResultListener(this);
        mSearch.reverseGeoCode(new ReverseGeoCodeOption().location(mCurrentLocation));
    }

    //控制箭头动画
    private void setListener(){
        mSlidingUpPanelLayout.addPanelSlideListener(new SlidingUpPanelLayout.SimplePanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                super.onPanelSlide(panel, slideOffset);

                arrow.setRotation(slideOffset * (-180));
            }
        });
    }

    private void initViewPager(){
        final Fragment[] fragments=new Fragment[]{new FoodSearchFragment(),new BusSearchFragment()};
        final String[] titles=new String[]{"美食","交通"};
        mViewPager.setAdapter(new FragmentStatePagerAdapter(getSupportFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return fragments[position];
            }

            @Override
            public int getCount() {
                return 2;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return titles[position];
            }
        });

        slidingTabStrip.setCustomColorizer(new SlidingTabStrip.TabColorizer() {
            @Override
            public int getIndicatorColor() {
                return getResources().getColor(android.R.color.holo_blue_bright);
            }

            @Override
            public int getSelectedTitleColor() {
                return getResources().getColor(android.R.color.holo_blue_bright);
            }

            @Override
            public int getNormalTitleColor() {
                return getResources().getColor(android.R.color.black);
            }
        });
        slidingTabStrip.setViewPager(mViewPager);
        slidingTabStrip.notifyIndicatorColorChanged();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        baiduMap.clear();
        mMapView.onDestroy();
    }

    public MapStatus generateMapStatus(LatLng location){
        return new MapStatus.Builder().overlook(0).zoom(18).rotate(0).target(location).build();
    }

    private void generateOverlay(){
        BitmapDescriptor bd= BitmapDescriptorFactory.fromResource(R.drawable.icon_gcoding);
        MarkerOptions mo=new MarkerOptions().position(mCurrentLocation).icon(bd).zIndex(9);
        mo.animateType(MarkerOptions.MarkerAnimateType.drop);
        mCurrentMarker= (Marker) baiduMap.addOverlay(mo);
        bd.recycle();
    }

    @Override
    public void onGetGeoCodeResult(GeoCodeResult geoCodeResult) {

    }

    @Override
    public void onGetReverseGeoCodeResult(ReverseGeoCodeResult reverseGeoCodeResult) {
        //用到这个,反向
        if(reverseGeoCodeResult==null){
            mLocationDetail.setText("此地址无信息");
        }else{
            mLocationDetail.setText(reverseGeoCodeResult.getAddress());
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
       switch (item.getItemId()){
           case android.R.id.home:
               onBackPressed();
       }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
