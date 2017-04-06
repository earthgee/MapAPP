package com.earthgee.mymap.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.Parcelable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;

import com.baidu.mapapi.search.route.BikingRoutePlanOption;
import com.baidu.mapapi.search.route.BikingRouteResult;
import com.baidu.mapapi.search.route.DrivingRoutePlanOption;
import com.baidu.mapapi.search.route.DrivingRouteResult;
import com.baidu.mapapi.search.route.OnGetRoutePlanResultListener;
import com.baidu.mapapi.search.route.PlanNode;
import com.baidu.mapapi.search.route.RoutePlanSearch;
import com.baidu.mapapi.search.route.TransitRoutePlanOption;
import com.baidu.mapapi.search.route.TransitRouteResult;
import com.baidu.mapapi.search.route.WalkingRoutePlanOption;
import com.baidu.mapapi.search.route.WalkingRouteResult;
import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.earthgee.mymap.R;
import com.earthgee.mymap.SearchRoadDetailActivity;
import com.earthgee.mymap.adapter.SearchResultRecyclerAdapter;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by earthgee on 2016/2/7.
 */
public class SearchRoadFragment extends AbsBaseFragment implements OnGetRoutePlanResultListener,OnGetSuggestionResultListener,SearchResultRecyclerAdapter.OnItemClickListener {
    @InjectView(R.id.e_til_start)
    TextInputLayout mStart;
    @InjectView(R.id.e_til_end)
    TextInputLayout mEnd;
    @InjectView(R.id.btn_search_road)
    FloatingActionButton mSearchRoad;
    @InjectView(R.id.search_road_listview)
    RecyclerView mSearchRoadListview;

    private SuggestionSearch mSuggestSearch;
    private ArrayList<String> mSuggests;
    private ArrayList<String> mCitys;
    private SearchResultRecyclerAdapter mAdapter;

    //private RoutePlanSearch mSearchManager;

    private String mStartCity;
    private String mEndCity;

    //private WalkingRouteResult mWalkingRouteResult;
    //private DrivingRouteResult mDrivingRouteResult;
    //private TransitRouteResult mTransitRouteResult;
    //private BikingRouteResult mBikingRouteResult;
    //private int jishujun = 0;

    private static final int MODE_START=0;
    private static final int MODE_END=1;
    private int CURRENT_MODE=MODE_START;

//    private Handler mHandler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            jishujun++;
//            if (jishujun == 4) {
//                //跳转到下个页面
//                Intent intent = new Intent(getActivity(), SearchRoadDetailActivity.class);
//                Parcelable[] data = new Parcelable[]{mWalkingRouteResult, mBikingRouteResult, mDrivingRouteResult, mTransitRouteResult};
//                intent.putExtra("data", data);
//                startActivity(intent);
//            }
//        }
//    };

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_search_road;
    }

    @Override
    protected void init(Bundle savedInstanceState, View contentView) {
        ButterKnife.inject(this, contentView);
        setFragmentStatus(FRAGMENT_STATUS_SUCCESS);
        mSuggestSearch=SuggestionSearch.newInstance();
        mSuggestSearch.setOnGetSuggestionResultListener(this);
        //mSearchManager = RoutePlanSearch.newInstance();
        //mSearchManager.setOnGetRoutePlanResultListener(this);
        mSuggests=new ArrayList<>();
        mCitys=new ArrayList<>();
        mAdapter=new SearchResultRecyclerAdapter(getActivity(),mSuggests);
        mAdapter.setOnItemClickListener(this);
        mSearchRoadListview.setLayoutManager(new LinearLayoutManager(getActivity()));
        mSearchRoadListview.setAdapter(mAdapter);
        initView();
        initListener();
    }

    private void initView() {
        mStart.setHint("请输入起点");
        mEnd.setHint("请输入终点");
        mSearchRoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkInput()) {
                    doSearch();
                }
            }
        });
    }

    private void initListener(){
        mStart.getEditText().addTextChangedListener(new OnSearchChangedListener1());
        mEnd.getEditText().addTextChangedListener(new OnSearchChangedListener2());
    }

    private boolean checkInput() {
        if (TextUtils.isEmpty(mStart.getEditText().getText()) || TextUtils.isEmpty(mEnd.getEditText().getText())) {
            Snackbar.make(getView(),"请输入起点和终点哦！",Snackbar.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    private void doSearch() {
        String start = mStart.getEditText().getText().toString();
        String end = mEnd.getEditText().getText().toString();

        PlanNode stNode = PlanNode.withCityNameAndPlaceName(mStartCity, start);
        PlanNode endNode = PlanNode.withCityNameAndPlaceName(mEndCity, end);

        Intent intent=new Intent(getActivity(),SearchRoadDetailActivity.class);
        intent.putExtra("startCity",mStartCity);
        intent.putExtra("endCity",mEndCity);
        intent.putExtra("start",start);
        intent.putExtra("end",end);
        startActivity(intent);
    }

    //步行路线
    @Override
    public void onGetWalkingRouteResult(WalkingRouteResult walkingRouteResult) {
        //城市错误的情况重构时修改
        //mWalkingRouteResult = walkingRouteResult;
        //mHandler.sendEmptyMessage(0);
    }

    //换乘路线
    @Override
    public void onGetTransitRouteResult(TransitRouteResult transitRouteResult) {
        //mTransitRouteResult = transitRouteResult;
        //mHandler.sendEmptyMessage(1);
    }

    //驾车路线
    @Override
    public void onGetDrivingRouteResult(DrivingRouteResult drivingRouteResult) {
        //mDrivingRouteResult = drivingRouteResult;
        //mHandler.sendEmptyMessage(2);
    }

    //骑行路线
    @Override
    public void onGetBikingRouteResult(BikingRouteResult bikingRouteResult) {
        //mBikingRouteResult = bikingRouteResult;
        //mHandler.sendEmptyMessage(3);
    }

    @Override
    public void onGetSuggestionResult(SuggestionResult suggestionResult) {
        if (suggestionResult == null || suggestionResult.getAllSuggestions() == null) {
            return;
        }

        mSuggests.clear();
        mCitys.clear();
        for (SuggestionResult.SuggestionInfo info : suggestionResult.getAllSuggestions()) {
            if (info.key != null) {
                mSuggests.add(info.key);
                mCitys.add(info.city);
            }
        }

        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onItemClick(View view, int position) {
        if(CURRENT_MODE==MODE_START){
            mStart.getEditText().setText(mSuggests.get(position));
            mStartCity=mCitys.get(position);
        }else{
            mEnd.getEditText().setText(mSuggests.get(position));
            mEndCity=mCitys.get(position);
        }
    }

    //搜索输入的监听器
    private class OnSearchChangedListener1 implements TextWatcher {


        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() == 0) {
                return;
            }

            CURRENT_MODE=MODE_START;
            mSuggestSearch.requestSuggestion((new SuggestionSearchOption()).keyword(s.toString()).city(""));
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }


    private class OnSearchChangedListener2 implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() == 0) {
                return;
            }

            CURRENT_MODE=MODE_END;
            mSuggestSearch.requestSuggestion((new SuggestionSearchOption()).keyword(s.toString()).city(""));
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

}
