package map_test;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;

/**
 * Created by earthgee on 2016/2/8.
 */
public class BaseMapDemo extends Activity{

    private MapView mMapView;
    private BaiduMap mBaiduMap;

    //first location then show map
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent=getIntent();
        if(intent.hasExtra("x")&&intent.hasExtra("y")){
            Bundle b=intent.getExtras();
            LatLng p=new LatLng(b.getDouble("y"),b.getDouble("x"));
            mMapView=new MapView(this,new BaiduMapOptions().mapStatus(new MapStatus.Builder().target(p).build()));
        }else{
            mMapView=new MapView(this,new BaiduMapOptions());
        }
        setContentView(mMapView);
        mBaiduMap=mMapView.getMap();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

}




