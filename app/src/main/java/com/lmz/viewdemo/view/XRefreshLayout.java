package com.lmz.viewdemo.view;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.andview.refreshview.callback.IHeaderCallBack;
import com.andview.refreshview.utils.Utils;
import com.lmz.viewdemo.R;

import java.util.Calendar;

/**
 * TODO:
 * Created by chenyanmo on 2016/4/6.
 * master@chenyanmo.com
 */
public class XRefreshLayout extends LinearLayout implements IHeaderCallBack {
    private ViewGroup mContent;
    private ImageView mArrowImageView;
    private CircleProgress mProgressBar;
    private TextView mHintTextView;
    private TextView mHeaderTimeTextView;
    private Animation mRotateUpAnim;
    private Animation mRotateDownAnim;
    private final int ROTATE_ANIM_DURATION = 180;
    private Context mContext;
    private Animation circleAnim;
    private boolean isRefreshing = false;
    private ImageView mRefrushImageView;//下拉刷新的图片控件

    public XRefreshLayout(Context context) {
        super(context);
        mContext = context;
        initView(context);
    }

    /**
     * @param context
     * @param attrs
     */
    public XRefreshLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    private void initView(Context context) {
        mContent = (ViewGroup) LayoutInflater.from(context).inflate(
                R.layout.xlistview_header, this);
        mArrowImageView = (ImageView) findViewById(R.id.xlistview_header_arrow);
        mHintTextView = (TextView) findViewById(R.id.xlistview_header_hint_textview);
        mHeaderTimeTextView = (TextView) findViewById(R.id.xlistview_header_last_time_textview);
        mProgressBar = (CircleProgress) findViewById(R.id.circle_progress);
        mRefrushImageView = (ImageView) findViewById(R.id.xlistview_refrush_img);

        mRotateUpAnim = new RotateAnimation(0.0f, -180.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mRotateUpAnim.setDuration(ROTATE_ANIM_DURATION);
        mRotateUpAnim.setFillAfter(true);
        mRotateDownAnim = new RotateAnimation(-180.0f, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        mRotateDownAnim.setDuration(ROTATE_ANIM_DURATION);
        mRotateDownAnim.setFillAfter(true);

        circleAnim = AnimationUtils.loadAnimation(context, R.anim.refreshing_progress_bar_rotate);
        LinearInterpolator interpolator = new LinearInterpolator();
        circleAnim.setInterpolator(interpolator);
    }

    @Override
    public void setRefreshTime(long lastRefreshTime) {

        // 获取当前时间
        Calendar mCalendar = Calendar.getInstance();
        long refreshTime = mCalendar.getTimeInMillis();
        long howLong = refreshTime - lastRefreshTime;
        int minutes = (int) (howLong / 1000 / 60);
        String refreshTimeText = null;
        Resources resources = getContext().getResources();
        if (minutes < 1) {
            refreshTimeText = resources
                    .getString(com.andview.refreshview.R.string.xrefreshview_refresh_justnow);
        } else if (minutes < 60) {
            refreshTimeText = resources
                    .getString(com.andview.refreshview.R.string.xrefreshview_refresh_minutes_ago);
            refreshTimeText = Utils.format(refreshTimeText, minutes);
        } else if (minutes < 60 * 24) {
            refreshTimeText = resources
                    .getString(com.andview.refreshview.R.string.xrefreshview_refresh_hours_ago);
            refreshTimeText = Utils.format(refreshTimeText, minutes / 60);
        } else {
            refreshTimeText = resources
                    .getString(com.andview.refreshview.R.string.xrefreshview_refresh_days_ago);
            refreshTimeText = Utils.format(refreshTimeText, minutes / 60 / 24);
        }
        mHeaderTimeTextView.setText(refreshTimeText);
//        mHeaderTimeTextView.setVisibility(View.VISIBLE);
    }

    /**
     * hide footer when disable pull load more
     */
    @Override
    public void hide() {
        setVisibility(View.GONE);
    }

    @Override
    public void show() {
        setVisibility(View.VISIBLE);
    }

    @Override
    public void onStateNormal() {
        mProgressBar.clearAnimation();
        isRefreshing = false;
//        RelativeLayout.LayoutParams params1 = (RelativeLayout.LayoutParams) mArrowImageView.getLayoutParams();
//        params1.width = DensityUtil.dip2px(mContext, 10);
//        params1.height = DensityUtil.dip2px(mContext, 10);
//        mArrowImageView.setLayoutParams(params1);
//        mArrowImageView.setBackgroundResource(R.drawable.cgf_update_renminbi_bg);
//        mArrowImageView.setVisibility(View.VISIBLE);
//        mArrowImageView.startAnimation(mRotateDownAnim);
//        mHintTextView.setVisibility(View.VISIBLE);
//        mHintTextView.setText(R.string.xlistview_header_hint_normal);
//        mProgressBar.clearAnimation();
//        mProgressBar.setVisibility(View.VISIBLE);

        getHeaderRefrushImg();
    }

    @Override
    public void onStateReady() {
        isRefreshing = false;
        mProgressBar.setBackgroundResource(R.drawable.refresh_circular);
//        mArrowImageView.setVisibility(View.VISIBLE);
//        mArrowImageView.clearAnimation();
//        mArrowImageView.startAnimation(mRotateUpAnim);
//        mHintTextView.setText(R.string.xlistview_header_hint_ready);
//        mProgressBar.setVisibility(View.VISIBLE);

//        mProgressBar.setBackgroundResource(R.color.transparent);
    }

    @Override
    public void onStateRefreshing() {
        isRefreshing = true;

//        mArrowImageView.clearAnimation();
//        RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) mArrowImageView.getLayoutParams();
//        params.width = DensityUtil.dip2px(mContext ,8);
//        params.height = DensityUtil.dip2px(mContext ,8);
//        mArrowImageView.setLayoutParams(params);
//        mArrowImageView.setBackgroundResource(R.drawable.cgf_update_renminbi_bg);
//        mArrowImageView.setVisibility(View.VISIBLE);
//        mHintTextView.setText(R.string.xlistview_header_hint_loading);
//        mProgressBar.setSubProgress(0);
        mProgressBar.startAnimation(circleAnim);
//        mProgressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onStateFinish() {

        isRefreshing = false;
//        mProgressBar.setBackgroundResource(R.color.transparent);
//        mArrowImageView.setVisibility(View.GONE);
//        mProgressBar.clearAnimation();
//        mProgressBar.setVisibility(View.GONE);
//        mHintTextView.setVisibility(View.GONE);
//        mHeaderTimeTextView.setVisibility(View.GONE);
    }

    @Override
    public void onHeaderMove(double offset, int offsetY, int deltaY) {

        if (!isRefreshing) {
            if (mProgressBar.getAnimation() == null) {
//                mProgressBar.setSubProgress((int) (offset * 100));
                mProgressBar.setRotation((int) ((-1) * offsetY));
            }
        }
    }

    @Override
    public int getHeaderHeight() {
        return mContent.getMeasuredHeight();
    }

    public void getHeaderRefrushImg() {
        Bitmap refrushBitmap = null;
        if (refrushBitmap != null) {
            mRefrushImageView.setImageBitmap(refrushBitmap);
        } else {
            mRefrushImageView.setImageResource(R.drawable.refresh_word);
        }
    }

    public boolean isRefreshing(){
        return isRefreshing;
    }
}
