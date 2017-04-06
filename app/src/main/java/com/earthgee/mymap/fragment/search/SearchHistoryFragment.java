package com.earthgee.mymap.fragment.search;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.earthgee.mymap.R;
import com.earthgee.mymap.SearchActivity;
import com.earthgee.mymap.fragment.AbsBaseFragment;
import com.earthgee.mymap.util.DividerItemDecoration;
import com.earthgee.mymap.util.HistoryManager;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by earthgee on 16/3/26.
 */
public class SearchHistoryFragment extends AbsBaseFragment implements SearchHistoryAdapter.OnItemClickListener{

    @InjectView(R.id.recycler_view)
    RecyclerView mHistoryView;

    private List<String> historys;
    private HistoryManager manager;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_search_history;
    }

    @Override
    protected void init(Bundle savedInstanceState, View contentView) {
        ButterKnife.inject(this,contentView);
        manager=new HistoryManager(getActivity());
        historys=manager.getHistory();
        mHistoryView.setLayoutManager(new LinearLayoutManager(getActivity()));
        SearchHistoryAdapter adapter=new SearchHistoryAdapter(getActivity(),historys);
        adapter.setmOnItemClickListener(this);
        mHistoryView.setAdapter(adapter);
        mHistoryView.addItemDecoration(new DividerItemDecoration(getActivity()));
        setFragmentStatus(FRAGMENT_STATUS_SUCCESS);
    }

    public void addHistory(String history){
        if(historys==null){
            historys=new ArrayList<>();
        }
        historys.add(history);
    }

//    @Override
//    public void onSaveInstanceState(Bundle outState) {
//        super.onSaveInstanceState(outState);
//        manager.saveHistory(historys);
//    }


    @Override
    public void onStop() {
        super.onStop();
        manager.saveHistory(historys);
    }

    @Override
    public void onItemClicked(int position) {
        ((SearchActivity)getActivity()).onSearch(historys.get(position));
    }
}
