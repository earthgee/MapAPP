package com.earthgee.mymap.fragment;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.earthgee.mymap.R;
import com.earthgee.mymap.SearchRoadDetailActivity;
import com.earthgee.mymap.adapter.FragmentAdapter;
import com.earthgee.mymap.entity.SearchParams;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by earthgee on 2016/2/12.
 */
public class AllRoadFragment extends AbsBaseFragment{
    @InjectView(R.id.e_tab_layout)
    TabLayout mTabLayout;
    @InjectView(R.id.e_view_pager)
    ViewPager mViewPager;
    @InjectView(R.id.e_tool_bar)
    Toolbar mToolBar;

    private BikingFragment bikingFragment;
    private BusFragment busFragment;
    private DriveFragment driveFragment;
    private WalkFragment walkFragment;

    private String startCity;
    private String endCity;
    private String startPosition;
    private String endPosition;

    private SearchParams mParams;
    private List<String> mPositionList;

    public void setSearchParams(List<String> searchParams){
        this.startCity=searchParams.get(0);
        this.endCity=searchParams.get(1);
        this.startPosition=searchParams.get(2);
        this.endPosition=searchParams.get(3);
        requestData();
    }

    public void setPositionData(List<String> list){
        mPositionList=list;
    }

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_all_route;
    }

    @Override
    protected void init(Bundle savedInstanceState, View contentView) {
        setFragmentStatus(FRAGMENT_STATUS_SUCCESS);
        ButterKnife.inject(this,contentView);
        initView();
    }

    private void initView(){
        ((SearchRoadDetailActivity)getActivity()).setSupportActionBar(mToolBar);

        List<String> titles=new ArrayList<>();
        titles.add("骑行");
        titles.add("公交");
        titles.add("驾驶");
        titles.add("步行");
        mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(0)));
        mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(1)));
        mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(2)));
        mTabLayout.addTab(mTabLayout.newTab().setText(titles.get(3)));

        List<Fragment> fragments=new ArrayList<>();

        bikingFragment=new BikingFragment();
        busFragment=new BusFragment();
        driveFragment=new DriveFragment();
        walkFragment=new WalkFragment();

        fragments.add(bikingFragment);
        fragments.add(busFragment);
        fragments.add(driveFragment);
        fragments.add(walkFragment);

        setRequestParamsToSubFragment(bikingFragment,mParams);
        setRequestParamsToSubFragment(busFragment,mParams);
        setRequestParamsToSubFragment(driveFragment,mParams);
        setRequestParamsToSubFragment(walkFragment,mParams);

        FragmentAdapter adapter=new FragmentAdapter(getChildFragmentManager(),fragments,titles);
        mViewPager.setAdapter(adapter);
        mTabLayout.setupWithViewPager(mViewPager);
        mTabLayout.setTabsFromPagerAdapter(adapter);
    }

    private void requestData(){
        mParams=new SearchParams(startCity,endCity,startPosition,endPosition);
    }

    private void setRequestParamsToSubFragment(AbsBaseRouteFragment mFragment,SearchParams params){
        mFragment.setRequestParams(params);
    }
}



