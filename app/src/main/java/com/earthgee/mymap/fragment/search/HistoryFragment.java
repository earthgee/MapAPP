package com.earthgee.mymap.fragment.search;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.earthgee.mymap.R;
import com.earthgee.mymap.fragment.AbsBaseFragment;
import com.earthgee.mymap.util.SlidingTabStrip;

import butterknife.ButterKnife;
import butterknife.InjectView;

/**
 * Created by earthgee on 16/3/26.
 */
public class HistoryFragment extends AbsBaseFragment{

    @InjectView(R.id.tab_strip)
    SlidingTabStrip mTabStrip;
    @InjectView(R.id.viewpager)
    ViewPager mViewPager;

    @Override
    protected int getContentLayout() {
        return R.layout.fragment_history;
    }

    @Override
    protected void init(Bundle savedInstanceState, View contentView) {
        setFragmentStatus(FRAGMENT_STATUS_SUCCESS);
        ButterKnife.inject(this, contentView);

        final Fragment[] mHistorys=new Fragment[]{new SearchHistoryFragment()};
        final String[] titles=new String[]{"历史"};
        mViewPager.setAdapter(new FragmentPagerAdapter(getChildFragmentManager()) {
            @Override
            public Fragment getItem(int position) {
                return mHistorys[position];
            }

            @Override
            public int getCount() {
                return 1;
            }

            @Override
            public CharSequence getPageTitle(int position) {
                return titles[position];
            }
        });

        mTabStrip.setViewPager(mViewPager);
        mTabStrip.notifyIndicatorColorChanged();
    }

    public void addHistory(String history){
        ((SearchHistoryFragment)((FragmentPagerAdapter)mViewPager.getAdapter()).getItem(0)).addHistory(history);
    }
}
