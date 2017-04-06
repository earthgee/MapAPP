package map_test;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.RadioButton;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapView;
import com.earthgee.mymap.R;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by earthgee on 2016/2/8.
 */
public class LayersDemo extends Activity{

    @InjectView(R.id.bmapView)
    MapView mMapView;

    private BaiduMap mBaiduMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_layers);
        ButterKnife.inject(this);
        mBaiduMap=mMapView.getMap();
    }

    public void setMapMode(View view){
        boolean checked=((RadioButton)view).isChecked();
        switch (view.getId()){
            case R.id.normal:
                if(checked){
                    mBaiduMap.setMapType(BaiduMap.MAP_TYPE_NORMAL);
                }
                break;
            case R.id.statellite:
                if(checked){
                    mBaiduMap.setMapType(BaiduMap.MAP_TYPE_SATELLITE);
                }
                break;
            default:
                break;
        }
    }

    public void setTraffic(View view){
        mBaiduMap.setTrafficEnabled(((CheckBox)view).isChecked());
    }

    public void setBaiduHeatMap(View view){
        mBaiduMap.setBaiduHeatMapEnabled(((CheckBox)view).isChecked());
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





