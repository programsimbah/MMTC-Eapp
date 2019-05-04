package com.pengembangsebelah.stmmappxo.utils;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class CustomViewPager extends ViewPager {
    boolean enabled;
    public CustomViewPager(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.enabled=true;
    }

    public CustomViewPager(@NonNull Context context, boolean enabled) {
        super(context);
        this.enabled = enabled;
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if(this.enabled) {
            return super.onInterceptTouchEvent(ev);
        }
        return false;
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if(this.enabled) {
            return super.onTouchEvent(ev);
        }
        return false;
    }

    public void SetPagingEnable(boolean enabled){
        this.enabled=enabled;
    }
}
