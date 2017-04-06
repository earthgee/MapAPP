package com.earthgee.mymap.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.earthgee.mymap.R;
import com.earthgee.mymap.adapter.RouteRecyclerAdapter;
import com.earthgee.mymap.entity.SearchParams;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by earthgee on 2016/2/12.
 */
public abstract class AbsBaseRouteFragment extends AbsBaseFragment implements OnGetRoutePlanResultListener{
    @InjectView(R.id.e_recycler_view)
    RecyclerView mRecyclerView;

    RoutePlanSearch mSearch=null;
    PlanNode stNode;
    PlanNode endNode;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_base_route;
    }

    @Override
    protected void init(Bundle savedInstanceState, View contentView) {
        ButterKnife.inject(this, contentView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mSearch=RoutePlanSearch.newInstance();
        mSearch.setOnGetRoutePlanResultListener(this);
    }

    protected void setRequestParams(SearchParams params){
        stNode=PlanNode.withCityNameAndPlaceName(params.getStartCity(),params.getStartPosition());
        endNode=PlanNode.withCityNameAndPlaceName(params.getEndCity(),params.getEndPosition());
    }

    @Override
    public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {

    }

    @Override
    public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {

    }

    @Override
    public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {

    }

    @Override
    public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {

    }

}
