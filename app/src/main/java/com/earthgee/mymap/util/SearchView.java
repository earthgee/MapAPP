package com.earthgee.mymap.util;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.earthgee.mymap.adapter.SearchResultRecyclerAdapter;

/**
 * Created by earthgee on 2016/2/21.
 */
public class SearchView extends ViewGroup implements OnGetSuggestionResultListener,SearchResultRecyclerAdapter.OnItemClickListener{


    public SearchView(Context context) {
        super(context);
    }

    @Override
    public void onGetSuggestionResult(SuggestionResult suggestionResult) {

    }

    @Override
    public void onItemClick(View view, int position) {

    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }
}
