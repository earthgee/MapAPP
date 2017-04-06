package com.earthgee.mymap;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.earthgee.mymap.adapter.FragmentAdapter;
import com.earthgee.mymap.adapter.MenuAdapter;
import com.earthgee.mymap.fragment.MapFragment;
import com.earthgee.mymap.fragment.SearchRoadFragment;
import com.earthgee.mymap.util.SlidingTabStrip;
import com.sport.SportActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;
import butterknife.InjectView;

public class MainActivity extends AppCompatActivity implements MenuAdapter.DrawerMenuClickListener {

    @InjectView(R.id.e_tool_bar)
    Toolbar mToolbar;
    @InjectView(R.id.e_drawer_layout)
    DrawerLayout mDrawerLayout;
    @InjectView(R.id.drawer_view)
    LinearLayout mDrawerView;
    @InjectView(R.id.e_view_pager)
    ViewPager mViewPager;

    //测试strip
    @InjectView(R.id.e_tab_strip)
    SlidingTabStrip mTabStrip;

    MapFragment mMapFragment;

    private ActionBarDrawerToggle mDrawerToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.inject(this);
        initView();
    }

    private void initView() {
        setSupportActionBar(mToolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mDrawerToggle = new ActionBarDrawerToggle(this, mDrawerLayout, mToolbar, R.string.drawer_open, R.string.drawer_close);
        mDrawerToggle.syncState();
        mDrawerLayout.setDrawerListener(mDrawerToggle);

        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleMenu();
            }
        });

        List<String> titles = new ArrayList<>();
        titles.add("地图");
        titles.add("寻路");

        mMapFragment = new MapFragment();
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(mMapFragment);
        fragments.add(new SearchRoadFragment());

        FragmentAdapter adapter = new FragmentAdapter(getSupportFragmentManager(), fragments, titles);
        mViewPager.setAdapter(adapter);
        mTabStrip.setViewPager(mViewPager);
        mTabStrip.notifyIndicatorColorChanged();

    }

    private void toggleMenu() {
        if (mDrawerLayout.isDrawerOpen(Gravity.RIGHT)) {
            mDrawerLayout.closeDrawer(Gravity.RIGHT);
        } else {
            mDrawerLayout.openDrawer(Gravity.RIGHT);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_search:
                mViewPager.setCurrentItem(1);
                break;
            case R.id.menu_map:
                mViewPager.setCurrentItem(0);
                break;
            case R.id.menu_search2:
                //进入搜索页面
                startActivityForResult(new Intent(this, SearchActivity.class), 1024);
                break;
            case android.R.id.home:
                mDrawerLayout.openDrawer(GravityCompat.START);
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        if (mDrawerLayout.isDrawerOpen(Gravity.RIGHT)) {
            mDrawerLayout.closeDrawer(Gravity.RIGHT);
        } else {
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1024 && resultCode == RESULT_OK) {
            String keyword = data.getStringExtra("keyword");
            //Log.d("keyword",keyword);
            if (mMapFragment != null) {
                mMapFragment.searchOnMap(keyword);
            }
        }
    }

    @Override
    public void onItemClicked(int position) {
        //点击position项菜单
        toggleMenu();
        //进行具体的跳转
        if (position == 0) {
            SportActivity.jumpToSportActivity(this);
        } else if (position == 1) {

        }
    }


}
