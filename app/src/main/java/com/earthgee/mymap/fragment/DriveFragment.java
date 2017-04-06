package com.earthgee.mymap.fragment;

import android.os.Bundle;
import android.view.View;

import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.earthgee.mymap.SearchRoadDetailActivity;
import com.earthgee.mymap.adapter.route.DriveRecyclerAdapter;

/**
 * Created by earthgee on 2016/2/12.
 */
public class DriveFragment extends AbsBaseRouteFragment implements DriveRecyclerAdapter.OnItemClickListener {

    private DriveRecyclerAdapter mAdapter;
    private DrivingRouteResult drivingRouteResult;

    @Override
    protected void init(Bundle savedInstanceState, View contentView) {
        super.init(savedInstanceState, contentView);
        mSearch.drivingSearch(new DrivingRoutePlanOption().from(stNode).to(endNode));
    }

    @Override
    public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {
        super.onGetDrivingRouteResult(drivingRouteResult);

        if(drivingRouteResult.error!= SearchResult.ERRORNO.NO_ERROR){
            setFragmentStatus(FRAGMENT_STATUC_ERROR);
            return;
        }

        this.drivingRouteResult=drivingRouteResult;
        setFragmentStatus(FRAGMENT_STATUS_SUCCESS);
        mAdapter=new DriveRecyclerAdapter(getActivity(),drivingRouteResult.getRouteLines());
        mAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onItemClick(View view, int position) {
        ((SearchRoadDetailActivity)getActivity()).showOneRoadDetail(drivingRouteResult.getRouteLines().get(position));
    }
}
