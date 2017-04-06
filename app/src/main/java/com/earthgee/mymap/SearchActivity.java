package com.earthgee.mymap;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.earthgee.mymap.fragment.search.HistoryFragment;
import com.quinny898.library.persistentsearch.SearchBox;
import com.quinny898.library.persistentsearch.SearchResult;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by earthgee on 16/3/25.
 */
public class SearchActivity extends AbsBaseActivity implements OnGetSuggestionResultListener{

    @InjectView(R.id.searchbox)
    SearchBox mSearchBox;

    private HistoryFragment historyFragment;

    private SuggestionSearch mSuggestSearch;

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        }
    };

    @Override
    protected int getLayoutId() {
        return R.layout.activity_search;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ButterKnife.inject(this);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                openSearch();
            }
        }, 500);

        mSearchBox.setLogoText("搜索");
        mSuggestSearch=SuggestionSearch.newInstance();
        mSuggestSearch.setOnGetSuggestionResultListener(this);
        mSearchBox.getSearch().addTextChangedListener(new OnTextChangeListener());
        mSearchBox.setSearchListener(MyListener);

       historyFragment=new HistoryFragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.content,historyFragment).commit();
    }

    private class OnTextChangeListener implements TextWatcher{

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if(s.length()==0){
                return;
            }

            mSuggestSearch.requestSuggestion(new SuggestionSearchOption().keyword(s.toString()).city(""));
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    private SearchBox.SearchListener MyListener=new SearchBox.SearchListener() {
        @Override
        public void onSearchOpened() {

        }

        @Override
        public void onSearchCleared() {

        }

        @Override
        public void onSearchClosed() {
            mSearchBox.hideCircularly(SearchActivity.this);
        }

        @Override
        public void onSearchTermChanged() {

        }

        @Override
        public void onSearch(String result) {
            historyFragment.addHistory(result);
            //在这里跳转回首页去展示overlay
            Intent intent=new Intent();
            intent.putExtra("keyword",result);
            setResult(RESULT_OK, intent);
            finish();
        }
    };

    public void onSearch(String result){
        //关闭软键盘
        InputMethodManager manager= (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        manager.hideSoftInputFromWindow(mSearchBox.getWindowToken(),0);
        //在这里跳转回首页去展示overlay
        Intent intent=new Intent();
        intent.putExtra("keyword",result);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.menu_search,menu);
        return true;
    }

    private void openSearch(){
        mSearchBox.revealFromMenuItem(R.id.search_item, this);

    }

    @Override
    public void onGetSuggestionResult(SuggestionResult suggestionResult) {
        if (suggestionResult == null || suggestionResult.getAllSuggestions() == null) {
            return;
        }

        for(SuggestionResult.SuggestionInfo info:suggestionResult.getAllSuggestions()){
            if(info.key!=null){
                SearchResult result=new SearchResult(info.key,null);
                mSearchBox.addSearchable(result);
            }
        }
        mSearchBox.updateResults();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.search_item:
                openSearch();
                break;
            case android.R.id.home:
                onBackPressed();
                break;
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSuggestSearch.destroy();
    }

    public void mic(View view) {
        // Is causing crashes. Abandon it.
        //mSearchBox.micClick(this);
        // crash? just ignore it.
        try {
            mSearchBox.micClick(this);
        } catch (Exception e) {
            // ignored
        }
    }
}
