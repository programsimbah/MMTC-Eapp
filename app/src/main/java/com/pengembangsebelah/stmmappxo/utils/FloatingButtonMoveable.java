package com.pengembangsebelah.stmmappxo.utils;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

public class FloatingButtonMoveable extends FloatingActionButton implements View.OnTouchListener{
    public final static float CLICK_DRAG_TOLERANCE =10;
    float downRawX,downRawY;
    float dX,dY;

    public FloatingButtonMoveable(Context context) {
        super(context);
        init();
    }

    public FloatingButtonMoveable(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public FloatingButtonMoveable(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    void init(){
        setOnTouchListener(this);
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int action = event.getAction();
        if(action==MotionEvent.ACTION_DOWN){
            downRawX=event.getRawX();
            downRawY=event.getRawY();
            dX = v.getX() - downRawX;
            dY = v.getY() - downRawY;
            return true;
        }else if(action==MotionEvent.ACTION_MOVE){
            int viewWidth = v.getWidth();
            int viewHeight = v.getHeight();

            View view=(View) v.getParent();
            int parentWidth = view.getWidth();
            int parentHeight = view.getHeight();

            float newX = event.getRawX() +dX;
            newX=Math.max(0,newX);
            newX=Math.min(parentWidth - viewWidth,newX);

            float newY = event.getRawY() +dY;
            newY = Math.max(0,newY);
            newY = Math.min(parentHeight-viewHeight,newY);

            v.animate().x(newX).y(newY).setDuration(0).start();
            return true;
        }else if(action==MotionEvent.ACTION_UP){
            float upRawX = event.getRawX();
            float upRawY = event.getRawY();

            float upDX = upRawX-downRawX;
            float upDY = upRawY-downRawY;

            if(Math.abs(upDX)<CLICK_DRAG_TOLERANCE &&Math.abs(upDY)<CLICK_DRAG_TOLERANCE){
                return performClick();
            }else {
                return true;
            }
        }else {
            return super.onTouchEvent(event);
        }
    }
}
