package com.earthgee.mymap;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;

import com.baidu.mapapi.search.sug.OnGetSuggestionResultListener;
import com.baidu.mapapi.search.sug.SuggestionResult;
import com.baidu.mapapi.search.sug.SuggestionSearch;
import com.baidu.mapapi.search.sug.SuggestionSearchOption;
import com.earthgee.mymap.adapter.SearchResultRecyclerAdapter;

import java.util.ArrayList;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by earthgee on 2016/2/21.
 */
public class SearchPoiActivity extends AppCompatActivity implements OnGetSuggestionResultListener,SearchResultRecyclerAdapter.OnItemClickListener{

    @InjectView(R.id.dialog_search_textinputlayout)
    TextInputLayout mSearchInput;
    @InjectView(R.id.dialog_search_data)
    RecyclerView mSearchContent;

    private SuggestionSearch mSuggestSearch;
    private ArrayList<String> suggest;
    private SearchResultRecyclerAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_search);
        ButterKnife.inject(this);
        mSuggestSearch=SuggestionSearch.newInstance();
        mSuggestSearch.setOnGetSuggestionResultListener(this);
        suggest=new ArrayList<>();
        initView();
    }

    private void initView(){
        mSearchInput.setHint("请输入要搜索的地址");
        EditText mSearchEdit=mSearchInput.getEditText();

        mSearchEdit.addTextChangedListener(new OnSearchChangedListener());
        mAdapter=new SearchResultRecyclerAdapter(this,suggest);
        mAdapter.setOnItemClickListener(this);
        mSearchContent.setLayoutManager(new LinearLayoutManager(this));
        mSearchContent.setAdapter(mAdapter);


    }

    @Override
    public void onGetSuggestionResult(SuggestionResult suggestionResult) {
        if (suggestionResult == null || suggestionResult.getAllSuggestions() == null) {
            return;
        }

        suggest.clear();
        for (SuggestionResult.SuggestionInfo info : suggestionResult.getAllSuggestions()) {
            if (info.key != null) {
                suggest.add(info.key);
            }
        }

        mAdapter.notifyDataSetChanged();
    }

    //搜索输入的监听器
    private class OnSearchChangedListener implements TextWatcher {

        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {
            if (s.length() == 0) {
                return;
            }

            mSuggestSearch.requestSuggestion((new SuggestionSearchOption()).keyword(s.toString()).city(""));
        }

        @Override
        public void afterTextChanged(Editable s) {

        }
    }

    @Override
    public void onItemClick(View view, int position) {
        //点击的目的地项
        String result=suggest.get(position);

        Intent intent=new Intent();
        intent.putExtra("position",result);
        setResult(RESULT_OK,intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSuggestSearch.destroy();
    }
}
