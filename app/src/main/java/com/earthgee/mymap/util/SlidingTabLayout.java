package com.earthgee.mymap.util;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;
import android.view.ViewParent;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.earthgee.mymap.R;

/**
 * Created by earthgee on 16/3/21.
 */
public class SlidingTabLayout extends LinearLayout{

    private int mDefaultBottomBorderColor;

    private SlidingTabStrip.TabColorizer mCustomColorizer;
    private static SimpleTabColorizer simpleTabColorizer;

    private final int mBottomBorderThickness;
    private final Paint mBottomBorderPaint;

    private int mPrimaryColor;

    private int mSelectedPosition;
    private float mPositionOffset;

    public SlidingTabLayout(Context context){
        this(context,null);
    }

    public SlidingTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        setWillNotDraw(false);

        float density=getResources().getDisplayMetrics().density;

        TypedValue typedValue=new TypedValue();
        context.getTheme().resolveAttribute(android.R.attr.colorForeground,typedValue,true);
        mDefaultBottomBorderColor=typedValue.data;

        simpleTabColorizer=new SimpleTabColorizer();
        int color=getResources().getColor(android.R.color.white);
        simpleTabColorizer.setmIndicatorColors(color);
        simpleTabColorizer.setmSelectedColor(color);
        simpleTabColorizer.setmNormalColor(getResources().getColor(android.R.color.darker_gray));

        mBottomBorderThickness= (int) (2*density);
        mBottomBorderPaint=new Paint();
        mBottomBorderPaint.setColor(mDefaultBottomBorderColor);

        TypedArray array=context.obtainStyledAttributes(R.styleable.Theme);
        mPrimaryColor=array.getColor(R.styleable.Theme_colorPrimary,0);
    }

    public void setCustomTabColorizer(SlidingTabStrip.TabColorizer tabColorizer){
        mCustomColorizer=tabColorizer;
        invalidate();
    }

    public void setSelectedIndicatorColor(int color){
        mCustomColorizer=null;
        simpleTabColorizer.setmIndicatorColors(color);
        invalidate();
    }

    public void updateTitlesView(){
        final SlidingTabStrip.TabColorizer tabColorizer=mCustomColorizer==null?simpleTabColorizer:mCustomColorizer;

        int id=getSlidingTabStrip().getmTabTextViewId();

        for(int i=0;i<getChildCount();i++){
            View view=getChildAt(i);
            TextView target= id==0? (TextView) view :(TextView) view.findViewById(id);

            int color;
            if(mSelectedPosition!=i){
                color=tabColorizer.getNormalTitleColor();
            }else{
                color=tabColorizer.getSelectedTitleColor();
            }

            target.setTextColor(color);
        }

        invalidate();
    }

    private SlidingTabStrip getSlidingTabStrip(){
        ViewParent view=getParent();

        if(view instanceof SlidingTabStrip){
            return (SlidingTabStrip) view;
        }else{
            throw new RuntimeException("SlidingTabLayout must have a parent instanceof SlidingTabStrip");
        }
    }

    public void onPagerChanged(int position,float positionOffset){
        mSelectedPosition=position;
        mPositionOffset=positionOffset;

        int id=getSlidingTabStrip().getmTabTextViewId();

        final SlidingTabStrip.TabColorizer tabColorizer=mCustomColorizer==null?simpleTabColorizer:mCustomColorizer;

        View view=getChildAt(mSelectedPosition);
        TextView targetView= id==0? (TextView) view :(TextView) view.findViewById(id);

        int selectedColor=tabColorizer.getSelectedTitleColor();
        int normalColor=tabColorizer.getNormalTitleColor();

        if(mPositionOffset>0f&&mSelectedPosition<(getChildCount()-1)){
            TextView next= (TextView) getChildAt(mSelectedPosition+1);

            int selectedBlend=blendColor(selectedColor,normalColor,1.0f-mPositionOffset);
            int nextBlend=blendColor(selectedColor,normalColor,mPositionOffset);

            targetView.setTextColor(selectedBlend);
            next.setTextColor(nextBlend);
        }

        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        final int height=getHeight();
        final int childCount=getChildCount();

        SlidingTabStrip.TabColorizer colorizer=mCustomColorizer==null?simpleTabColorizer:mCustomColorizer;

        if(childCount>0){
            View selectedTitle=getChildAt(mSelectedPosition);
            int left=selectedTitle.getLeft();
            int right=selectedTitle.getRight();
            int color=colorizer.getIndicatorColor();

            if(mPositionOffset>0f&&mSelectedPosition<childCount-1){
                View nextTitle=getChildAt(mSelectedPosition+1);

                left= (int) (mPositionOffset*nextTitle.getLeft()+(1.0f-mPositionOffset)*left);
                right= (int) (mPositionOffset*nextTitle.getRight()+(1.0f-mPositionOffset)*right);

            }

            mBottomBorderPaint.setColor(color);
            canvas.drawRect(left,height-mBottomBorderThickness,right,height,mBottomBorderPaint);
        }
    }

    private static int blendColor(int color1,int color2,float ratio){
        final float inverseRation=1f-ratio;
        float r=(Color.red(color1)*ratio)+(Color.red(color2)*inverseRation);
        float g=(Color.green(color1)*ratio)+(Color.green(color2)*inverseRation);
        float b=(Color.blue(color1)*ratio)+(Color.blue(color2)*inverseRation);
        return Color.rgb((int)r,(int)g,(int)b);
    }

    public static class SimpleTabColorizer implements SlidingTabStrip.TabColorizer{

        private int mIndicatorColors;
        private int mSelectedColor;
        private int mNormalColor;

        @Override
        public int getIndicatorColor() {
            return mIndicatorColors;
        }

        @Override
        public int getSelectedTitleColor() {
            return mSelectedColor;
        }

        @Override
        public int getNormalTitleColor() {
            return mNormalColor;
        }

        public void setmIndicatorColors(int mIndicatorColors) {
            this.mIndicatorColors = mIndicatorColors;
        }

        public void setmSelectedColor(int mSelectedColor) {
            this.mSelectedColor = mSelectedColor;
        }

        public void setmNormalColor(int mNormalColor) {
            this.mNormalColor = mNormalColor;
        }
    }

}
