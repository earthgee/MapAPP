package map_test;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.model.LatLng;
import com.earthgee.mymap.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by earthgee on 2016/2/8.
 */
public class MapControlDemo extends Activity implements View.OnClickListener{

    @InjectView(R.id.bmapView)
    MapView mMapView;
    @InjectView(R.id.state)
    TextView mStateText;

    private BaiduMap mBaiduMap;

    private LatLng currentPt;

    @InjectView(R.id.zoombutton)
    Button zoomButton;
    @InjectView(R.id.rotatebutton)
    Button rotateButton;
    @InjectView(R.id.overlookbutton)
    Button overlookButton;
    @InjectView(R.id.savescreen)
    Button saveButton;

    private String touchType;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_control);
        ButterKnife.inject(this);
        initListener();
    }

    private void initListener(){
        mBaiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                touchType="单击";
                currentPt=latLng;
                updateMapState();
            }

            @Override
            public boolean onMapPoiClick(MapPoi mapPoi) {
                return false;
            }
        });

        mBaiduMap.setOnMapLongClickListener(new BaiduMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng latLng) {
                touchType = "长按";
                currentPt = latLng;
                updateMapState();
            }
        });

        mBaiduMap.setOnMapDoubleClickListener(new BaiduMap.OnMapDoubleClickListener() {
            @Override
            public void onMapDoubleClick(LatLng latLng) {
                touchType = "双击";
                currentPt = latLng;
                updateMapState();
            }
        });

        mBaiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus) {
                updateMapState();
            }

            @Override
            public void onMapStatusChange(MapStatus mapStatus) {
                updateMapState();
            }

            @Override
            public void onMapStatusChangeFinish(MapStatus mapStatus) {
                updateMapState();
            }
        });

        zoomButton.setOnClickListener(this);
        rotateButton.setOnClickListener(this);
        overlookButton.setOnClickListener(this);
        saveButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.zoombutton:
                performZoom();
                break;
            case R.id.rotatebutton:
                performRotate();
                break;
            case R.id.overlookbutton:
                performOverLook();
                break;
            case R.id.savescreen:
                break;
        }
    }

    private void performZoom(){
        EditText t= (EditText) findViewById(R.id.zoomlevel);
        float zoomLevel=Float.parseFloat(t.getText().toString());
        MapStatusUpdate u= MapStatusUpdateFactory.zoomTo(zoomLevel);
        mBaiduMap.animateMapStatus(u);
    }

    private void performRotate(){

    }

    private void performOverLook(){

    }

    private void updateMapState(){

    }
}





