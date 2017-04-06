package com.earthgee.mymap.fragment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.WalkingRouteLine;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.earthgee.mymap.SearchRoadDetailActivity;
import com.earthgee.mymap.adapter.RouteRecyclerAdapter;
import com.earthgee.mymap.adapter.SearchResultRecyclerAdapter;
import com.earthgee.mymap.adapter.route.WalkRecyclerAdapter;
import com.earthgee.mymap.entity.SearchParams;

import java.util.List;

/**
 * Created by earthgee on 2016/2/12.
 */
public class WalkFragment extends AbsBaseRouteFragment implements WalkRecyclerAdapter.OnItemClickListener {

    private WalkRecyclerAdapter mAdapter;
    private WalkingRouteResult walkingRouteResult;

    @Override
    protected void init(Bundle savedInstanceState, View contentView) {
        super.init(savedInstanceState, contentView);
        mSearch.walkingSearch(new WalkingRoutePlanOption().from(stNode).to(endNode));
    }

    @Override
    public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {
        super.onGetWalkingRouteResult(walkingRouteResult);

        if(walkingRouteResult.error!= SearchResult.ERRORNO.NO_ERROR){
            setFragmentStatus(FRAGMENT_STATUC_ERROR);
            return;
        }

        this.walkingRouteResult=walkingRouteResult;
        setFragmentStatus(FRAGMENT_STATUS_SUCCESS);
        mAdapter=new WalkRecyclerAdapter(getActivity(),walkingRouteResult.getRouteLines(),walkingRouteResult.getTaxiInfo());
        mAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onItemClick(View view, int position) {
        ((SearchRoadDetailActivity)getActivity()).showOneRoadDetail(walkingRouteResult.getRouteLines().get(position));
    }
}
