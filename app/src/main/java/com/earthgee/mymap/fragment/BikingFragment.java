package com.earthgee.mymap.fragment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.baidu.mapapi.search.core.SearchResult;
import com.baidu.mapapi.search.route.BikingRouteLine;
import com.baidu.mapapi.search.route.BikingRoutePlanOption;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.earthgee.mymap.R;
import com.earthgee.mymap.SearchRoadDetailActivity;
import com.earthgee.mymap.adapter.RouteRecyclerAdapter;
import com.earthgee.mymap.adapter.SearchResultRecyclerAdapter;
import com.earthgee.mymap.adapter.route.BikingRecyclerAdapter;
import com.earthgee.mymap.entity.SearchParams;

import java.util.List;

import butterknife.ButterKnife;

/**
 * Created by earthgee on 2016/2/12.
 */
public class BikingFragment extends AbsBaseRouteFragment implements BikingRecyclerAdapter.OnItemClickListener {

    private BikingRecyclerAdapter mAdapter;
    private BikingRouteResult bikingRouteResult;

    @Override
    protected void init(Bundle savedInstanceState, View contentView) {
        super.init(savedInstanceState, contentView);
        mSearch.bikingSearch(new BikingRoutePlanOption().from(stNode).to(endNode));
    }

    @Override
    public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {
        super.onGetBikingRouteResult(bikingRouteResult);

        if(bikingRouteResult.error!= SearchResult.ERRORNO.NO_ERROR){
            setFragmentStatus(FRAGMENT_STATUC_ERROR);
            return;
        }

        this.bikingRouteResult=bikingRouteResult;
        setFragmentStatus(FRAGMENT_STATUS_SUCCESS);
        //mAdapter=new RouteRecyclerAdapter(getActivity(),bikingRouteResult);
        mAdapter=new BikingRecyclerAdapter(getActivity(),bikingRouteResult.getRouteLines());
        mAdapter.setOnItemClickListener(this);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onItemClick(View view, int position) {
        ((SearchRoadDetailActivity)getActivity()).showOneRoadDetail(bikingRouteResult.getRouteLines().get(position));
    }
}
