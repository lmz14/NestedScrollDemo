package com.lmz.viewdemo.Utils;

import android.content.Context;
import android.graphics.RectF;
import android.util.DisplayMetrics;
import android.view.View;

/**
 * @author linmeizhen
 * @date 2018/8/20
 * @description
 */
public class Util {
    public static int getDevHeight(Context context){
        DisplayMetrics metrics = context.getResources().getDisplayMetrics();
        return metrics.heightPixels;
    }

    /**
     * 获取状态栏的高度
     * @param context
     * @return
     */
    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources().getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    /**
     * 计算指定的 View 在屏幕中的坐标。
     */
    public static RectF calcViewScreenLocation(View view) {
        int[] location = new int[2];
        // 获取控件在屏幕中的位置，返回的数组分别为控件左顶点的 x、y 的值
        view.getLocationOnScreen(location);
        return new RectF(location[0], location[1], location[0] + view.getWidth(),
                location[1] + view.getHeight());
    }

}
