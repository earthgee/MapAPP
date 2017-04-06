package com.earthgee.mymap.fragment.searchnearby;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.earthgee.mymap.R;
import com.earthgee.mymap.fragment.AbsBaseFragment;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by earthgee on 16/3/24.
 */
public abstract class AbsSearchFragment<T extends AbsSearchAdapter> extends AbsBaseFragment implements SwipeRefreshLayout.OnRefreshListener{

    @InjectView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout mSwipeRefreshLayout;
    @InjectView(R.id.recycler_view)
    RecyclerView mRecycleView;

    private boolean isRefreshing=false;
    private boolean isLoadingMore=false;

    protected int mCurrentPageNum=0;

    T adapter;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_search_nearby;
    }

    @Override
    protected void init(Bundle savedInstanceState, View contentView) {
        setFragmentStatus(FRAGMENT_STATUS_SUCCESS);
        ButterKnife.inject(this, contentView);

        mRecycleView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mSwipeRefreshLayout.setColorSchemeResources(android.R.color.holo_blue_bright,android.R.color.holo_green_light,android.R.color.holo_orange_light,android.R.color.holo_red_light);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        mRecycleView.setOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if(!isRefreshing&&!isLoadingMore&&dy>0){
                    int lastVisiblePosition=((LinearLayoutManager)recyclerView.getLayoutManager()).findLastVisibleItemPosition();
                    if(lastVisiblePosition+1==adapter.getItemCount()){
                        loadMore();
                    }
                }
            }
        });

        initAdapter();
        initData();
    }

    private void initAdapter(){
        adapter=getAdapter();
        mRecycleView.setAdapter(adapter);
    }

    protected abstract void initData();

    protected abstract T getAdapter();

    @Override
    public void onRefresh() {
        if(!isRefreshing&&!isLoadingMore){
            mCurrentPageNum=0;
            isRefreshing=true;
            mSwipeRefreshLayout.setRefreshing(true);
            doRefresh();
        }
    }

    protected abstract void doRefresh();

    protected void loadMore(){
        isLoadingMore=true;
        mSwipeRefreshLayout.setRefreshing(true);
    }

    protected void onLoadDataSuccess(){
        mSwipeRefreshLayout.setRefreshing(false);
        isRefreshing=false;
        isLoadingMore=false;
    }
}
