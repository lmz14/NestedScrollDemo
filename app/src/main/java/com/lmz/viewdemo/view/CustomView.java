package com.lmz.viewdemo.view;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.os.Build;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import com.nineoldandroids.view.ViewHelper;

/**
 * Created by lmz on 2018/7/1.
 */

@SuppressLint("AppCompatCustomView")
public class CustomView extends View {
    private final String TAG = "CustomView";

    public CustomView(Context context) {
        super(context);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public CustomView(Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.e(TAG,"left:"+getLeft()+",top:"+getTop()+",rigth:"+getRight()+",bottom:"+getBottom());
        Log.e(TAG,"x:"+getX()+",y:"+getY());
        Log.e(TAG,"TranslationX:"+getTranslationX()+",TranslationY:"+getTranslationY());
        Log.e(TAG,"-----------------------------------------------------------------");
    }

    private float lastX,lastY;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        float x = event.getRawX();
        float y = event.getRawY();
        switch (event.getAction()){
            case MotionEvent.ACTION_MOVE:
                float deltaX = x - lastX;
                float deltaY = y - lastY;
                float mTranslationX = ViewHelper.getTranslationX(this)+deltaX;
                float mTranslationY = ViewHelper.getTranslationY(this)+deltaY;
                ViewHelper.setTranslationX(this,mTranslationX);
                ViewHelper.setTranslationY(this,mTranslationY);
                Log.e(TAG,"mTranslationX:"+mTranslationX+",mTranslationY:"+mTranslationY);
                break;
            default:
                break;
        }
        lastX = x;
        lastY = y;
        return true;
//        return super.onTouchEvent(event);
    }
}
