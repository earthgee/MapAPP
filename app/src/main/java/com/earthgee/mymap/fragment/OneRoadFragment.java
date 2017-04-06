package com.earthgee.mymap.fragment;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.baidu.mapapi.search.core.RouteLine;
import com.earthgee.mymap.R;
import com.earthgee.mymap.SearchRoadDetailActivity;
import com.earthgee.mymap.adapter.OneRouteRecyclerAdapter;
import com.earthgee.mymap.util.DividerItemDecoration;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by earthgee on 2016/2/12.
 */
public class OneRoadFragment extends AbsBaseFragment {
    @InjectView(R.id.e_tool_bar)
    Toolbar mToolBar;
    @InjectView(R.id.e_recycler_view)
    RecyclerView mRecyclerView;
    @InjectView(R.id.e_btn_play)
    FloatingActionButton mBtnPlay;
    private OneRouteRecyclerAdapter mAdapter;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_one_route;
    }

    @Override
    protected void init(Bundle savedInstanceState, View contentView) {
        setFragmentStatus(FRAGMENT_STATUS_SUCCESS);
        ButterKnife.inject(this, contentView);

        final RouteLine routeLine = (RouteLine) getArguments().get("route_line");
        ((SearchRoadDetailActivity) getActivity()).setSupportActionBar(mToolBar);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(getActivity()));
        mAdapter=new OneRouteRecyclerAdapter(getActivity(),routeLine);
        mRecyclerView.setAdapter(mAdapter);

        mBtnPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((SearchRoadDetailActivity)getActivity()).showRoadDetailOnMap(routeLine);
            }
        });
    }
}
