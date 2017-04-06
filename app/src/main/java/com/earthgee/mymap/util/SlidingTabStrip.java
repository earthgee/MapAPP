package com.earthgee.mymap.util;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

/**
 * Created by earthgee on 16/3/21.
 */
public class SlidingTabStrip extends HorizontalScrollView{

    private static int mTitleOffset=24;

    private SlidingTabLayout mSlidingTabLayout;

    private int mTabTextViewId;
    private int mTabViewLayoutId;

    private boolean mDistributeEvenly;

    private ViewPager.OnPageChangeListener mOnPageChangeListener;
    private OnCurrentTabClickListener mOnCurrentTabClickListener;

    private ViewPager mViewPager;
    private int mScrollState;

    public interface TabColorizer{
        int getIndicatorColor();
        int getSelectedTitleColor();
        int getNormalTitleColor();
    }

    public SlidingTabStrip(Context context) {
        this(context,null);
    }

    public SlidingTabStrip(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public SlidingTabStrip(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        setHorizontalScrollBarEnabled(false);
        setFillViewport(true);

        mTitleOffset= (int) (mTitleOffset*getResources().getDisplayMetrics().density);

        mSlidingTabLayout=new SlidingTabLayout(context);
        addView(mSlidingTabLayout, LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT);
    }

    public void setCustomColorizer(TabColorizer colorizer){
        mSlidingTabLayout.setCustomTabColorizer(colorizer);
    }

    public void setIndicatorColor(int color){
        mSlidingTabLayout.setSelectedIndicatorColor(color);
    }

    public void notifyIndicatorColorChanged(){
        mSlidingTabLayout.updateTitlesView();
    }

    public void setmDistributeEvenly(boolean mDistributeEvenly) {
        this.mDistributeEvenly = mDistributeEvenly;
    }

    public void setmTabTextViewId(int mTabViewLayoutId,int mTabTextViewId) {
        this.mTabViewLayoutId=mTabViewLayoutId;
        this.mTabTextViewId = mTabTextViewId;
    }

    public int getmTabTextViewId() {
        return mTabTextViewId;
    }

    public void setmOnPageChangeListener(ViewPager.OnPageChangeListener mOnPageChangeListener) {
        this.mOnPageChangeListener = mOnPageChangeListener;
    }

    public void setmOnCurrentTabClickListener(OnCurrentTabClickListener mOnCurrentTabClickListener) {
        this.mOnCurrentTabClickListener = mOnCurrentTabClickListener;
    }

    public void setViewPager(ViewPager viewPager){
        mSlidingTabLayout.removeAllViews();

        mViewPager=viewPager;
        if(mViewPager!=null){
            mViewPager.setOnPageChangeListener(new InternalViewPagerListener());
            populateTabStrip();
        }
    }

    private TextView createDefaultTabView(Context context){
        TextView textView=new TextView(context);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 12);
        textView.setTypeface(Typeface.DEFAULT_BOLD);
        textView.setAllCaps(true);
        textView.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.MATCH_PARENT));
        int padding= (int) (16*getResources().getDisplayMetrics().density);
        textView.setPadding(padding,padding,padding,padding);

        return textView;
    }

    private void populateTabStrip(){
        final PagerAdapter adapter=mViewPager.getAdapter();
        final OnClickListener tabClickListener=new TabClickListener();

        for(int i=0;i<adapter.getCount();i++){
            View tabView=null;
            TextView tabTextView=null;

            if(mTabViewLayoutId!=0){
                tabView= LayoutInflater.from(getContext()).inflate(mTabViewLayoutId,mSlidingTabLayout,false);
                tabTextView= (TextView) tabView.findViewById(mTabTextViewId);
            }

            if(tabView==null){
                tabView=createDefaultTabView(getContext());
            }

            if(tabTextView==null&&tabView instanceof TextView){
                tabTextView= (TextView) tabView;
            }

            if(tabTextView!=null){
                tabTextView.setText(adapter.getPageTitle(i));
            }

            tabView.setOnClickListener(tabClickListener);
            mSlidingTabLayout.addView(tabView);
            if(i==mViewPager.getCurrentItem()){
                tabView.setSelected(true);
            }
        }
    }

    @Override
    protected void onAttachedToWindow() {
        super.onAttachedToWindow();

        if(mViewPager!=null){
            scrollTabTo(mViewPager.getCurrentItem(),0);
        }
    }

    private class InternalViewPagerListener implements ViewPager.OnPageChangeListener{

        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            int tabStripChildCount=mSlidingTabLayout.getChildCount();

            if(tabStripChildCount==0||position<0||position>=tabStripChildCount){
                return;
            }

            mSlidingTabLayout.onPagerChanged(position,positionOffset);

            View view=mSlidingTabLayout.getChildAt(position);
            int extraOffset= view==null? (int) (view.getWidth() * positionOffset) :0;
            scrollTabTo(position,extraOffset);

            if(mOnPageChangeListener!=null){
                mOnPageChangeListener.onPageScrolled(position,positionOffset,positionOffsetPixels);
            }
        }

        @Override
        public void onPageSelected(int position) {
            if(mScrollState==ViewPager.SCROLL_STATE_IDLE){
                mSlidingTabLayout.onPagerChanged(position,0f);
                scrollTabTo(position,0);
            }

            for(int i=0;i<mSlidingTabLayout.getChildCount();i++){
                mSlidingTabLayout.getChildAt(i).setSelected(i==position);
            }

            if(mOnPageChangeListener!=null){
                mOnPageChangeListener.onPageSelected(position);
            }

        }

        @Override
        public void onPageScrollStateChanged(int state) {
            mScrollState=state;

            if(mOnPageChangeListener!=null){
                mOnPageChangeListener.onPageScrollStateChanged(state);
            }
        }
    }

    private void scrollTabTo(int position,int extraOffset){
        View selectedView=mSlidingTabLayout.getChildAt(position);

        if(selectedView!=null){
            int targetScrollX=selectedView.getLeft()+extraOffset;

            if(position>0&&extraOffset>0){
                targetScrollX-=mTitleOffset;
            }

            scrollTo(targetScrollX,0);
        }
    }

    private class TabClickListener implements OnClickListener{

        @Override
        public void onClick(View v) {
            for(int i=0;i<mSlidingTabLayout.getChildCount();i++){
                if(v==mSlidingTabLayout.getChildAt(i)){
                    if(mViewPager.getCurrentItem()!=i){
                        mViewPager.setCurrentItem(i);
                    }else if(mOnCurrentTabClickListener!=null){
                        mOnCurrentTabClickListener.onTabClick(i);
                    }
                    return;
                }
            }
        }
    }

    public interface OnCurrentTabClickListener{
        void onTabClick(int position);
    }
}
