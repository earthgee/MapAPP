package map_test;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

import com.baidu.mapapi.map.BaiduMapOptions;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.SupportMapFragment;
import com.earthgee.mymap.R;

/**
 * Created by earthgee on 2016/2/8.
 */
public class MapFragmentDemo extends FragmentActivity{

    SupportMapFragment map;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        MapStatus ms=new MapStatus.Builder().overlook(-45).zoom(15).build();
        BaiduMapOptions bo=new BaiduMapOptions().mapStatus(ms).compassEnabled(false).zoomControlsEnabled(false);
        map=SupportMapFragment.newInstance(bo);
        FragmentManager manager=getSupportFragmentManager();
        manager.beginTransaction().add(R.id.map,map).commit();
    }


}





