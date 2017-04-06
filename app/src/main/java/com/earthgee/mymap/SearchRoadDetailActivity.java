package com.earthgee.mymap;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;

import com.baidu.mapapi.search.core.RouteLine;
import com.earthgee.mymap.fragment.AllRoadFragment;
import com.earthgee.mymap.fragment.OneRoadFragment;
import com.earthgee.mymap.fragment.RoadMapFragment;

import java.util.ArrayList;

/**
 * Created by earthgee on 2016/2/12.
 */
public class SearchRoadDetailActivity extends AppCompatActivity{
    private FragmentManager manager;
    private AllRoadFragment mAllRoadFragment;
    private OneRoadFragment mOneRoadFragment;
    private RoadMapFragment mRoadMapFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_road);
        manager=getSupportFragmentManager();
        showAllRoadDetail();
    }

    private void showAllRoadDetail(){
        FragmentTransaction transaction=manager.beginTransaction();
        mAllRoadFragment=new AllRoadFragment();
        ArrayList<String> list=new ArrayList<>();
        list.add(getIntent().getStringExtra("startCity"));
        list.add(getIntent().getStringExtra("endCity"));
        list.add(getIntent().getStringExtra("start"));
        list.add(getIntent().getStringExtra("end"));
        transaction.replace(R.id.container,mAllRoadFragment).commit();
        mAllRoadFragment.setSearchParams(list);
    }

    public void showOneRoadDetail(RouteLine routeLine){
        FragmentTransaction transaction=manager.beginTransaction();
        mOneRoadFragment=new OneRoadFragment();
        Bundle bundle=new Bundle();
        bundle.putParcelable("route_line",routeLine);
        mOneRoadFragment.setArguments(bundle);
        transaction.replace(R.id.container,mOneRoadFragment).addToBackStack("first").commit();
    }

    public void showRoadDetailOnMap(RouteLine routeLine){
        FragmentTransaction transaction=manager.beginTransaction();
        mRoadMapFragment=new RoadMapFragment();
        Bundle bundle=new Bundle();
        bundle.putParcelable("route_line",routeLine);
        mRoadMapFragment.setArguments(bundle);
        transaction.replace(R.id.container,mRoadMapFragment).addToBackStack("second").commit();
    }

}
