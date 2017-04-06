package com.earthgee.mymap.fragment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.BikingRoutePlanOption;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.TransitRouteLine;
import com.baidu.mapapi.search.route.TransitRoutePlanOption;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.earthgee.mymap.R;
import com.earthgee.mymap.SearchRoadDetailActivity;
import com.earthgee.mymap.adapter.RouteRecyclerAdapter;
import com.earthgee.mymap.adapter.SearchResultRecyclerAdapter;
import com.earthgee.mymap.adapter.route.BusRecyclerAdapter;
import com.earthgee.mymap.entity.SearchParams;

import java.util.List;

/**
 * Created by earthgee on 2016/2/12.
 */
public class BusFragment extends AbsBaseRouteFragment implements BusRecyclerAdapter.OnItemClickListener {

    private BusRecyclerAdapter mAdapter;
    private TransitRouteResult transitRouteResult;

    @Override
    protected void init(Bundle savedInstanceState, View contentView) {
        super.init(savedInstanceState, contentView);
        mSearch.transitSearch(new TransitRoutePlanOption().from(stNode).city(stNode.getCity()).to(endNode));
    }

    @Override
    public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {
        super.onGetTransitRouteResult(transitRouteResult);

        if(transitRouteResult.error!= SearchResult.ERRORNO.NO_ERROR){
            setFragmentStatus(FRAGMENT_STATUC_ERROR);
            return;
        }

        this.transitRouteResult=transitRouteResult;
        setFragmentStatus(FRAGMENT_STATUS_SUCCESS);
        mAdapter=new BusRecyclerAdapter(getActivity(),transitRouteResult.getRouteLines());
        mAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onItemClick(View view, int position) {
        ((SearchRoadDetailActivity)getActivity()).showOneRoadDetail(transitRouteResult.getRouteLines().get(position));
    }



}
