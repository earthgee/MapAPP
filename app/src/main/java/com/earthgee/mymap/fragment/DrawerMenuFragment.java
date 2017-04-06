package com.earthgee.mymap.fragment;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ListView;

import com.earthgee.mymap.MainActivity;
import com.earthgee.mymap.R;
import com.earthgee.mymap.adapter.MenuAdapter;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by earthgee on 2016/3/20.
 * 侧边选项fragment
 */
public class DrawerMenuFragment extends AbsBaseFragment{

    @InjectView(R.id.lv_drawer_menu)
    RecyclerView mListView;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_drawer_menu;
    }

    @Override
    protected void init(Bundle savedInstanceState, View contentView) {
        ButterKnife.inject(this, contentView);
        setFragmentStatus(FRAGMENT_STATUS_SUCCESS);
        MenuAdapter menuAdapter=new MenuAdapter(getActivity());
        menuAdapter.setMenuClickListener((MainActivity)getActivity());
        mListView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mListView.setAdapter(menuAdapter);
    }
}
