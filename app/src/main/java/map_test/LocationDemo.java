package map_test;

import android.app.Activity;
import android.os.Bundle;
import android.widget.Button;

import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationConfiguration;
import com.earthgee.mymap.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by earthgee on 2016/2/8.
 */
public class LocationDemo extends Activity{

    @InjectView(R.id.button1)
    Button requestButton;
    @InjectView(R.id.bmapView)
    MapView mMapView;


    private MyLocationConfiguration.LocationMode mCurrentMode;
    private BitmapDescriptor mCurrentMarker;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location);
        ButterKnife.inject(this);
        mCurrentMode= MyLocationConfiguration.LocationMode.NORMAL;
        requestButton.setText("普通");


    }
}
